package ru.hogwarts.school.service;

import java.util.List;

import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {

	private final FacultyRepository repository;

	public FacultyService(FacultyRepository repository) {
		this.repository = repository;
	}

	public Faculty createFaculty(Faculty faculty) {
		return repository.save(faculty);
	}
	
	public Faculty getFaculty(Long id) {
		return repository.findById(id).get();
	}
	
	public Faculty editFaculty(Faculty faculty) {
		if(faculty.getId() == 0 || !repository.existsById(faculty.getId()))
			return null;
		return repository.save(faculty);
	}
	
	public void deleteFaculty(Long id) {
		repository.deleteById(id);
	}
	
	public List<Faculty> getAllFaculties() {
		return repository.findAll();
	}

	public List<Faculty> getFacultiesWithColor(String color) {
		List<Faculty> list = getAllFaculties();
		list.removeIf(e -> !e.getColor().equals(color));
		return list;
	}
}
