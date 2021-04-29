package click.escuela.student.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import static org.mockito.Mockito.doNothing;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.student.api.CourseApi;
import click.escuela.student.enumerator.CourseEnum;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.rest.CourseController;
import click.escuela.student.rest.handler.Handler;
import click.escuela.student.service.impl.CourseServiceImpl;
import click.escuela.student.service.impl.StudentServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class CourseControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private CourseController courseController;

	@Mock
	private CourseServiceImpl courseService;
	
	@Mock
	private StudentServiceImpl studentService;

	private ObjectMapper mapper;
	private CourseApi courseApi;
	private static String EMPTY = "";

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(courseController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(courseController, "courseService", courseService);

		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();

		doNothing().when(courseService).create(Mockito.any());
		// doNothing().when(studentService).update(Mockito.any());

	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(post("/school/{schoolId}/course", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(CourseEnum.CREATE_OK.name());

	}

	@Test
	public void whenCreateYearEmpty() throws JsonProcessingException, Exception {

		courseApi.setYear(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/course", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Year cannot be empty");

	}

	@Test
	public void whenCreateDivisonEmpty() throws JsonProcessingException, Exception {

		courseApi.setDivision(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/course", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Division cannot be empty");

	}

	@Test
	public void whenCreateCoutStudentEmpty() throws JsonProcessingException, Exception {

		courseApi.setCountStudent(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/course", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("CountStudent cannot be null");

	}

	@Test
	public void whenCreateSchoolEmpty() throws JsonProcessingException, Exception {

		courseApi.setSchoolId(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/course", "123")
				.contentType(MediaType.APPLICATION_JSON).content(toJson(courseApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("School cannot be null");

	}

	@Test
	public void whenAddStudentOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/course/{idCourse}/student/add/{idStudent}", "123",UUID.randomUUID().toString(),UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(CourseEnum.UPDATE_OK.name());

	}
	
	@Test
	public void whenAddStudentIdCourseEmpty() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/course/{idCourse}/student/add/{idStudent}", "123",EMPTY,UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");

	}
	
	@Test
	public void whenAddStudentIdStudentEmpty() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/course/{idCourse}/student/add/{idStudent}", "123",UUID.randomUUID().toString(),EMPTY)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	
	@Test
	public void whenDeleteStudentOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/course/{idCourse}/student/del/{idStudent}", "123",UUID.randomUUID().toString(),UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(CourseEnum.UPDATE_OK.name());

	}
	
	@Test
	public void whenDeleteStudentIdCourseEmpty() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/course/{idCourse}/student/del/{idStudent}", "123",EMPTY,UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");

	}
	
	@Test
	public void whenDeleteStudentIdStudentEmpty() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc.perform(put("/school/{schoolId}/course/{idCourse}/student/del/{idStudent}", "123",UUID.randomUUID().toString(),EMPTY)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");

	}
	
	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

}
