package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TeacherMapperTest {

    @InjectMocks
    private TeacherMapperImpl teacherMapper;

    @BeforeEach
    void setUp() {
        // Initialise ton mapper ici si nécessaire
    }

    @Test
    void testToEntity() {
        TeacherDto teacherDto = new TeacherDto(
                1L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertNotNull(teacher);
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }

    @Test
    void testToDto() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertNotNull(teacherDto);
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        LocalDateTime now = LocalDateTime.now();
        List<TeacherDto> teacherDtos = Arrays.asList(
                new TeacherDto(1L, "Doe", "John", now, now),
                new TeacherDto(2L, "Smith", "Jane", now, now)
        );

        List<Teacher> teachers = teacherMapper.toEntity(teacherDtos);

        assertNotNull(teachers);
        assertEquals(teacherDtos.size(), teachers.size());
        assertEquals(teacherDtos.get(0).getId(), teachers.get(0).getId());
        assertEquals(teacherDtos.get(0).getFirstName(), teachers.get(0).getFirstName());
        assertEquals(teacherDtos.get(0).getLastName(), teachers.get(0).getLastName());
        assertEquals(teacherDtos.get(0).getCreatedAt(), teachers.get(0).getCreatedAt());
        assertEquals(teacherDtos.get(0).getUpdatedAt(), teachers.get(0).getUpdatedAt());
    }

    @Test
    void testToDtoList() {
        LocalDateTime now = LocalDateTime.now();
        List<Teacher> teachers = Arrays.asList(
                Teacher.builder().id(1L).lastName("Doe").firstName("John").createdAt(now).updatedAt(now).build(),
                Teacher.builder().id(2L).lastName("Smith").firstName("Jane").createdAt(now).updatedAt(now).build()
        );

        List<TeacherDto> teacherDtos = teacherMapper.toDto(teachers);

        assertNotNull(teacherDtos);
        assertEquals(teachers.size(), teacherDtos.size());
        assertEquals(teachers.get(0).getId(), teacherDtos.get(0).getId());
        assertEquals(teachers.get(0).getFirstName(), teacherDtos.get(0).getFirstName());
        assertEquals(teachers.get(0).getLastName(), teacherDtos.get(0).getLastName());
        assertEquals(teachers.get(0).getCreatedAt(), teacherDtos.get(0).getCreatedAt());
        assertEquals(teachers.get(0).getUpdatedAt(), teacherDtos.get(0).getUpdatedAt());
    }
}
