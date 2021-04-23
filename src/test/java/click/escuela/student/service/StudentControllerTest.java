package click.escuela.student.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.student.api.AdressApi;
import click.escuela.student.api.ParentApi;
import click.escuela.student.api.StudentApi;
import click.escuela.student.api.StudentUpdateApi;
import click.escuela.student.enumerator.GenderType;
import click.escuela.student.enumerator.StudentEnum;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.rest.StudentController;
import click.escuela.student.rest.handler.Handler;
import click.escuela.student.service.impl.StudentServiceImpl;

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
	private static String EMPTY = "";

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(studentController, "studentService", studentService);
		
		adressApi = new AdressApi("Calle falsa","6458","Nogues");
		parentApi = ParentApi.builder().adressApi(adressApi).
				birthday(LocalDate.now()).cellPhone("3534543").document("33543534").
				email("oscar.umnbetrqgmail.com").gender("F").name("oscar").surname("umbert").build();
		
		
		studentApi = StudentApi.builder().adressApi(adressApi).birthday(LocalDate.now()).document("32333222")
				.cellPhone("4534543").division("C").grade("3°").email("oscar@gmail.com").
				gender(GenderType.MALE.toString()).name("oscar").surname("umbert").parentApi(parentApi).school("1234").build();
		
		studentUpdateApi = new StudentUpdateApi(studentApi);
		studentUpdateApi.setId(UUID.randomUUID().toString());
		
		doNothing().when(studentService).create(Mockito.any());
		//doNothing().when(studentService).update(Mockito.any());
		

	}
	
	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {
		
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.CREATE_OK.name());
		
	}
	@Test
	public void whenCreateNameEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setName(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Name cannot be empty");
		
	}
	
	@Test
	public void whenCreateSurnameEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setSurname(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Surname cannot be empty");
		
	}
	
	@Test
	public void whenCreateDocumentEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setDocument(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document cannot be empty");
		
	}
	
	@Test
	public void whenCreateDocumentGreaterCharacters() throws JsonProcessingException, Exception {
		
		studentApi.setDocument("53454646546456564");
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document must be between 7 and 9 characters");
		
	}
	
	@Test
	public void whenCreateDocumentLessCharacters() throws JsonProcessingException, Exception {
		
		studentApi.setDocument("3453");
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Document must be between 7 and 9 characters");
		
	}
	
	@Test
	public void whenCreateGenderEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setGender(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Gender cannot be null");
		
	}
	
	@Test
	public void whenCreateCellphoneEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setCellPhone(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("CellPhone cannot be null");
		
	}
	//TODO ver que no funciona el empty null
	public void whenCreateAdressEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setAdressApi(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Adress cannot be null");
		
	}
	//TODO ver que no funciona el empty null
	public void whenCreateParentEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setParentApi(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Adress cannot be null");
		
	}
	
	@Test
	public void whenCreateSchoolEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setSchool(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("School cannot be null");
		
	}
	@Test
	public void whenCreateGradeEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setGrade(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Grade cannot be null");
		
	}
	
	@Test
	public void whenCreateDivisionEmpty() throws JsonProcessingException, Exception {
		
		studentApi.setDivision(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Division cannot be null");
		
	}
	
	//TODO ver que no funciona el empty null
	public void whenCreateAdressNumberEmpty() throws JsonProcessingException, Exception {
		
		adressApi.setNumber(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Number cannot be null");
		
	}
	
	//TODO ver que no funciona el empty null
	public void whenCreateAdressStreetEmpty() throws JsonProcessingException, Exception {
		
		adressApi.setNumber(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Street cannot be null");
		
	}
	//TODO ver que no funciona el empty null
	public void whenCreateAdressLocalityEmpty() throws JsonProcessingException, Exception {
		
		adressApi.setLocality(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Street cannot be null");
		
	}
	//TODO ver que no funciona el empty null
	public void whenCreateAdressNumberGreaterCharacters() throws JsonProcessingException, Exception {
		
		adressApi.setNumber("544546546464");
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Number must be between 2 and 6 characters");
		
	}
	
	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {
		
		doThrow( new TransactionException(StudentEnum.CREATE_ERROR.getCode(),
				StudentEnum.CREATE_ERROR.getDescription())).when(studentService).create(Mockito.any());
		
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.CREATE_ERROR.getDescription());
		
	}
	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
	
	@Test
	public void whenUpdatOk() throws JsonProcessingException, Exception {
		
		MvcResult result = mockMvc.perform(put("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentUpdateApi))).andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.UPDATE_OK.name());
		
	}
	
	@Test
	public void whenUpdateErrorService() throws JsonProcessingException, Exception {
		
		doThrow( new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),
				StudentEnum.UPDATE_ERROR.getDescription())).when(studentService).update(Mockito.any(),Mockito.any());
		
		MvcResult result = mockMvc.perform(put("/school/{schoolId}/student","123").
				contentType(MediaType.APPLICATION_JSON).content(toJson(studentUpdateApi))).andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(StudentEnum.UPDATE_ERROR.getDescription());
		
	}
	
	
}
