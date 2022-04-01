package ru.hogwarts.school.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
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
		Faculty obj = service.deleteFaculty(id);

		if(obj == null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(obj);
	}
	

	@GetMapping("color")
	public List<Faculty> getFacultiesWithColor(@RequestParam String color) {
		return service.getFacultiesWithColor(color);
	}
}
