package ru.hogwarts.school;

import net.minidev.json.JSONObject;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    public Faculty getDefaultFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Hogwarts staff");

        return faculty;
    }

    @Test
    public void shouldCreateFacultyTest() throws Exception {
        Faculty faculty = getDefaultFaculty();

        JSONObject object = new JSONObject();
        object.put("name", faculty.getName());

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()));
    }

    @Test
    public void shouldReturnFacultyById() throws Exception {
        Faculty faculty = getDefaultFaculty();

        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()));
    }

    @Test
    public void shouldChangeExistFaculty() throws Exception {
        Faculty faculty = getDefaultFaculty();

        JSONObject object = new JSONObject();
        object.put("name", faculty.getName());

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(facultyService, atLeastOnce()).editFaculty(any());
    }

    @Test
    public void shouldDeleteFaculty() throws Exception {
        Faculty faculty = getDefaultFaculty();

        when(facultyRepository.getById(faculty.getId())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/{id}", faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

       verify(facultyRepository, atLeastOnce()).deleteById(faculty.getId());
    }

    @Test
    public void shouldReturnFacultiesWithColor() throws Exception {
        Faculty faculty1 = getDefaultFaculty();
        faculty1.setColor("Yellow");

        Faculty faculty2 = new Faculty();
        faculty2.setName("Faculty2");
        faculty2.setId(2L);
        faculty2.setColor("Red");

        when(facultyRepository.findAll()).thenReturn(new ArrayList<>(List.of(faculty1, faculty2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/color?color=Red")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(faculty2.getName()));
    }

    @Test
    public void shouldReturnFacultyByNameOrColor() throws Exception {
        Faculty faculty1 = getDefaultFaculty();
        faculty1.setColor("Yellow");

        Faculty faculty2 = new Faculty();
        faculty2.setName("Faculty2");
        faculty2.setId(2L);
        faculty2.setColor("Red");

        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(any(), any())).thenReturn(new ArrayList<>(List.of(faculty1, faculty2)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filter?color=" + faculty2.getColor() + "&name=" + faculty1.getName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(Lists.newArrayList(faculty1.getName(), faculty2.getName())));
    }

    @Test
    public void shouldReturnStudentsByFacultyId() throws Exception {
        Faculty faculty = getDefaultFaculty();

        Student response = new Student(10L, "Bob", 32);
        doReturn(new ArrayList<>(List.of(response)))
                .when(facultyService)
                .getStudentsByFacultyId(faculty.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/students?id="+faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                 .andExpect(jsonPath("$[*].name").value(response.getName()));
    }

}
