package com.invoice.job;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Admin {

    @Id
    private String id;
    private String firstName;
    private String lastName;

    // Getters and Setters
}
