package com.example.contacts.mapper;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.dto.ContactConfigDto;
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

    public static List<ContactConfigDto> asContactConfigDto(Collection<Contact> entities) {
        return entities == null ? emptyList() : entities.stream().map(ContactMapper::asContactConfigDto).collect(toList());
    }

    public static ContactConfigDto asContactConfigDto(Contact entity) {
        return toDtoMapper.map(entity, ContactConfigDto.class);
    }

    public static Contact asEntity(ContactDto dto) {
        return toEntityMapper.map(dto, Contact.class);
    }

    public static Contact asEntity(ContactConfigDto dto) {
        return toEntityMapper.map(dto, Contact.class);
    }
}
