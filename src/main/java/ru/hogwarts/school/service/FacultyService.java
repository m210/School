package ru.hogwarts.school.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

@Service
public class FacultyService {

	private final Map<Long, Faculty> map = new HashMap<>();
	private Long id = 0L;
	
	public Faculty createFaculty(Faculty faculty) {
		faculty.setId(++id);
		map.put(id, faculty);
		return faculty;
	}
	
	public Faculty getFaculty(Long id) {
		return map.get(id);
	}
	
	public Faculty editFaculty(Faculty faculty) {
		if(!map.containsKey(faculty.getId())) {
			return null;
		}

		map.put(faculty.getId(), faculty);
		return faculty;
	}
	
	public Faculty deleteFaculty(Long id) {
		if(!map.containsKey(id)) {
			return null;
		}

		return map.remove(id);
	}
	
	public List<Faculty> getAllFaculties() {
		return map.values().stream().collect(Collectors.toList());
	}

	public List<Faculty> getFacultiesWithColor(String color) {
		List<Faculty> list = getAllFaculties();
		list.removeIf(e -> !e.getColor().equals(color));
		return list;
	}
}
