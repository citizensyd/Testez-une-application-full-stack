package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testToEntity() {
        UserDto userDto = new UserDto(
                1L,
                "user@test.com",
                "Doe",
                "John",
                true,
                "passwordSecret",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        User user = userMapper.toEntity(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        // Le mot de passe est ignoré dans le DTO, donc pas de vérification ici
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt());
    }

    @Test
    void testToDto() {
        User user = User.builder()
                .id(1L)
                .email("user@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("passwordSecret") // Normalement pas retourné dans le DTO
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserDto userDto = userMapper.toDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.isAdmin(), userDto.isAdmin());
        // Le mot de passe n'est pas mappé vers le DTO
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        List<UserDto> userDtos = Arrays.asList(
                new UserDto(1L, "user1@test.com", "Doe", "John", true, "passwordSecret", LocalDateTime.now(), LocalDateTime.now()),
                new UserDto(2L, "user2@test.com", "Smith", "Jane", false, "passwordSecret", LocalDateTime.now(), LocalDateTime.now())
        );

        List<User> users = userMapper.toEntity(userDtos);

        assertNotNull(users);
        assertEquals(userDtos.size(), users.size());

        for (int i = 0; i < userDtos.size(); i++) {
            UserDto dto = userDtos.get(i);
            User entity = users.get(i);

            assertEquals(dto.getId(), entity.getId());
            assertEquals(dto.getEmail(), entity.getEmail());
            assertEquals(dto.getLastName(), entity.getLastName());
            assertEquals(dto.getFirstName(), entity.getFirstName());
            assertEquals(dto.isAdmin(), entity.isAdmin());
            // Le mot de passe est ignoré dans le DTO, donc pas de vérification ici
            assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
            assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
        }
    }

    @Test
    void testToDtoList() {
        List<User> users = Arrays.asList(
                User.builder().id(1L).email("user1@test.com").lastName("Doe").firstName("John").admin(true).password("passwordSecret").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build(),
                User.builder().id(2L).email("user2@test.com").lastName("Smith").firstName("Jane").admin(false).password("passwordSecret").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build()
        );

        List<UserDto> userDtos = userMapper.toDto(users);

        assertNotNull(userDtos);
        assertEquals(users.size(), userDtos.size());

        for (int i = 0; i < users.size(); i++) {
            User entity = users.get(i);
            UserDto dto = userDtos.get(i);

            assertEquals(entity.getId(), dto.getId());
            assertEquals(entity.getEmail(), dto.getEmail());
            assertEquals(entity.getLastName(), dto.getLastName());
            assertEquals(entity.getFirstName(), dto.getFirstName());
            assertEquals(entity.isAdmin(), dto.isAdmin());
            // Note: Le mot de passe n'est pas inclus dans le DTO
            assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
            assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
        }
    }
}
