package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {
    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        // ... initialiser les autres attributs de session ...

        sessionDto = new SessionDto();
        // ... initialiser les attributs de sessionDto ...
    }

    @Test
    void findById_WhenSessionExists_ShouldReturnSessionDto() {
        // Arrange
        when(sessionService.getById(anyLong())).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> responseEntity = sessionController.findById("1");

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(sessionDto, responseEntity.getBody());
    }

    @Test
    void findById_WhenSessionDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(sessionService.getById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = sessionController.findById("1");

        // Assert
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    void findById_WhenIdIsNotNumber_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<?> responseEntity = sessionController.findById("abc");

        // Assert
        assertNotNull(responseEntity);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    void findAll_ShouldReturnListOfSessionDtos() {
        // Arrange
        when(sessionService.findAll()).thenReturn(Arrays.asList(session));
        when(sessionMapper.toDto(any(List.class))).thenReturn(Arrays.asList(sessionDto));

        // Act
        ResponseEntity<?> responseEntity = sessionController.findAll();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody() instanceof List);
        assertEquals(1, ((List<?>) responseEntity.getBody()).size());
    }

    @Test
    void create_ShouldReturnNewSessionDto() {
        // Arrange
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);

        // Act
        ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(sessionDto, responseEntity.getBody());
    }

    @Test
    void update_WhenSessionExists_ShouldReturnUpdatedSessionDto() {
        // Arrange
        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);

        // Act
        ResponseEntity<?> responseEntity = sessionController.update("1", sessionDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(sessionDto, responseEntity.getBody());
    }

    @Test
    void participate_WhenValidIds_ShouldReturnOkStatus() {
        // Arrange
        doNothing().when(sessionService).participate(anyLong(), anyLong());

        // Act
        ResponseEntity<?> responseEntity = sessionController.participate("1", "100");

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void noLongerParticipate_WhenValidIds_ShouldReturnOkStatus() {
        // Arrange
        doNothing().when(sessionService).noLongerParticipate(anyLong(), anyLong());

        // Act
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("1", "100");

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

}
