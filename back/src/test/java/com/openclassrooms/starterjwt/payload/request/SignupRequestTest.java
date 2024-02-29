package com.openclassrooms.starterjwt.payload.request;

import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestTest {
    @Test
    void testEquals() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");

        SignupRequest request3 = new SignupRequest();
        request3.setEmail("test3@example.com");

        assertEquals(request1, request2); // Devrait retourner true
        assertNotEquals(request1, request3); // Devrait retourner false
    }

    @Test
    void testHashCode() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");

        assertEquals(request1.hashCode(), request2.hashCode());
    }

}
