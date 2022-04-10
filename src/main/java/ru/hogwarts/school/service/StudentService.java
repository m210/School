package ru.hogwarts.school.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

@Service
public class StudentService {

	private final StudentRepository repository;

	public StudentService(StudentRepository repository) {
		this.repository = repository;
	}

	public Student createStudent(Student student) {
		return repository.save(student);
	}
	
	public Student getStudent(Long id) {
		return repository.findById(id).get();
	}
	
	public Student editStudent(Student student) {
		if(student.getId() == 0 || !repository.existsById(student.getId()))
			return null;
		return repository.save(student);
	}
	
	public void deleteStudent(Long id) {
		repository.deleteById(id);
	}
	
	public List<Student> getAllStudents() {
		return repository.findAll();
	}

	public List<Student> getStudentsByAge(int age) {
		List<Student> list = getAllStudents();
		list.removeIf(e -> e.getAge() != age);
		return list;
	}
}
