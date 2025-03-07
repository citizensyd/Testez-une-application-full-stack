package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class TeacherServiceIntegrationTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll_ShouldReturnAllTeachers() {
        // Arrange
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setLastName("Doe");
        teacher1.setFirstName("John");
        teacher1.setCreatedAt(LocalDateTime.now());
        teacher1.setUpdatedAt(LocalDateTime.now());

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setLastName("Smith");
        teacher2.setFirstName("Anna");
        teacher2.setCreatedAt(LocalDateTime.now());
        teacher2.setUpdatedAt(LocalDateTime.now());
        given(teacherRepository.findAll()).willReturn(Arrays.asList(teacher1, teacher2));

        // Act
        List<Teacher> teachers = teacherService.findAll();

        // Assert
        assertThat(teachers).hasSize(2).contains(teacher1, teacher2);
    }

    @Test
    public void findById_WhenTeacherExists_ShouldReturnTeacher() {
        // Arrange
        Teacher teacher = Teacher.builder()
                .id(3L)
                .lastName("Brown")
                .firstName("David")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        given(teacherRepository.findById(teacher.getId())).willReturn(Optional.of(teacher));

        // Act
        Teacher foundTeacher = teacherService.findById(teacher.getId());

        // Assert
        assertThat(foundTeacher).isEqualTo(teacher);
    }


    @Test
    public void findById_WhenTeacherDoesNotExist_ShouldReturnNull() {
        // Arrange
        long invalidId = -1L;
        given(teacherRepository.findById(invalidId)).willReturn(Optional.empty());

        // Act
        Teacher foundTeacher = teacherService.findById(invalidId);

        // Assert
        assertThat(foundTeacher).isNull();
    }
}
