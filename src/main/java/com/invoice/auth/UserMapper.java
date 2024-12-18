package com.invoice.auth;

import org.mapstruct.Mapper;

// Ensure you import the necessary classes for User and UserDTO
@Mapper(componentModel = "spring")
public interface UserMapper {
    // Define the mappings for specific fields if needed
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
