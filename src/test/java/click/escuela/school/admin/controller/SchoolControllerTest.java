package click.escuela.school.admin.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.school.admin.api.SchoolApi;
import click.escuela.school.admin.dto.SchoolDTO;
import click.escuela.school.admin.enumerator.SchoolMessage;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.rest.SchoolController;
import click.escuela.school.admin.rest.handler.Handler;
import click.escuela.school.admin.service.impl.SchoolServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class SchoolControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private SchoolController schoolController;

	@Mock
	private SchoolServiceImpl schoolService;

	private ObjectMapper mapper;
	private SchoolApi schoolApi;
	private School school;
	private String id;

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(schoolController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(schoolController, "schoolService", schoolService);

		id = "1234";
		school = School.builder().id(Long.valueOf(id)).name("Colegio Nacional").cellPhone("47589869")
				.email("colegionacional@edu.gob.com").adress("Entre Rios 1418")
				.build();
		schoolApi = SchoolApi.builder().name("Colegio Nacional").cellPhone("1534567890").email("nacio@edu.com.ar")
				.adress("Zuviria 2412").countCourses(23).build();
		List<School> schools = new ArrayList<>();
		schools.add(school);

		doNothing().when(schoolService).create(Mockito.any());
		Mockito.when(schoolService.getAll()).thenReturn(Mapper.mapperToSchoolsDTO(schools));
	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(SchoolMessage.CREATE_OK.name());
	}

	@Test
	public void whenNameEmpty() throws JsonProcessingException, Exception {
		schoolApi.setName(null);
		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Name cannot be empty");
	}

	@Test
	public void whenCellPhoneEmpty() throws JsonProcessingException, Exception {
		schoolApi.setCellPhone(null);
		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("CellPhone cannot be null");
	}

	@Test
	public void whenCountCourseEmpty() throws JsonProcessingException, Exception {
		schoolApi.setCountCourses(null);
		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("CountCourses cannot be null");
	}

	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {
		doThrow(new SchoolException(SchoolMessage.CREATE_ERROR)).when(schoolService).create(Mockito.any());

		MvcResult result = mockMvc
				.perform(post("/school").contentType(MediaType.APPLICATION_JSON).content(toJson(schoolApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(SchoolMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenGetAllIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/getAll").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<SchoolDTO>> typeReference = new TypeReference<List<SchoolDTO>>() {
		};
		List<SchoolDTO> results = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(results.get(0).getId()).contains(id.toString());
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
}
