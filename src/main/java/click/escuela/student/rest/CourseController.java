package click.escuela.student.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.student.api.CourseApi;
import click.escuela.student.dto.CourseDTO;
import click.escuela.student.enumerator.CourseEnum;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.service.impl.CourseServiceImpl;
import click.escuela.student.service.impl.StudentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/course")
public class CourseController {

	@Autowired
	private CourseServiceImpl courseService;

	@Autowired
	private StudentServiceImpl studentService;

	// Metodo de prueba
	@Operation(summary = "Get all the courses", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDTO.class))) })
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<CourseDTO>> getAllCourses() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(courseService.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<String> getCourse() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@Operation(summary = "Create Course", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CourseEnum> create(@RequestBody @Validated CourseApi courseApi) throws TransactionException {

		courseService.create(courseApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseEnum.CREATE_OK);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<String> update() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@Operation(summary = "Update/Add course in student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idCourse}/student/add/{idStudent}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CourseEnum> addStudent(@PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws TransactionException {
		studentService.addCourse(idStudent, idCourse);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseEnum.UPDATE_OK);
	}

	@Operation(summary = "Update/Delete course in student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idCourse}/student/del/{idStudent}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CourseEnum> deleteStudent(@PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws TransactionException {
		studentService.deleteCourse(idStudent, idCourse);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseEnum.UPDATE_OK);
	}

	@PutMapping(value = "/{id}/teacher/add/{teacher}")
	public ResponseEntity<String> addTeacher() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@PutMapping(value = "/{id}/teacher/del/{teacher}")
	public ResponseEntity<String> deleteTeacher() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> delete() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
}
