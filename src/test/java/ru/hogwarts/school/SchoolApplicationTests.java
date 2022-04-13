package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        Assertions.assertThat(controller).isNotNull();
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student(666L, "Test Student", 666);

        Assertions.assertThat(restTemplate
                        .postForObject("http://localhost:" + port + "/student", student, String.class)).isNotNull();

        Student newStudent = controller.getStudentsByAge(666).get(0);
        controller.deleteStudent(newStudent.getId());
    }

    @Test
    public void testGetStudentInfo() {
        Assertions.assertThat(restTemplate
                .getForObject("http://localhost:" + port + "/student/8", String.class))
                .contains("\"id\":8");
    }

    @Test
    public void testShouldEditStudent() {
        Student student = controller.createStudent(new Student(666L, "Test Student", 777));

        Assertions.assertThat(student.getName().equals("Test Student")).isTrue();

        student.setName("Edited student");
        restTemplate.put("http://localhost:" + port + "/student", student, String.class);

        Student testStudent = controller.getStudentsByAge(777).get(0);
        Assertions.assertThat(testStudent.getName().equals("Edited student")).isTrue();

        controller.deleteStudent(testStudent.getId());
    }

    @Test
    public void testShouldDeleteStudent() {
        Student student = controller.createStudent(new Student(666L, "Deleting Student", 888));

        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/" + student.getId(), String.class))
                .contains("\"id\":" + student.getId());

        restTemplate.delete("http://localhost:" + port + "/student/" + student.getId());

        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/student/" + student.getId(), String.class))
                .contains("Internal Server Error");
    }

    @Test
    public void testGetStudentsByAge() {
        Student student = controller.createStudent(new Student(666L, "Get by Age Student 1", 666));
        Student student2 = controller.createStudent(new Student(666L, "Get by Age Student 2", 666));

        Collection<Student> list = restTemplate.getForObject("http://localhost:" + port + "/student/filter/666", Collection.class);

        try {
            Assertions.assertThat(list.size()).isEqualTo(2);
        } finally {
            controller.deleteStudent(student.getId());
            controller.deleteStudent(student2.getId());
        }
    }

    @Test
    public void testGetStudentsByAgeBetween() {
        Student student = controller.createStudent(new Student(666L, "Get by Age Student 1", 666));
        Student student2 = controller.createStudent(new Student(666L, "Get by Age Student 2", 777));
        Student student3 = controller.createStudent(new Student(666L, "Get by Age Student 3", 888));
        Student student4 = controller.createStudent(new Student(666L, "Get by Age Student 4", 999));

        try {
            Collection<Student> list = restTemplate.getForObject("http://localhost:" + port + "/student/filter/?min=666&max=888", Collection.class);
            Assertions.assertThat(list.size()).isEqualTo(3);
        } finally {
            controller.deleteStudent(student.getId());
            controller.deleteStudent(student2.getId());
            controller.deleteStudent(student3.getId());
            controller.deleteStudent(student4.getId());
        }
    }

    @Test
    public void testShouldReturnFacultyOfStudent() {
        Assertions.assertThat(restTemplate
                        .getForObject("http://localhost:" + port + "/student/faculty/8", String.class))
                .isNotNull();
    }
}
