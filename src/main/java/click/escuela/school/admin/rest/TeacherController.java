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

import click.escuela.school.admin.api.TeacherApi;
import click.escuela.school.admin.dto.TeacherCourseStudentsDTO;
import click.escuela.school.admin.dto.TeacherDTO;
import click.escuela.school.admin.enumerator.TeacherMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.TeacherException;
import click.escuela.school.admin.service.impl.TeacherServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

	@Operation(summary = "Get teacher by Id", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))) })
	@GetMapping(value = "/{teacherId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TeacherDTO> getById(
			@Parameter(name = "Teacher id", required = true) @PathVariable("teacherId") String teacherId)
			throws TeacherException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(teacherService.getById(teacherId));
	}

	@Operation(summary = "Get course with students", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherCourseStudentsDTO.class))) })
	@GetMapping(value = "/{teacherId}/courses", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TeacherCourseStudentsDTO> getCoursesAndStudents(@PathVariable("teacherId") String teacherId) throws TeacherException {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(teacherService.getCourseAndStudents(teacherId));
	}

	@Operation(summary = "Get teacher by schoolId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))) })
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<TeacherDTO>> getBySchoolId(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(teacherService.getBySchoolId(schoolId));
	}

	@Operation(summary = "Get teacher by courseId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDTO.class))) })
	@GetMapping(value = "course/{courseId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<TeacherDTO>> getByCourseId(
			@Parameter(name = "Course id", required = true) @PathVariable("courseId") String courseId)
			throws CourseException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(teacherService.getByCourseId(courseId));
	}

	@Operation(summary = "Create Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TeacherMessage> create(
			@Parameter(name = "School Id", required = true) @PathVariable("schoolId") String schoolId,
			@RequestBody @Validated TeacherApi teacherApi) throws TeacherException {
		teacherService.create(schoolId, teacherApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.CREATE_OK);
	}

	@Operation(summary = "Update Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TeacherMessage> update(
			@Parameter(name = "School Id", required = true) @PathVariable("schoolId") String schoolId,
			@RequestBody @Validated TeacherApi teacherApi) throws TeacherException {
		teacherService.update(schoolId, teacherApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.UPDATE_OK);
	}

	@Operation(summary = "Add courses in Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idTeacher}/add/courses")
	public ResponseEntity<TeacherMessage> addCourses(@PathVariable("idTeacher") String idTeacher,
			@RequestBody @Validated List<String> listUUIDs) throws TeacherException, CourseException {
		teacherService.addCourses(idTeacher, listUUIDs);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.UPDATE_OK);
	}

	@Operation(summary = "Delete courses from Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idTeacher}/del/courses")
	public ResponseEntity<TeacherMessage> deleteCourses(@PathVariable("idTeacher") String idTeacher,
			@RequestBody @Validated List<String> listUUIDs) throws TeacherException, CourseException {
		teacherService.deleteCourses(idTeacher, listUUIDs);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.UPDATE_OK);
	}

	@Operation(summary = "Add courses in Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idTeacher}/add/courses")
	public ResponseEntity<TeacherMessage> addCourses(@PathVariable("idTeacher") String idTeacher,
			@RequestBody @Validated List<String> listUUIDs) throws TeacherException, CourseException {
		teacherService.addCourses(idTeacher, listUUIDs);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.UPDATE_OK);
	}

	@Operation(summary = "Delete courses from Teacher", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "/{idTeacher}/del/courses")
	public ResponseEntity<TeacherMessage> deleteCourses(@PathVariable("idTeacher") String idTeacher,
			@RequestBody @Validated List<String> listUUIDs) throws TeacherException, CourseException {
		teacherService.deleteCourses(idTeacher, listUUIDs);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(TeacherMessage.UPDATE_OK);
	}

}
