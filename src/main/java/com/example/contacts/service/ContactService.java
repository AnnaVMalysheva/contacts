package com.example.contacts.service;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.dto.SearchContactDto;

import java.util.List;

public interface ContactService {

    ContactDto add(ContactDto contactDto);

    ContactDto update(long id, ContactDto contactDto);

    void delete(long id);

    List<ContactDto> search(SearchContactDto searchContactDto);

    ContactDto findById(long id);
}
