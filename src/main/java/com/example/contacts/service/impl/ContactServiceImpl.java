package com.example.contacts.service.impl;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.entity.Contact;
import com.example.contacts.exception.ContactAlreadyExistException;
import com.example.contacts.exception.ContactNotFoundException;
import com.example.contacts.mapper.ContactMapper;
import com.example.contacts.repository.ContactRepository;
import com.example.contacts.service.ContactService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactDto save(ContactDto contactDto) {
        if (contactRepository.findByEmail(contactDto.getEmail()).isPresent()) {
            throw new ContactAlreadyExistException();
        }
        return ContactMapper.asDto(contactRepository.save(ContactMapper.asEntity(contactDto)));
    }

    @Override
    public ContactDto update(long id, ContactDto contactDto) {
        if (contactRepository.findByEmail(contactDto.getEmail()).isPresent()) {
            throw new ContactAlreadyExistException();
        }
        Contact contact = contactRepository.findById(id).orElseThrow(ContactNotFoundException::new);
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setEmail(contactDto.getEmail());
        contact.setPhone(contactDto.getPhone());
        return ContactMapper.asDto(contactRepository.save(contact));
    }

    @Override
    public void delete(long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(ContactNotFoundException::new);
        contactRepository.delete(contact);
    }

    @Override
    public List<ContactDto> search(ContactDto contactDto) {
        Contact contact = ContactMapper.asEntity(contactDto);
        Example<Contact> example = Example.of(contact);
        return ContactMapper.asDto(contactRepository.findAll(example));
    }

    @Override
    public ContactDto findById(long id) {
        return  ContactMapper.asDto(contactRepository.findById(id).orElseThrow(ContactNotFoundException::new));
    }
}
