package com.example.contacts.service;

import com.example.contacts.dto.ContactConfigDto;
import com.example.contacts.dto.ContactDto;
import com.example.contacts.entity.Contact;
import com.example.contacts.exception.ContactAlreadyExistException;
import com.example.contacts.exception.ContactNotFoundException;
import com.example.contacts.repository.ContactRepository;
import com.example.contacts.service.impl.ContactServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.contacts.TestHelper.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ContactServiceImplTest {
	@Captor
	private ArgumentCaptor<Example<Contact>> contactExampleArgumentCaptor;

	@Captor
	private ArgumentCaptor<Contact> contactArgumentCaptor;

	@Mock
	private ContactRepository contactRepository;

	@Spy
	@InjectMocks
	private ContactServiceImpl contactServiceImpl;

	@Test
	public void getAppRequests() {

		when(contactRepository.findAll((Example<Contact>) anyObject())).thenReturn(Collections.singletonList(CONTACT));

		List<ContactConfigDto> searchResults = contactServiceImpl.search(SEARCH_CONTACT_DTO);

		verify(contactRepository, times(1)).findAll(contactExampleArgumentCaptor.capture());

		assertEquals(1L, searchResults.size());
	}

	@Test
	public void addContact() {
		when(contactRepository.save(any(Contact.class))).thenReturn(CONTACT);

		contactServiceImpl.add(CONTACT_DTO);

		verify(contactRepository).save(contactArgumentCaptor.capture());
		verify(contactRepository, times(1)).save(contactArgumentCaptor.capture());
		Contact savedContact = contactArgumentCaptor.getValue();

		assertEquals(CONTACT.getId(), savedContact.getId());
		assertEquals(CONTACT.getFirstName(), savedContact.getFirstName());
		assertEquals(CONTACT.getEmail(), savedContact.getEmail());
	}

	@Test(expected = ContactAlreadyExistException.class)
	public void addContactAlreadyExistException() {
		when(contactRepository.findByEmail(anyString())).thenReturn(Optional.of(CONTACT));

		contactServiceImpl.add(CONTACT_DTO);

		verify(contactRepository, times(0)).save(any(Contact.class));
	}

	@Test
	public void deleteContact() {
		when(contactRepository.findById(anyLong())).thenReturn(Optional.of(CONTACT));

		contactServiceImpl.delete(1L);

		verify(contactRepository, times(1)).delete(contactArgumentCaptor.capture());

		Contact savedContact = contactArgumentCaptor.getValue();

		assertEquals(CONTACT.getId(), savedContact.getId());
		assertEquals(CONTACT.getFirstName(), savedContact.getFirstName());
		assertEquals(CONTACT.getEmail(), savedContact.getEmail());
	}

	@Test(expected = ContactNotFoundException.class)
	public void deleteContactNotFoundException() {
		when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

		contactServiceImpl.delete(1L);

		verify(contactRepository, times(0)).delete(any());
	}


	@Test
	public void updateContact() {
		ContactDto updatedContactDto = ContactDto.builder().firstName("testnamenew").email("test@mail.ru").build();
		when(contactRepository.findById(anyLong())).thenReturn(Optional.of(CONTACT));

		ContactConfigDto updatedContactConfigDto = contactServiceImpl.update(1L, updatedContactDto);

		verify(contactRepository, times(1)).findById(1L);

		assertEquals(updatedContactDto.getFirstName(), updatedContactConfigDto.getFirstName());
		assertEquals(updatedContactDto.getEmail(), updatedContactConfigDto.getEmail());
	}

	@Test(expected = ContactNotFoundException.class)
	public void updateContactNotFoundException() {
		ContactDto contactDto = ContactDto.builder().firstName("testnamenew").email("test@mail.ru").build();

		when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

		contactServiceImpl.update(1L, contactDto);
		verify(contactRepository, times(1)).findById(1L);
	}

}
