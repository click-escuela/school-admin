package click.escuela.student.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.student.api.TeacherApi;
import click.escuela.student.dto.TeacherDTO;
import click.escuela.student.enumerator.TeacherMessage;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.service.impl.TeacherServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/teacher")
public class TeacherController {

	@Autowired
	private TeacherServiceImpl teacherService;

	// Metodo de prueba
	@Operation(summary = "Get all the teachers", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))) })
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<TeacherDTO>> getAllCourses() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(teacherService.findAll());
	}

	@Operation(summary = "Create Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TeacherMessage> create(@RequestBody @Validated TeacherApi teacherApi)
			throws TransactionException {

		teacherService.create(teacherApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.CREATE_OK);
	}

}
