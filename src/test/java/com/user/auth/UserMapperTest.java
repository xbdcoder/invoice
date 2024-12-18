package com.user.auth;

import com.invoice.auth.User;
import com.invoice.auth.UserDTO;
import com.invoice.auth.UserMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToDTO() {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setAddress("123 Wonderland");

        UserDTO userDTO = userMapper.toDTO(user);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getAddress(), userDTO.getAddress());
    }

    @Test
    void testToEntity() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(2L);
        userDTO.setName("Bob");
        userDTO.setEmail("bob@example.com");
        userDTO.setAddress("456 Fantasy Street");

        User user = userMapper.toEntity(userDTO);

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getAddress(), user.getAddress());
    }
}