package com.openclassrooms.mapper;

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

import java.util.Collections;
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

}
