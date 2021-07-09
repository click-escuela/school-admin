package click.escuela.school.admin.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.school.admin.api.AdressApi;
import click.escuela.school.admin.api.TeacherApi;
import click.escuela.school.admin.dto.TeacherDTO;
import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.TeacherMessage;
import click.escuela.school.admin.exception.TeacherException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.rest.TeacherController;
import click.escuela.school.admin.rest.handler.Handler;
import click.escuela.school.admin.service.impl.TeacherServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class TeacherControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private TeacherController teacherController;

	@Mock
	private TeacherServiceImpl teacherService;

	private ObjectMapper mapper;
	private TeacherApi teacherApi;
	private AdressApi adressApi;
	private static String EMPTY = "";
	private String id;
	private String schoolId;
	private String courseId;

	@Before
	public void setUp() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(teacherController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(teacherController, "teacherService", teacherService);

		id= UUID.randomUUID().toString();
		schoolId= "1234";
		courseId= UUID.randomUUID().toString();
		adressApi = new AdressApi("Calle falsa", "6458", "Nogues");
		List<String> listStringIds = new ArrayList<>();
		List<Course> course = new ArrayList<>();
		listStringIds.add(courseId);
		course.add(new Course());
		
		teacherApi = TeacherApi.builder().gender(GenderType.FEMALE.toString()).name("Mariana").surname("Lopez")
				.birthday(LocalDate.now()).documentType(DocumentType.DNI.toString()).document("25897863").cellPhone("1589632485")
				.email("mariAna@gmail.com").schoolId(Integer.valueOf(schoolId)).adressApi(adressApi).build();
		Teacher teacher=Teacher.builder().id(UUID.fromString(id)).gender(GenderType.FEMALE).name("Mariana").surname("Lopez")
				.birthday(LocalDate.now()).documentType(DocumentType.DNI).document("25897863").cellPhone("1589632485")
				.email("mariAna@gmail.com").courses(course).schoolId(Integer.valueOf(schoolId)).adress(new Adress()).build();
		List<Teacher> teachers= new ArrayList<>();
		teachers.add(teacher);
		
		doNothing().when(teacherService).create(Mockito.any());
		Mockito.when(teacherService.getById(id)).thenReturn(Mapper.mapperToTeacherDTO(teacher));
		Mockito.when(teacherService.getBySchoolId(schoolId)).thenReturn(Mapper.mapperToTeachersDTO(teachers));
		Mockito.when(teacherService.getByCourseId(courseId)).thenReturn(Mapper.mapperToTeachersDTO(teachers));
	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(TeacherMessage.CREATE_OK.name());

	}

	@Test
	public void whenCreateNameEmpty() throws JsonProcessingException, Exception {

		teacherApi.setName(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Name cannot be empty");

	}

	@Test
	public void whenCreateSurnameEmpty() throws JsonProcessingException, Exception {

		teacherApi.setSurname(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Surname cannot be empty");

	}

	@Test
	public void whenCreateDocumentEmpty() throws JsonProcessingException, Exception {

		teacherApi.setDocument(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher",schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document cannot be empty");

	}
	
	@Test
	public void whenCreateSchoolNull() throws JsonProcessingException, Exception {

		teacherApi.setSchoolId(null);;
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("School ID cannot be null");

	}

	@Test
	public void whenCreateDocumentGreaterCharacters() throws JsonProcessingException, Exception {

		teacherApi.setDocument("53454646546456564");
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document must be between 7 and 9 characters");

	}

	@Test
	public void whenCreateDocumentLessCharacters() throws JsonProcessingException, Exception {

		teacherApi.setDocument("3453");
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document must be between 7 and 9 characters");

	}

	@Test
	public void whenCreateDocumentTypeEmpty() throws JsonProcessingException, Exception {

		teacherApi.setDocumentType(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher",schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document type cannot be empty");

	}

	@Test
	public void whenCreateGenderEmpty() throws JsonProcessingException, Exception {

		teacherApi.setGender(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Gender cannot be null");

	}

	@Test
	public void whenCreateCellphoneEmpty() throws JsonProcessingException, Exception {

		teacherApi.setCellPhone(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("CellPhone cannot be null");

	}

	@Test
	public void whenCreateAdressEmpty() throws JsonProcessingException, Exception {

		teacherApi.setAdressApi(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Adress cannot be null");

	}

	@Test
	public void whenCreateAdressNumberEmpty() throws JsonProcessingException, Exception {

		adressApi.setNumber(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Number cannot be null");

	}

	@Test
	public void whenCreateAdressStreetEmpty() throws JsonProcessingException, Exception {

		adressApi.setStreet(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Street cannot be null");

	}

	@Test
	public void whenCreateAdressLocalityEmpty() throws JsonProcessingException, Exception {

		adressApi.setLocality(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Locality cannot be null");

	}

	@Test
	public void whenCreateAdressNumberGreaterCharacters() throws JsonProcessingException, Exception {

		adressApi.setNumber("544546546464");
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Number must be between 2 and 6 characters");

	}

	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {

		doThrow(new TeacherException(TeacherMessage.CREATE_ERROR)).when(teacherService).create(Mockito.any());

		MvcResult result = mockMvc.perform(post("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(TeacherMessage.CREATE_ERROR.getDescription());

	}

	@Test
	public void whenUpdateOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(TeacherMessage.UPDATE_OK.name());

	}

	@Test
	public void whenUpdateErrorService() throws JsonProcessingException, Exception {

		doThrow(new TeacherException(TeacherMessage.UPDATE_ERROR)).when(teacherService).update(Mockito.any());

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/teacher", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(teacherApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(TeacherMessage.UPDATE_ERROR.getDescription());

	}
	
	@Test
	public void getByIdIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/teacher/{teacherId}", schoolId, id)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();
		TeacherDTO response = mapper.readValue(result.getResponse().getContentAsString(), TeacherDTO.class);
		assertThat(response).hasFieldOrPropertyWithValue("id", id.toString());
	}

	@Test
	public void getByIdIsError() throws JsonProcessingException, Exception {
		id = UUID.randomUUID().toString();
		doThrow(new TeacherException(TeacherMessage.GET_ERROR))
				.when(teacherService).getById(id);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/school/{schoolId}/teacher/{teacherId}", schoolId, id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void getByIdSchoolIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/teacher", schoolId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<TeacherDTO>> typeReference = new TypeReference<List<TeacherDTO>>() {};
		List<TeacherDTO> results = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(results.get(0).getId()).contains(id.toString());
	}

	@Test
	public void getByIdSchoolIsEmpty() throws JsonProcessingException, Exception {
		schoolId = "6666";
		Mockito.when(teacherService.getBySchoolId(schoolId)).thenReturn(new ArrayList<>());

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/teacher", schoolId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	@Test
	public void getByIdCourseIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/teacher/course/{courseId}", schoolId, courseId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<TeacherDTO>> typeReference = new TypeReference<List<TeacherDTO>>() {};
		List<TeacherDTO> results = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(results.get(0).getId()).contains(id.toString());
	}

	@Test
	public void getByIdCourseIsEmpty() throws JsonProcessingException, Exception {
		courseId = UUID.randomUUID().toString();
		Mockito.when(teacherService.getByCourseId(courseId)).thenReturn(new ArrayList<>());

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/teacher/course/{courseId}", schoolId, courseId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

}
