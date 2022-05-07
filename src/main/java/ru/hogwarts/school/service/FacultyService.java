package ru.hogwarts.school.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

	public String getFacultyLongName() {
		return repository.findAll().stream()
		    .map(a -> a.getName())
		    .max((a,b) -> a.length() - b.length())
		    .orElseThrow();
	}

	public int getSum(int variants) {
		final int size = 1_000_000;
		switch(variants) {
			case 0: // 41ms
				return Stream.iterate(1, a -> a + 1).limit(size).reduce(0, (a, b) -> a + b );
			case 1: // 29ms
				return Stream.iterate(1, a -> a + 1).parallel().limit(size).reduce(0, (a, b) -> a + b );
			case 2: // 28ms
				return Stream.iterate(1, a -> a + 1).parallel().limit(size).mapToInt(a -> a).sum();
			case 3: // 3ms
				int sum = 0;
				for (int i = 0; i < size; i++) {
					sum += (i + 1);
				}
				return sum;
			case 4: // 5ms (parallel) - 8ms (successively)
				int[] arr = new int[size];
				for (int i = 0; i < size; i++) {
					arr[i] = (i + 1);
				}
				return Arrays.stream(arr).sum();
		}
		throw new RuntimeException("Doesn't support");
	}
}
