package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @Mock
    private UserService userService;

    @Mock
    private TeacherService teacherService;

    // Utilise Mappers.getMapper pour obtenir une instance de l'implémentation générée de SessionMapper
    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Test
    void testToEntity() {
        when(teacherService.findById(anyLong())).thenAnswer(invocation -> {
            Teacher teacher = new Teacher();
            teacher.setId(invocation.getArgument(0));
            return teacher;
        });

        when(userService.findById(anyLong())).thenAnswer(invocation -> {
            User user = new User();
            user.setId(invocation.getArgument(0));
            return user;
        });
        // Create a SessionDto with sample data
        SessionDto sessionDto = new SessionDto();
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Collections.singletonList(1L));
        sessionDto.setDescription("Session Description");

        // Map to Session
        Session session = sessionMapper.toEntity(sessionDto);

        // Verify the mapping
        assertNotNull(session);
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertNotNull(session.getTeacher());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertNotNull(session.getUsers());
        assertEquals(sessionDto.getUsers().size(), session.getUsers().size());
        assertEquals(sessionDto.getUsers().get(0), session.getUsers().get(0).getId());
    }

    @Test
    void testToDto() {
        // Create a Session with sample data
        Session session = new Session();
        session.setDescription("Session Description");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);
        User user = new User();
        user.setId(1L);
        session.setUsers(List.of(user));

        // Map to SessionDto
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Verify the mapping
        assertNotNull(sessionDto);
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertNotNull(sessionDto.getUsers());
        assertEquals(session.getUsers().size(), sessionDto.getUsers().size());
        assertEquals(session.getUsers().get(0).getId(), sessionDto.getUsers().get(0));
    }
    @Test
    void whenUserListIsEmpty_thenUsersShouldBeEmpty() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Collections.emptyList());
        sessionDto.setDescription("Description");

        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(session);
        assertTrue(session.getUsers().isEmpty());
    }
    @Test
    void testToEntityList() {
        when(teacherService.findById(anyLong())).thenAnswer(invocation -> {
            Teacher teacher = new Teacher();
            teacher.setId(invocation.getArgument(0));
            return teacher;
        });

        when(userService.findById(anyLong())).thenAnswer(invocation -> {
            User user = new User();
            user.setId(invocation.getArgument(0));
            return user;
        });
        // Création d'une liste de SessionDto avec des données de test
        List<SessionDto> sessionDtos = Arrays.asList(
                new SessionDto(1L, "Session 1", new Date(), 1L, "Description for Session 1", Arrays.asList(1L, 2L), LocalDateTime.now(), LocalDateTime.now()),
                new SessionDto(2L, "Session 2", new Date(), 2L, "Description for Session 2", Arrays.asList(3L, 4L), LocalDateTime.now(), LocalDateTime.now())
        );

        // Conversion en liste de Session
        List<Session> sessions = sessionMapper.toEntity(sessionDtos);

        // Assertions pour vérifier la conversion
        assertNotNull(sessions);
        assertEquals(sessionDtos.size(), sessions.size());

        for (int i = 0; i < sessionDtos.size(); i++) {
            SessionDto dto = sessionDtos.get(i);
            Session session = sessions.get(i);

            assertEquals(dto.getId(), session.getId());
            assertEquals(dto.getName(), session.getName());
            assertEquals(dto.getDate(), session.getDate());
            assertEquals(dto.getDescription(), session.getDescription());
            // Ici, on suppose que teacherService.findById retourne un Teacher valide pour l'ID
            assertEquals(dto.getTeacher_id(), session.getTeacher().getId());
            // Ici, on suppose que userService.findById retourne un User valide pour chaque ID dans la liste
            assertEquals(dto.getUsers().size(), session.getUsers().size());
            // Autres assertions spécifiques au besoin
        }
    }
    @Test
    void testToDtoList() {

        // Création d'une liste de Session avec des données de test
        LocalDateTime now = LocalDateTime.now();
        List<Session> sessions = Arrays.asList(
                new Session(1L, "Session 1", new Date(), "Description for Session 1",
                        Teacher.builder().id(1L).build(),
                        Arrays.asList(
                                User.builder().id(1L).email("user1@example.com").firstName("First1").lastName("Last1").password("pass1").admin(true).build(),
                                User.builder().id(2L).email("user2@example.com").firstName("First2").lastName("Last2").password("pass2").admin(false).build()
                        ), now, now),
                new Session(2L, "Session 2", new Date(), "Description for Session 2",
                        Teacher.builder().id(2L).build(),
                        Arrays.asList(
                                User.builder().id(3L).email("user3@example.com").firstName("First3").lastName("Last3").password("pass3").admin(true).build(),
                                User.builder().id(4L).email("user4@example.com").firstName("First4").lastName("Last4").password("pass4").admin(false).build()
                        ), now, now)
        );

        // Conversion en liste de SessionDto
        List<SessionDto> sessionDtos = sessionMapper.toDto(sessions);

        // Assertions pour vérifier la conversion
        assertNotNull(sessionDtos);
        assertEquals(sessions.size(), sessionDtos.size());

        for (int i = 0; i < sessions.size(); i++) {
            Session session = sessions.get(i);
            SessionDto dto = sessionDtos.get(i);

            assertEquals(session.getId(), dto.getId());
            assertEquals(session.getName(), dto.getName());
            assertEquals(session.getDate(), dto.getDate());
            assertEquals(session.getDescription(), dto.getDescription());
            assertEquals(session.getTeacher().getId(), dto.getTeacher_id());
            assertEquals(session.getUsers().size(), dto.getUsers().size());
            // Autres assertions spécifiques au besoin
        }
    }
}
