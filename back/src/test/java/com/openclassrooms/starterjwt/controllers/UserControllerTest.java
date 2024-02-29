package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    void findById_UserExists() {
        // Arrange
        Long id = 1L;
        User user = new User();
        user.setId(id);
        when(userService.findById(id)).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.findById(id.toString());

        // Assert
        assertEquals(response.getStatusCodeValue(), 200);
        verify(userService).findById(id);
    }

    @Test
    void findById_UserDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(userService.findById(id)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById(id.toString());

        // Assert
        assertEquals(response.getStatusCodeValue(), 404);
    }

    @Test
    void findById_InvalidIdFormat() {
        // Act
        ResponseEntity<?> response = userController.findById("invalid");

        // Assert
        assertEquals(response.getStatusCodeValue(), 400);
    }

    @Test
    void deleteUser_Success() {
        // Arrange
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("user@example.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(id)).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.save(id.toString());

        // Assert
        assertEquals(response.getStatusCodeValue(), 200);
        verify(userService).delete(id);
    }

    @Test
    void deleteUser_UserDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(userService.findById(id)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.save(id.toString());

        // Assert
        assertEquals(response.getStatusCodeValue(), 404);
    }

    @Test
    void deleteUser_Unauthorized() {
        // Arrange
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("user@example.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("anotheruser@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(id)).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.save(id.toString());

        // Assert
        assertEquals(response.getStatusCodeValue(), 401);
    }

    @Test
    void deleteUser_InvalidIdFormat() {
        // Act
        ResponseEntity<?> response = userController.save("invalid");

        // Assert
        assertEquals(response.getStatusCodeValue(), 400);
    }

}
