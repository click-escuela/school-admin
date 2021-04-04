package click.escuela.student.rest;

import javax.validation.Valid;

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

import click.escuela.student.api.StudentApi;
import click.escuela.student.api.StudentUpdateApi;
import click.escuela.student.dto.StudentDTO;
import click.escuela.student.enumerator.StudentEnum;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.service.impl.StudentService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(path = "/student")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@Operation(summary = "Get student by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
	@GetMapping(value = "/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId) throws TransactionException {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.get(studentId));
	}
	@Operation(summary = "Get student by schoolId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
	@GetMapping(value = "/school/{schoolId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getBySchool(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId) throws TransactionException {

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.getBySchool(schoolId));
	}

	@Operation(summary = "Create student", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> create(
		 @RequestBody @Validated StudentApi studentApi) throws TransactionException {
		studentService.create(studentApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(StudentEnum.CREATE_OK);
	}

	@Operation(summary = "Update student by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@Parameter(name = "Student Api", required = true) @RequestBody StudentUpdateApi studentUpdateApi) throws TransactionException {
		studentService.update(studentUpdateApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(StudentEnum.UPDATE_OK);
	}

	@Operation(summary = "Delete student by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@DeleteMapping(value = "/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId) throws TransactionException {
		studentService.delete(studentId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(StudentEnum.DELETE_OK);
	}
}
