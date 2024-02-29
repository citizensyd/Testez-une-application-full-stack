package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        // Configuration de base pour les mocks si nécessaire
    }

    @Test
    void loadUserByUsername_UserFound_ReturnsUserDetails() {
        // Crée un faux utilisateur pour le retour du repository
        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setEmail("test@example.com");
        fakeUser.setFirstName("Test");
        fakeUser.setLastName("User");
        fakeUser.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(fakeUser));

        // Action
        UserDetails result = userDetailsService.loadUserByUsername("test@example.com");

        // Vérification
        assertNotNull(result);
        assertEquals(fakeUser.getEmail(), result.getUsername());
        assertEquals(fakeUser.getPassword(), result.getPassword());

        // Confirme que l'interaction avec le repository s'est produite comme attendu
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        // Configure le repository pour retourner un Optional vide
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Vérification
        assertThrows(UsernameNotFoundException.class, () -> {
            // Action
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });

        // Confirme que l'interaction avec le repository s'est produite comme attendu
        verify(userRepository).findByEmail("nonexistent@example.com");
    }
}

