package click.escuela.school.admin.rest;

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

import click.escuela.school.admin.api.SchoolApi;
import click.escuela.school.admin.dto.SchoolDTO;
import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.enumerator.SchoolMessage;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.service.impl.SchoolServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school")
public class SchoolController {

	@Autowired
	private SchoolServiceImpl schoolService;

	@Operation(summary = "Create School", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<SchoolMessage> create(@RequestBody @Validated SchoolApi schoolApi) throws SchoolException {
		schoolService.create(schoolApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(SchoolMessage.CREATE_OK);
	}

	// Metodo de prueba
	@Operation(summary = "Get all the school", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))) })
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<SchoolDTO>> getStudents() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(schoolService.getAll());
	}

}
