package com.example.contacts.controller;

import com.example.contacts.dto.ContactDto;
import com.example.contacts.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

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
    public ResponseEntity<ContactDto> update(@PathVariable("id") long id, @Valid @RequestBody ContactDto contactDto){
        return ResponseEntity.ok().body(contactService.update(id, contactDto));
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ContactDto> findById(@PathVariable long id){
        return ResponseEntity.ok().body(contactService.findById(id));
    }

    @PostMapping(path = {"/search"})
    public  List<ContactDto> search(@RequestBody ContactDto contactDto){
        return contactService.search(contactDto);
    }

    @DeleteMapping(path ={"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        contactService.delete(id);
        return ResponseEntity.ok().build();

    }

}
