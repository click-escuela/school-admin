package click.escuela.school.admin.rest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.school.admin.api.StudentApi;
import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.dto.StudentParentDTO;
import click.escuela.school.admin.enumerator.StudentMessage;
import click.escuela.school.admin.exception.ParentException;
import click.escuela.school.admin.exception.SchoolException;

import click.escuela.school.admin.exception.StudentException;
import click.escuela.school.admin.service.impl.StudentServiceImpl;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(path = "/school/{schoolId}/student")
public class StudentController {

	@Autowired
	private StudentServiceImpl studentService;

	@Operation(summary = "Get all the students", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<StudentDTO>> getStudents() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.findAll());
	}

	@Operation(summary = "Get student by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
	@GetMapping(value = "/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<StudentDTO> getById(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId,
			@RequestParam("fullDetail") Boolean fullDetail) throws StudentException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.getById(schoolId, studentId, fullDetail));

	}

	@Operation(summary = "Get student by schoolId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<StudentDTO>> getBySchool(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId, @RequestParam("fullDetail") Boolean fullDetail) throws SchoolException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.getBySchool(schoolId, fullDetail));
	}
	
	@Operation(summary = "Get student by parentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentParentDTO.class))) })
	@GetMapping(value = "/parent/{parentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<StudentParentDTO>> getStudentsByParentId(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@Parameter(name = "Parent id", required = true) @PathVariable("parentId") String parentId,
			@RequestParam("fullDetail") Boolean fullDetail) throws ParentException {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(studentService.getStudentsByParentId(parentId, fullDetail));
	}

	@Operation(summary = "Get student by courseId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
	@GetMapping(value = "course/{courseId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<StudentDTO>> getByCourse(
			@Parameter(name = "Course id", required = true) @PathVariable("courseId") String courseId, @RequestParam("fullDetail") Boolean fullDetail) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.getByCourse(courseId, fullDetail));
	}

	@Operation(summary = "Create student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<StudentDTO> create(
			@Parameter(name = "School Id", required = true) @PathVariable("schoolId") String schoolId,
			@RequestBody @Validated StudentApi studentApi) throws StudentException, SchoolException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.create(schoolId, studentApi));

	}

	@Operation(summary = "Update student by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<StudentMessage> update(
			@Parameter(name = "School Id", required = true) @PathVariable("schoolId") String schoolId,
			@RequestBody @Validated StudentApi studentApi) throws StudentException {
		studentService.update(schoolId, studentApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(StudentMessage.UPDATE_OK);
	}

	@Operation(summary = "Delete student by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@DeleteMapping(value = "/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<StudentMessage> delete(
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId) {
		studentService.delete(studentId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(StudentMessage.DELETE_OK);
	}
}
