package ru.hogwarts.school.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

@Service
public class StudentService {

	private Logger logger = LoggerFactory.getLogger(StudentService.class);

	private final StudentRepository repository;

	public StudentService(StudentRepository repository) {
		this.repository = repository;
		logger.info("StudentService is loaded");
	}

	public Student createStudent(Student student) {
		logger.info("Create student");
		return repository.save(student);
	}
	
	public Student getStudent(Long id) {
		logger.info("Get student with identified {}", id);
		return repository.findById(id).get();
	}
	
	public Student editStudent(Student student) {
		if(student.getId() == null || !repository.existsById(student.getId())) {
			logger.warn("The student {} is not exist", student);
			return null;
		}

		logger.info("Save student info {} to the repository.", student);
		return repository.save(student);
	}

	public List<Student> getStudentsByAgeBetween(int min, int max) {
		logger.info("Get student by age between {} and {}", min, max);

		return repository.findByAgeBetween(min, max);
	}
	
	public void deleteStudent(Long id) {
		logger.info("Delete student with identifier {}", id);
		repository.deleteById(id);
	}
	
	public List<Student> getAllStudents() {
		logger.info("Get all students");
		return repository.findAll();
	}

	public List<Student> getStudentsByAge(int age) {
		logger.info("Get student by age {}", age);

		List<Student> list = getAllStudents();
		list.removeIf(e -> e.getAge() != age);
		return list;
	}

	public Faculty getFacultyByStudentId(Long id) {
		logger.info("Get faculty of student with identified {}", id);
		Student student = getStudent(id);
		if(student == null) {
			logger.warn("The student with identifier {} is not exist", id);
			return null;
		}
		logger.info("The student with identifier {} is found", id);
		return student.getFaculty();
	}

	public int getStudentsAmount() {
		logger.info("Get students amount");
		return repository.getStudentsAmount();
	}

	public double getAverageAge() {
		logger.info("Get students average age");
		return repository.getAverageAge();
	}

	public List<Student> getFiveLastStudents() {
		logger.info("Get five last students");
		return  repository.getFiveLastStudents();
	}

}
