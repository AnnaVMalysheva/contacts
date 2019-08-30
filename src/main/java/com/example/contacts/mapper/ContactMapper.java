package com.example.contacts.mapper;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.entity.Contact;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ContactMapper {

    private static ModelMapper toDtoMapper = new ModelMapper();
    private static ModelMapper toEntityMapper = new ModelMapper();

    public static ContactDto asDto(Contact entity) {
        return toDtoMapper.map(entity, ContactDto.class);
    }

    public static List<ContactDto> asDto(Collection<Contact> entities) {
        return entities == null ? emptyList() : entities.stream().map(ContactMapper::asDto).collect(toList());
    }

    public static Contact asEntity(ContactDto dto) {
        return toEntityMapper.map(dto, Contact.class);
    }

    public static Set<Contact> asEntity(Collection<ContactDto> dtos) {
        return dtos == null ? emptySet() : dtos.stream().map(ContactMapper::asEntity).collect(toSet());
    }
}
