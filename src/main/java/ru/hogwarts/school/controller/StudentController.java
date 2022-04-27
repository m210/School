package ru.hogwarts.school.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

@RestController
@RequestMapping("student")
public class StudentController {

	// http://localhost:8080/swagger-ui/index.html#/
	private final StudentService service;
	
	public StudentController(StudentService service) {
		this.service = service;
	}
	
	@PostMapping()
	public Student createStudent(@RequestBody Student student) {
		return service.createStudent(student);
	}

	@GetMapping("{id}")
	public ResponseEntity getStudentInfo(@PathVariable Long id) {
		Student student = service.getStudent(id);
		if(student == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(student);
	}
	
	@PutMapping()
	public ResponseEntity editStudent(@RequestBody Student student) {
		Student obj = service.editStudent(student);
		if(obj == null) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(obj);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deleteStudent(@PathVariable Long id) {
		service.deleteStudent(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("filter/{age}")
	public List<Student> getStudentsByAge(@PathVariable int age) {
		return service.getStudentsByAge(age);
	}

	@GetMapping("filter/")
	public List<Student> getStudentsByAgeBetween(@RequestParam int min, @RequestParam int max) {
		return service.getStudentsByAgeBetween(min, max);
	}

	@GetMapping("/all")
	public List<Student> getAllStudents() {
		return service.getAllStudents();
	}

	@GetMapping("/faculty/{id}")
	public ResponseEntity getFacultyByStudentId(@PathVariable Long id) {
		Faculty obj = service.getFacultyByStudentId(id);
		if(obj == null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(obj);
	}

	@GetMapping("/amount")
	public int getStudentsAmount() {
		return service.getStudentsAmount();
	}

	@GetMapping("/averageAge")
	public double getAverageAge() {
		return service.getAverageAge();
	}

	@GetMapping("/lastFiveStudents")
	public List<Student> getFiveLastStudents() {
		return service.getFiveLastStudents();
	}
}
