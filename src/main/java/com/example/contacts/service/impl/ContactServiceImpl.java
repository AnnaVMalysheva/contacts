package com.example.contacts.service.impl;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.dto.SearchContactDto;
import com.example.contacts.entity.Contact;
import com.example.contacts.exception.ContactAlreadyExistException;
import com.example.contacts.exception.ContactNotFoundException;
import com.example.contacts.mapper.ContactMapper;
import com.example.contacts.repository.ContactRepository;
import com.example.contacts.service.ContactService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactDto add(ContactDto contactDto) {
        contactRepository.findByEmail(contactDto.getEmail()).ifPresent(contact -> {
            throw new ContactAlreadyExistException("Contact with email " + (contactDto.getEmail() + " already exists"));
        });
        return ContactMapper.asContactDto(contactRepository.save(ContactMapper.asEntity(contactDto)));
    }

    @Transactional
    @Override
    public ContactDto update(long id, ContactDto contactDto) {
        Contact contact = contactRepository.findById(id).orElseThrow(ContactNotFoundException::new);
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setEmail(contactDto.getEmail());
        contact.setPhone(contactDto.getPhone());
        return ContactMapper.asContactDto(contact);
    }

    @Override
    public void delete(long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(ContactNotFoundException::new);
        contactRepository.delete(contact);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ContactDto> search(SearchContactDto searchContactDto) {
        Contact contact = ContactMapper.asEntity(searchContactDto);
        Example<Contact> example = Example.of(contact);
        return ContactMapper.asContactDto(contactRepository.findAll(example));
    }

    @Transactional(readOnly = true)
    @Override
    public ContactDto findById(long id) {
        return ContactMapper.asContactDto(contactRepository.findById(id).orElseThrow(ContactNotFoundException::new));
    }
}
