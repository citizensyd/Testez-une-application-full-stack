package com.openclassrooms.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private TeacherService teacherService;
    @Test
    public void findAll_WhenCalled_ShouldReturnAllTeachers() {
        // Arrange
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
        when(teacherRepository.findAll()).thenReturn(teachers);

        // Act
        List<Teacher> result = teacherService.findAll();

        // Assert
        assertEquals(teachers, result);
        verify(teacherRepository).findAll();
    }
    @Test
    public void findById_WhenTeacherExists_ShouldReturnTeacher() {
        // Arrange
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        // Act
        Teacher foundTeacher = teacherService.findById(teacherId);

        // Assert
        assertEquals(teacher, foundTeacher);
        verify(teacherRepository).findById(teacherId);
    }

    @Test
    public void findById_WhenTeacherDoesNotExist_ShouldReturnNull() {
        // Arrange
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // Act
        Teacher foundTeacher = teacherService.findById(teacherId);

        // Assert
        assertNull(foundTeacher);
        verify(teacherRepository).findById(teacherId);
    }
}
