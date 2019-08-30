package com.example.contacts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "contacts")
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contacts_ids_gen")
    @SequenceGenerator(name = "contacts_ids_gen", sequenceName = "contacts_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String phone;

    @Column(name = "create_on", insertable = false, updatable = false)
    private Date createOn;
}