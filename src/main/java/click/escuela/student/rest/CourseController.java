package click.escuela.student.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/courses")
public class CourseController {
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getStudents() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@PostMapping(value = "")
	public ResponseEntity<?> create() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> update() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	@PutMapping(value = "/{id}/student/add/{student}")
	public ResponseEntity<?> addStudent() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	@PutMapping(value = "/{id}/student/del/{student}")
	public ResponseEntity<?> deleteStudent() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	@PutMapping(value = "/{id}/teacher/add/{teacher}")
	public ResponseEntity<?> addTeacher() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	@PutMapping(value = "/{id}/teacher/del/{teacher}")
	public ResponseEntity<?> deleteTeacher() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
}
