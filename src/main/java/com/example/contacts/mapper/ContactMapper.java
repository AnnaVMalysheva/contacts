package com.example.contacts.mapper;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.dto.SearchContactDto;
import com.example.contacts.entity.Contact;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ContactMapper {

    private static ModelMapper toDtoMapper = new ModelMapper();
    private static ModelMapper toEntityMapper = new ModelMapper();

    public static ContactDto asContactDto(Contact entity) {
        return toDtoMapper.map(entity, ContactDto.class);
    }

    public static List<ContactDto> asContactDto(Collection<Contact> entities) {
        return entities == null ? emptyList() : entities.stream().map(ContactMapper::asContactDto).collect(toList());
    }

    public static Contact asEntity(ContactDto dto) {
        return toEntityMapper.map(dto, Contact.class);
    }

    public static Contact asEntity(SearchContactDto dto) {
        return toEntityMapper.map(dto, Contact.class);
    }
}
