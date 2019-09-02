package com.example.contacts.service;

import com.example.contacts.dto.ContactDto;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    ContactDto save(ContactDto contactDto);

    ContactDto update(long id, ContactDto contactDto);

    void delete(long id);

    List<ContactDto> search(ContactDto contactDto);

    ContactDto findById(long id);
}
