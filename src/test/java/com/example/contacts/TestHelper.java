package com.example.contacts;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.dto.ContactConfigDto;
import com.example.contacts.entity.Contact;

public interface TestHelper {
    ContactDto CONTACT_DTO = ContactDto.builder().firstName("firstName").lastName("lastname").email("test@mail.ru").phone("12345678911").build();
    ContactConfigDto SEARCH_CONTACT_DTO = ContactConfigDto.builder().firstName("firstName").lastName("lastname").email("test@mail.ru").phone("12345678911").build();
    Contact CONTACT = Contact.builder().firstName("firstName").lastName("lastname").email("test@mail.ru").phone("12345678911").build();
    String BASE_URL = "/contact";
}
