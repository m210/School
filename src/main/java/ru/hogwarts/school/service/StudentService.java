package ru.hogwarts.school.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

@Service
public class StudentService {

	private final Map<Long, Student> map = new HashMap<>();
	private Long id = 0L;
	
	public Student createStudent(Student student) {
		student.setId(++id);
		map.put(id, student);
		return student;
	}
	
	public Student getStudent(Long id) {
		return map.get(id);
	}
	
	public Student editStudent(Student student) {
		if(!map.containsKey(student.getId())) {
			return null;
		}
		
		map.put(student.getId(), student);
		return student;
	}
	
	public Student deleteStudent(Long id) {
		if(!map.containsKey(id)) {
			return null;
		}

		return map.remove(id);
	}
	
	public List<Student> getAllStudents() {
		return map.values().stream().collect(Collectors.toList());
	}

	public List<Student> getStudentsByAge(int age) {
		List<Student> list = getAllStudents();
		list.removeIf(e -> e.getAge() != age);
		return list;
	}
}
