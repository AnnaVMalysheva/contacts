package com.example.contacts.controller;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping({"/contacts"})
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ContactDto> create(@Valid @RequestBody ContactDto contactDto){
        return new ResponseEntity<>(contactService.save(contactDto), HttpStatus.CREATED);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<ContactDto> update(@PathVariable("id") long id,
                                             @RequestBody ContactDto contactDto){

        return contactService.update(id, contactDto).map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ContactDto> findById(@PathVariable long id){
        return null;
    }

    @DeleteMapping(path ={"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        contactService.delete(id);
        return ResponseEntity.ok().build();

    }

}
