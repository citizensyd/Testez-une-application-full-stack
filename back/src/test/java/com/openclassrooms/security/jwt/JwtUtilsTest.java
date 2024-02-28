package com.openclassrooms.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.SignatureException;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "your_jwt_secret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);

        UserDetailsImpl userDetailsMock = mock(UserDetailsImpl.class);
        when(userDetailsMock.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetailsMock);
    }

    @Test
    public void whenGenerateJwtToken_thenReturnValidToken() {
        // Arrange & Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void whenGetUserNameFromJwtToken_thenReturnUsername() {
        // Arrange
        String token = jwtUtils.generateJwtToken(authentication);

        // Act
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals("testUser", username);
    }

    @Test
    public void whenValidateJwtToken_thenReturnTrue() {
        // Arrange
        String token = jwtUtils.generateJwtToken(authentication);
        System.out.println(token);

        // Act & Assert
        assertTrue(jwtUtils.validateJwtToken(token));
    }

}
