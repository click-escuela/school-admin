package click.escuela.school.admin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import click.escuela.school.admin.api.BillApi;
import click.escuela.school.admin.dto.BillDTO;
import click.escuela.school.admin.enumerator.BillEnum;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.enumerator.PaymentStatus;
import click.escuela.school.admin.exception.BillException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Bill;
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
	private Bill bill;
	private UUID id;
	private UUID studentId;
	private Integer schoolId;
	private List<Bill> bills;
	private static String EMPTY = "";

	@Before
	public void setup() throws BillException {
		mockMvc = MockMvcBuilders.standaloneSetup(billController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(billController, "billService", billService);

		studentId = UUID.randomUUID();
		id = UUID.randomUUID();
		schoolId = 1234;
		bill = Bill.builder().id(id).schoolId(schoolId).year(2021).month(6).status(PaymentStatus.PENDING)
				.studentId(studentId).file("Mayo").amount((double) 12000).build();
		billApi = BillApi.builder().year(2021).month(6).file("Mayo").amount((double) 12000).build();
		bills = new ArrayList<>();
		bills.add(bill);
		Mockito.when(billService.findBills(schoolId.toString(), studentId.toString(), PaymentStatus.PENDING.toString(),
				6, 2021)).thenReturn(Mapper.mapperToBillsDTO(bills));

		doNothing().when(billService).create(Mockito.anyString(), Mockito.anyString(), Mockito.any());
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
	public void whenCreateYearNull() throws JsonProcessingException, Exception {

		billApi.setYear(null);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Year cannot be empty");

	}

	@Test
	public void whenCreateMonthLess() throws JsonProcessingException, Exception {

		billApi.setMonth(0);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Month should not be less than 1");

	}

	@Test
	public void whenCreateMonthGreater() throws JsonProcessingException, Exception {

		billApi.setMonth(13);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Month should not be greater than 12");

	}

	@Test
	public void whenCreateMonthNull() throws JsonProcessingException, Exception {

		billApi.setMonth(null);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Month cannot be empty");

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

		doThrow(new BillException(BillEnum.CREATE_ERROR))
				.when(billService).create(Mockito.anyString(), Mockito.anyString(), Mockito.any());

		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/{studentId}", "123", "212121")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(BillEnum.CREATE_ERROR.getDescription());

	}

	@Test
	public void getBillByStudentIdIsOk() throws Exception {
		MvcResult result = mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/school/{schoolId}/bill/student/{studentId}?month=6&year=2021&status=PENDING",
										schoolId.toString(), studentId.toString())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<BillDTO>> typeReference = new TypeReference<List<BillDTO>>() {
		};
		List<BillDTO> studentResult = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(studentResult.get(0).getId()).contains(id.toString());
	}

	@Test
	public void getBillsByStudentIdIsEmpty() throws Exception {
		schoolId = 6666;
		studentId = UUID.randomUUID();
		doThrow(NullPointerException.class).when(billService).findBills(schoolId.toString(), studentId.toString(),
				PaymentStatus.PENDING.toString(), 6, 2021);

		MvcResult result = mockMvc
				.perform(
						MockMvcRequestBuilders
								.get("/school/{schoolId}/bill/student/{studentId}?month=6&year=2021&status=PENDING",
										schoolId.toString(), studentId.toString())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

}
