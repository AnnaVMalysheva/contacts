package com.example.contacts.service.impl;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.entity.Contact;
import com.example.contacts.exception.ContactNotFoundException;
import com.example.contacts.mapper.ContactMapper;
import com.example.contacts.repository.ContactRepository;
import com.example.contacts.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactDto save(ContactDto contactDto) {
        return ContactMapper.asDto(contactRepository.save(ContactMapper.asEntity(contactDto)));
    }

    @Override
    public Optional<ContactDto> update(long id, ContactDto contactDto) {
        return contactRepository.findById(id).map(value -> {
            value.setFirstName(contactDto.getFirstName());
            Contact updated = contactRepository.save(value);
            return ContactMapper.asDto(updated);
        });

    }

    @Override
    public void delete(long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(ContactNotFoundException::new);
        contactRepository.delete(contact);
    }
}
