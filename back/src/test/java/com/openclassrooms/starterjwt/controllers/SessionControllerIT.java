package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SessionControllerIT {

    private MockMvc mockMvc;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
    }

    @Test
    void findById_WhenSessionExists_ShouldReturnSessionDto() throws Exception {
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();

        when(sessionService.getById(anyLong())).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_WhenSessionDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_ShouldReturnNewSessionDto() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Nom de la session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Description de la session");
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        LocalDateTime now = LocalDateTime.now();
        sessionDto.setCreatedAt(now);
        sessionDto.setUpdatedAt(now);

        Session session = Session.builder()
                .id(1L)
                .name(sessionDto.getName())
                .date(sessionDto.getDate())
                .description(sessionDto.getDescription())
                .build();

        String jsonContent = objectMapper.writeValueAsString(sessionDto);
        System.out.println(jsonContent);

        doReturn(session).when(sessionService).create(any());
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        mockMvc.perform(post("/api/session")
                        .content(objectMapper.writeValueAsString(sessionDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void update_WhenSessionExists_ShouldReturnUpdatedSessionDto() throws Exception {
        SessionDto sessionDto = new SessionDto(
                1L,
                "Nom de la session",
                new Date(),
                1L,
                "Description de la session",
                Arrays.asList(1L, 2L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Session session = new Session();
        session.setId(1L);
        session.setName(sessionDto.getName());
        session.setDate(sessionDto.getDate());
        session.setDescription(sessionDto.getDescription());

        when(sessionService.update(eq(1L), any())).thenReturn(session); // Utilise eq(1L) pour spécifier l'ID attendu
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        mockMvc.perform(put("/api/session/1")
                        .content(objectMapper.writeValueAsString(sessionDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void save_ShouldReturnOkStatus() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(new Session());

        mockMvc.perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void participate_WhenValidIds_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void noLongerParticipate_WhenValidIds_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
