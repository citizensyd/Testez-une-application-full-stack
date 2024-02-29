package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        // ... initialiser les autres attributs du professeur ...

        teacherDto = new TeacherDto();
        // ... initialiser les attributs du TeacherDto ...
    }

    @Test
    void findById_WhenTeacherExists_ShouldReturnTeacherDto() {
        // Arrange
        when(teacherService.findById(anyLong())).thenReturn(teacher);
        when(teacherMapper.toDto(any(Teacher.class))).thenReturn(teacherDto);

        // Act
        ResponseEntity<?> responseEntity = teacherController.findById("1");

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(teacherDto, responseEntity.getBody());
    }

    @Test
    void findById_WhenTeacherDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(teacherService.findById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = teacherController.findById("1");

        // Assert
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    void findById_WhenIdIsNotNumber_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<?> responseEntity = teacherController.findById("abc");

        // Assert
        assertNotNull(responseEntity);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    void findAll_ShouldReturnListOfTeacherDtos() {
        // Arrange
        when(teacherService.findAll()).thenReturn(Arrays.asList(teacher));
        when(teacherMapper.toDto(any(List.class))).thenReturn(Arrays.asList(teacherDto));

        // Act
        ResponseEntity<?> responseEntity = teacherController.findAll();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody() instanceof List);
        assertEquals(1, ((List<?>) responseEntity.getBody()).size());
    }
}
