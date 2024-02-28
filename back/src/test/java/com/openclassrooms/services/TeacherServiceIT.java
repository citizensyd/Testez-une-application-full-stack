package com.openclassrooms.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherServiceIT {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findAll_ShouldReturnAllTeachers() {
        // Arrange - ajouter des enseignants dans la base de données
        Teacher teacher1 = new Teacher(/* initialise tes données */);
        Teacher teacher2 = new Teacher(/* initialise tes données */);
        entityManager.persist(teacher1);
        entityManager.persist(teacher2);
        entityManager.flush();

        // Act
        List<Teacher> teachers = teacherService.findAll();

        // Assert
        assertThat(teachers).hasSize(2).contains(teacher1, teacher2);
    }

    @Test
    public void findById_WhenTeacherExists_ShouldReturnTeacher() {
        // Arrange
        Teacher teacher = new Teacher(/* initialise tes données */);
        entityManager.persistAndFlush(teacher);

        // Act
        Teacher foundTeacher = teacherService.findById(teacher.getId());

        // Assert
        assertThat(foundTeacher).isEqualTo(teacher);
    }

    @Test
    public void findById_WhenTeacherDoesNotExist_ShouldReturnNull() {
        // Act
        Teacher foundTeacher = teacherService.findById(-1L); // ID inexistant

        // Assert
        assertThat(foundTeacher).isNull();
    }
}

