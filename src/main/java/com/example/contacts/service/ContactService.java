package com.example.contacts.service;

import com.example.contacts.dto.ContactDto;

import java.util.Optional;

public interface ContactService {

    ContactDto save(ContactDto contactDto);

    Optional<ContactDto> update(long id, ContactDto contactDto);

    void delete(long id);
}
