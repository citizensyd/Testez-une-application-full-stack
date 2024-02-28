package com.openclassrooms.security.services;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    UserDetailsImpl user = UserDetailsImpl.builder()
            .id(1L)
            .username("testUser")
            .firstName("Test")
            .lastName("User")
            .admin(true)
            .password("password")
            .build();

    @Test
    void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testEquals() {
        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .build();
        UserDetailsImpl user3 = UserDetailsImpl.builder()
                .id(2L)
                .build();

        assertEquals(user, user2);
        assertNotEquals(user, user3);
    }

    @Test
    void testGetters() {
        assertEquals("testUser", user.getUsername());
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertTrue(user.getAdmin());
        assertEquals("password", user.getPassword());
    }
}
