package ru.hogwarts.school.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {

	private Logger logger = LoggerFactory.getLogger(FacultyService.class);
	private final FacultyRepository repository;

	public FacultyService(FacultyRepository repository) {
		this.repository = repository;
		logger.info("FacultyService is loaded");
	}

	public Faculty createFaculty(Faculty faculty) {
		logger.info("Create faculty");
		return repository.save(faculty);
	}
	
	public Faculty getFaculty(Long id) {
		logger.info("Get faculty with identified {}", id);
		return repository.findById(id).get();
	}
	
	public Faculty editFaculty(Faculty faculty) {
		if(faculty.getId() == null || !repository.existsById(faculty.getId())) {
			logger.warn("The faculty {} is not exist", faculty);
			return null;
		}

		logger.info("Save faculty info {} to the repository.", faculty);
		return repository.save(faculty);
	}
	
	public void deleteFaculty(Long id) {
		logger.info("Delete faculty with identifier {}", id);
		repository.deleteById(id);
	}
	
	public List<Faculty> getAllFaculties() {
		logger.info("Get all faculties");
		return repository.findAll();
	}

	public List<Faculty> getFacultiesWithColor(String color) {
		logger.info("Get faculty with color {}", color);

		List<Faculty> list = getAllFaculties();
		list.removeIf(e -> !e.getColor().equals(color));
		return list;
	}

	public List<Faculty> findByNameOrColor(String name, String color) {
		logger.info("Get faculty with name {} and color {}", name, color);
		return repository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
	}

	public List<Student> getStudentsByFacultyId(Long id) {
		logger.debug("Get student of faculty with identified {}", id);
		Faculty obj = getFaculty(id);
		if(obj == null) {
			logger.warn("The faculty with identifier {} is not exist", id);
			return null;
		}
		logger.info("The faculty with identifier {} is found", id);

		return obj.getStudents();
	}
}
