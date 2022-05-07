package ru.hogwarts.school.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

@RestController
@RequestMapping("faculty")
public class FacultyController {

	private final FacultyService service;
	
	public FacultyController(FacultyService service) {
		this.service = service;
	}
	
	@PostMapping()
	public Faculty createFaculty(@RequestBody Faculty faculty) {
		return service.createFaculty(faculty);
	}
	
	@GetMapping("{id}")
	public ResponseEntity getFacultyInfo(@PathVariable Long id) {
		Faculty obj = service.getFaculty(id);
		if(obj == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(obj);
	}
	
	@PutMapping()
	public ResponseEntity editFaculty(@RequestBody Faculty faculty) {
		Faculty obj = service.editFaculty(faculty);
		if(obj == null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(obj);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deleteFaculty(@PathVariable Long id) {
		service.deleteFaculty(id);
		return ResponseEntity.ok().build();
	}
	

	@GetMapping("color")
	public List<Faculty> getFacultiesWithColor(@RequestParam String color) {
		return service.getFacultiesWithColor(color);
	}

	@GetMapping("filter")
	public List<Faculty> findByNameOrColor(@RequestParam(required = false) String name, @RequestParam(required = false) String color) {
		return service.findByNameOrColor(name, color);
	}

	@GetMapping("/all")
	public List<Faculty> getAllFaculties() {
		return service.getAllFaculties();
	}

	@GetMapping("/students")
	public ResponseEntity getStudentsByFacultyId(Long id) {
		List<Student> obj = service.getStudentsByFacultyId(id);
		if(obj == null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(obj);
	}

	@GetMapping("/getFacultyLongName")
	public ResponseEntity getFacultyLongName() {
		try {
			return ResponseEntity.ok(service.getFacultyLongName());
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/getSum/{variants}")
	public ResponseEntity getSum(@PathVariable Integer variants) {
		return ResponseEntity.ok(service.getSum(variants));
	}
}
