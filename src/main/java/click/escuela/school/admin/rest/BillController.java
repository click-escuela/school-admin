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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.school.admin.api.BillApi;
import click.escuela.school.admin.api.BillStatusApi;
import click.escuela.school.admin.dto.BillDTO;
import click.escuela.school.admin.enumerator.BillEnum;
import click.escuela.school.admin.exception.BillException;
import click.escuela.school.admin.exception.StudentException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.service.impl.BillServiceImpl;
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
	public ResponseEntity<BillDTO> getById(@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@Parameter(name = "Bill id", required = true) @PathVariable("billId") String billId)
			throws BillException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(billService.getById(billId,schoolId));
	}

	@Operation(summary = "Get bill by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillDTO.class))) })
	@GetMapping(value = "student/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<BillDTO>> getByStudentId(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId,
			@RequestParam(required = false, value = "status") String status,
			@RequestParam(required = false, value = "month") Integer month,
			@RequestParam(required = false, value ="year") Integer year) throws StudentException{
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(billService.findBills(schoolId, studentId, status, month, year));
	}

	@Operation(summary = "Create Bill", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BillEnum> create(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId,
			@RequestBody @Validated BillApi billApi) throws BillException {

		billService.create(schoolId, studentId, billApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(BillEnum.CREATE_OK);
	}
	
	@Operation(summary = "Automatic Bills Creation ", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "/automatic", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BillEnum> automaticCreation(
			@RequestBody @Validated BillApi billApi) throws BillException {
		billService.automaticCreation(billApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(BillEnum.CREATE_OK);
	}
	
	@Operation(summary = "Update Payment Bill", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{billId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BillEnum> updatePayment(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId,
			@Parameter(name = "Bill id", required = true) @PathVariable("billId") String billId, @RequestBody @Validated BillStatusApi billStatusApi) throws TransactionException {
		billService.updatePayment(schoolId, billId,billStatusApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(BillEnum.PAYMENT_STATUS_CHANGED);
	}
	

}
