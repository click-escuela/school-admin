package click.escuela.school.admin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import static org.mockito.Mockito.doThrow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.school.admin.api.BillApi;
import click.escuela.school.admin.enumerator.BillEnum;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.rest.BillController;
import click.escuela.school.admin.rest.handler.Handler;
import click.escuela.school.admin.service.impl.BillServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class BillControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private BillController billController;

	@Mock
	private BillServiceImpl billService;

	private ObjectMapper mapper;
	private BillApi billApi;
	private static String EMPTY = "";

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(billController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(billController, "billService", billService);

		billApi = BillApi.builder().period(2021).file("Mayo").amount((double) 12000).build();

		doNothing().when(billService).create(Mockito.anyString(), Mockito.any());
	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(CourseMessage.CREATE_OK.name());

	}

	@Test
	public void whenCreatePeriodEmpty() throws JsonProcessingException, Exception {

		billApi.setPeriod(null);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Period cannot be empty");

	}

	@Test
	public void whenCreateFileEmpty() throws JsonProcessingException, Exception {

		billApi.setFile(EMPTY);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("File cannot be empty");

	}

	@Test
	public void whenCreateAmountEmpty() throws JsonProcessingException, Exception {

		billApi.setAmount(null);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Amount cannot be empty");

	}

	@Test
	public void whenCreateError() throws JsonProcessingException, Exception {

		doThrow(new TransactionException(BillEnum.CREATE_ERROR.getCode(), BillEnum.CREATE_ERROR.getDescription()))
				.when(billService).create(Mockito.anyString(), Mockito.any());

		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(BillEnum.CREATE_ERROR.getDescription());

	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

}
