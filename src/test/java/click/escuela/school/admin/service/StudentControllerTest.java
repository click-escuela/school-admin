package click.escuela.school.admin.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.school.admin.api.AdressApi;
import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.api.ParentApi;
import click.escuela.school.admin.api.StudentApi;
import click.escuela.school.admin.api.StudentUpdateApi;
import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.enumerator.EducationLevels;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.StudentEnum;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Parent;
import click.escuela.school.admin.model.Student;
import click.escuela.school.admin.rest.StudentController;
import click.escuela.school.admin.rest.handler.Handler;
import click.escuela.school.admin.service.impl.StudentServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class StudentControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private StudentController studentController;

	@Mock
	private StudentServiceImpl studentService;

	private ObjectMapper mapper;
	private StudentApi studentApi;
	private StudentUpdateApi studentUpdateApi;
	private ParentApi parentApi;
	private AdressApi adressApi;
	private UUID idStudent;
	private UUID idCourse;
	private Integer idSchool;
	private static String EMPTY = "";

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(studentController, "studentService", studentService);

		idStudent = UUID.randomUUID();
		idCourse = UUID.randomUUID();
		idSchool = 1234;
		adressApi = new AdressApi("Calle falsa", "6458", "Nogues");
		parentApi = ParentApi.builder().adressApi(adressApi).birthday(LocalDate.now()).cellPhone("3534543")
				.document("33543534").email("oscar.umnbetrqgmail.com").gender(GenderType.FEMALE.toString())
				.name("oscar").surname("umbert").build();

		studentApi = StudentApi.builder().adressApi(adressApi).birthday(LocalDate.now()).document("32333222")
				.cellPhone("4534543").division("C").grade("3°").email("oscar@gmail.com")
				.level(EducationLevels.SECUNDARIO.toString()).gender(GenderType.MALE.toString()).name("oscar")
				.surname("umbert").parentApi(parentApi).schoolId(1234).build();

		studentUpdateApi = new StudentUpdateApi(studentApi);
		studentUpdateApi.setId(idStudent.toString());

		doNothing().when(studentService).create(Mockito.any());
		// doNothing().when(studentService).update(Mockito.any());

	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.CREATE_OK.name());

	}

	@Test
	public void whenCreateNameEmpty() throws JsonProcessingException, Exception {

		studentApi.setName(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Name cannot be empty");

	}

	@Test
	public void whenCreateSurnameEmpty() throws JsonProcessingException, Exception {

		studentApi.setSurname(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Surname cannot be empty");

	}

	@Test
	public void whenCreateDocumentEmpty() throws JsonProcessingException, Exception {

		studentApi.setDocument(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document cannot be empty");

	}

	@Test
	public void whenCreateDocumentGreaterCharacters() throws JsonProcessingException, Exception {

		studentApi.setDocument("53454646546456564");
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document must be between 7 and 9 characters");

	}

	@Test
	public void whenCreateDocumentLessCharacters() throws JsonProcessingException, Exception {

		studentApi.setDocument("3453");
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document must be between 7 and 9 characters");

	}

	@Test
	public void whenCreateGenderEmpty() throws JsonProcessingException, Exception {

		studentApi.setGender(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Gender cannot be null");

	}

	@Test
	public void whenCreateCellphoneEmpty() throws JsonProcessingException, Exception {

		studentApi.setCellPhone(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("CellPhone cannot be null");

	}

	@Test
	public void whenCreateAdressEmpty() throws JsonProcessingException, Exception {

		studentApi.setAdressApi(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Adress cannot be null");

	}

	@Test
	public void whenCreateParentEmpty() throws JsonProcessingException, Exception {

		studentApi.setParentApi(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Parent cannot be null");

	}

	@Test
	public void whenCreateSchoolEmpty() throws JsonProcessingException, Exception {

		studentApi.setSchoolId(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("School cannot be null");

	}

	@Test
	public void whenCreateGradeEmpty() throws JsonProcessingException, Exception {

		studentApi.setGrade(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Grade cannot be null");

	}

	@Test
	public void whenCreateDivisionEmpty() throws JsonProcessingException, Exception {

		studentApi.setDivision(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Division cannot be null");

	}

	@Test
	public void whenCreateAdressNumberEmpty() throws JsonProcessingException, Exception {

		adressApi.setNumber(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Number cannot be null");

	}

	@Test
	public void whenCreateAdressStreetEmpty() throws JsonProcessingException, Exception {

		adressApi.setStreet(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Street cannot be null");

	}

	@Test
	public void whenCreateAdressLocalityEmpty() throws JsonProcessingException, Exception {

		adressApi.setLocality(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Locality cannot be null");

	}

	@Test
	public void whenCreateAdressNumberGreaterCharacters() throws JsonProcessingException, Exception {

		adressApi.setNumber("544546546464");
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Number must be between 2 and 6 characters");

	}

	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {

		doThrow(new TransactionException(StudentEnum.CREATE_ERROR.getCode(), StudentEnum.CREATE_ERROR.getDescription()))
				.when(studentService).create(Mockito.any());

		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.CREATE_ERROR.getDescription());

	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	@Test
	public void whenUpdatOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentUpdateApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.UPDATE_OK.name());

	}

	@Test
	public void whenUpdateErrorService() throws JsonProcessingException, Exception {

		doThrow(new TransactionException(StudentEnum.UPDATE_ERROR.getCode(), StudentEnum.UPDATE_ERROR.getDescription()))
				.when(studentService).update(Mockito.any());

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/student", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(studentUpdateApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.UPDATE_ERROR.getDescription());

	}

	@Test
	public void getStudentByIdIsOk() throws JsonProcessingException, Exception {
		Student student = Student.builder().id(idStudent).absences(3).birthday(LocalDate.now()).cellPhone("535435")
				.document("342343232").division("B").grade("2°").email("oscar@gmail.com").gender(GenderType.MALE)
				.name("oscar").level(EducationLevels.SECUNDARIO).parent(new Parent()).build();
		Mockito.when(studentService.getById(idStudent.toString(), false))
				.thenReturn(Mapper.mapperToStudentDTO(student));

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders
						.get("/school/{schoolId}/student/{idStudent}?fullDetail=false", "1234", idStudent.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		StudentDTO response = mapper.readValue(result.getResponse().getContentAsString(), StudentDTO.class);
		assertThat(response).hasFieldOrPropertyWithValue("id", idStudent.toString());
	}

	@Test
	public void getStudentByIdIsError() throws JsonProcessingException, Exception {
		idStudent = UUID.randomUUID();
		doThrow(new TransactionException(StudentEnum.GET_ERROR.getCode(), StudentEnum.GET_ERROR.getDescription()))
				.when(studentService).getById(idStudent.toString(), false);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/school/{schoolId}/student/{idStudent}?fullDetail=false", "1234", idStudent.toString())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.GET_ERROR.getDescription());
	}

	@Test
	public void getStudentByIdSchoolIsOk() throws JsonProcessingException, Exception {
		Student student = Student.builder().id(idStudent).absences(3).birthday(LocalDate.now()).cellPhone("535435")
				.document("342343232").schoolId(idSchool).division("B").grade("2°").email("oscar@gmail.com")
				.gender(GenderType.MALE).name("oscar").level(EducationLevels.SECUNDARIO).parent(new Parent()).build();

		List<Student> students = new ArrayList<>();
		students.add(student);
		Mockito.when(studentService.getBySchool(idSchool.toString(), false))
				.thenReturn(Mapper.mapperToStudentsDTO(students));

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders
						.get("/school/{schoolId}/student?fullDetail=false", idSchool.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<StudentDTO>> typeReference = new TypeReference<List<StudentDTO>>() {
		};
		List<StudentDTO> studentResult = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(studentResult.get(0).getId()).contains(idStudent.toString());
	}

	@Test
	public void getStudentByIdSchoolIsError() throws JsonProcessingException, Exception {
		idSchool = 6666;
		doThrow(NullPointerException.class).when(studentService).getBySchool(idSchool.toString(), false);

		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/school/{schoolId}/student?fullDetail=false", idSchool.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	@Test
	public void getStudentByIdCourseIsOk() throws JsonProcessingException, Exception {

		List<StudentDTO> students = new ArrayList<>();
		CourseApi courseApi = CourseApi.builder().id(idCourse.toString()).year(6).division("C").countStudent(20)
				.schoolId(12345).build();
		studentApi.setId(idStudent.toString());
		studentApi.setCourseApi(courseApi);
		students.add(Mapper.mapperToStudentDTO(studentApi));

		Mockito.when(studentService.getByCourse(idCourse.toString(), false)).thenReturn(students);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/student/course/{courseId}?fullDetail=false",
						idSchool.toString(), idCourse.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<StudentDTO>> typeReference = new TypeReference<List<StudentDTO>>() {
		};
		List<StudentDTO> studentResult = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(studentResult.get(0).getId()).contains(idStudent.toString());

	}

	@Test
	public void getStudentByIdCourseIsError() throws JsonProcessingException, Exception {
		idCourse = UUID.randomUUID();
		doThrow(NullPointerException.class).when(studentService).getByCourse(idCourse.toString(), false);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/student/course/{courseId}?fullDetail=false",
						idSchool.toString(), idCourse.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

}
