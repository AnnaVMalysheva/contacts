package com.example.contacts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactConfigDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

}
