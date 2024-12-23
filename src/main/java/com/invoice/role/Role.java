package com.invoice.role;

import com.invoice.auth.User;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter @Setter  // Lombok will generate getters and setters
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    // Many-to-many relationship with User
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    // No need to manually write getters/setters, Lombok does it for you.
}
