package com.openclassrooms.starterjwt.services;


import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    public void create_WhenCalled_ShouldSaveSession() {
        // Arrange
        Session session = new Session();
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        Session createdSession = sessionService.create(new Session());

        // Assert
        assertNotNull(createdSession);
        verify(sessionRepository).save(any(Session.class));
    }
    @Test
    public void delete_WhenCalled_ShouldUseRepositoryToDelete() {
        // Arrange
        Long sessionId = 1L;
        doNothing().when(sessionRepository).deleteById(sessionId);

        // Act
        sessionService.delete(sessionId);

        // Assert
        verify(sessionRepository).deleteById(sessionId);
    }
    @Test
    public void findAll_WhenCalled_ShouldReturnAllSessions() {
        // Arrange
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);

        // Act
        List<Session> result = sessionService.findAll();

        // Assert
        assertEquals(sessions, result);
        verify(sessionRepository).findAll();
    }
    @Test
    public void participate_WhenValidUserAndSession_ShouldAddUserToSession() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 1L;
        Session session = new Session();
        session.setUsers(new ArrayList<>());
        User user = new User();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        sessionService.participate(sessionId, userId);

        // Assert
        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    public void noLongerParticipate_WhenValidUserAndSession_ShouldRemoveUserFromSession() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Session session = new Session();
        session.setUsers(new ArrayList<>(Collections.singletonList(user)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act
        sessionService.noLongerParticipate(sessionId, userId);

        // Assert
        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }
}

