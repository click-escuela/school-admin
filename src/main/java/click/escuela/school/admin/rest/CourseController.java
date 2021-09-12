package click.escuela.school.admin.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.dto.CourseDTO;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.StudentException;
import click.escuela.school.admin.service.impl.CourseServiceImpl;
import click.escuela.school.admin.service.impl.StudentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

	@Operation(summary = "Create Course", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CourseMessage> create(@Parameter(name = "School Id", required = true) @PathVariable("schoolId") String schoolId,
			@RequestBody @Validated CourseApi courseApi) throws CourseException {
		courseService.create(schoolId,courseApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseMessage.CREATE_OK);
	}

	@Operation(summary = "Update/Add course in student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idCourse}/student/add/{idStudent}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CourseMessage> addStudent(@PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws CourseException, StudentException {
		studentService.addCourse(idStudent, idCourse);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseMessage.UPDATE_OK);
	}

	@Operation(summary = "Update/Delete course in student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idCourse}/student/del/{idStudent}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CourseMessage> deleteStudent(@PathVariable("idCourse") String idCourse,
			@PathVariable("idStudent") String idStudent) throws StudentException, CourseException {
		studentService.deleteCourse(idStudent, idCourse);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(CourseMessage.UPDATE_OK);
	}

}
