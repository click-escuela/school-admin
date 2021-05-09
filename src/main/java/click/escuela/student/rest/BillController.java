package click.escuela.student.rest;

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

import click.escuela.student.api.BillApi;
import click.escuela.student.dto.BillDTO;
import click.escuela.student.enumerator.BillEnum;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.service.impl.BillServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/bill")
public class BillController {

	@Autowired
	private BillServiceImpl billService;

	// Metodo de prueba
	@Operation(summary = "Get all the bills", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillDTO.class))) })
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<BillDTO>> getAllCourses() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(billService.findAll());
	}

	@Operation(summary = "Get bill by billtId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillDTO.class))) })
	@GetMapping(value = "/{billId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BillDTO> getById(
			@Parameter(name = "Bill id", required = true) @PathVariable("billId") String billId)
			throws TransactionException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(billService.getById(billId));
	}

	@Operation(summary = "Create Bill", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BillEnum> create(
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId,
			@RequestBody @Validated BillApi billApi) throws TransactionException {

		billService.create(studentId, billApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(BillEnum.CREATE_OK);
	}

}
