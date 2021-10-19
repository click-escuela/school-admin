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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.school.admin.api.ExcelApi;
import click.escuela.school.admin.dto.ExcelDTO;
import click.escuela.school.admin.enumerator.ExcelMessage;
import click.escuela.school.admin.exception.ExcelException;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.service.impl.ExcelServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/excel")
public class ExcelController {

	@Autowired
	private ExcelServiceImpl excelService;

	// Metodo de prueba
	@Operation(summary = "Get all excels", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExcelDTO.class))) })
	@GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ExcelDTO>> getStudents() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(excelService.getAll());
	}

	@Operation(summary = "Save Excel", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ExcelMessage> save(@Parameter(name = "School Id", required = true) @PathVariable("schoolId") String schoolId,@RequestBody @Validated ExcelApi excelApi) throws ExcelException, SchoolException {
		excelService.save(schoolId, excelApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ExcelMessage.CREATE_OK);
	}

}
