package com.example.contacts.service;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.dto.ContactConfigDto;

import java.util.List;

public interface ContactService {

    ContactConfigDto add(ContactDto contactDto);

    ContactConfigDto update(long id, ContactDto contactDto);

    void delete(long id);

    List<ContactConfigDto> search(ContactConfigDto contactConfigDto);

    ContactConfigDto findById(long id);
}
