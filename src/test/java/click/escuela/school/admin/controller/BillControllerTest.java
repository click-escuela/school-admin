package click.escuela.school.admin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
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
import click.escuela.school.admin.api.BillStatusApi;
import click.escuela.school.admin.dto.BillDTO;
import click.escuela.school.admin.enumerator.BillEnum;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.enumerator.PaymentStatus;
import click.escuela.school.admin.exception.BillException;
import click.escuela.school.admin.exception.StudentException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Bill;
import click.escuela.school.admin.model.Student;
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
	private UUID schoolId;
	private List<Bill> bills;
	private static String EMPTY = "";
	private BillStatusApi billStatus = new BillStatusApi();

	@Before
	public void setup() throws BillException, StudentException {
		mockMvc = MockMvcBuilders.standaloneSetup(billController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(billController, "billService", billService);

		studentId = UUID.randomUUID();
		id = UUID.randomUUID();
		schoolId = UUID.randomUUID();
		bill = Bill.builder().id(id).schoolId(schoolId).year(2021).month(6).status(PaymentStatus.PENDING)
				.student(new Student()).file("Mayo").amount((double) 12000).build();
		billStatus.setStatus(PaymentStatus.CANCELED.name());
		billApi = BillApi.builder().year(2021).month(6).file("Mayo").amount((double) 12000).build();
		bills = new ArrayList<>();
		bills.add(bill);
		BillDTO billDTO = Mapper.mapperToBillDTO(bill);		
		List<BillDTO> billsDTO = new ArrayList<>();
		billsDTO.add(billDTO);
		Mockito.when(billService.findBills(schoolId.toString(), studentId.toString(), PaymentStatus.PENDING.toString(),
				6, 2021)).thenReturn(Mapper.mapperToBillsDTO(bills));
		Mockito.when(billService.getById(id.toString(), schoolId.toString())).thenReturn(billDTO);
		Mockito.when(billService.findAll()).thenReturn(billsDTO);
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
		schoolId = UUID.randomUUID();
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
	
	@Test
	public void getByIdIsOk() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(resultBillApi(get("/school/{schoolId}/bill/{billId}", schoolId, id.toString())),
				BillDTO.class)).hasFieldOrPropertyWithValue("id", id.toString());
	}

	@Test
	public void getByIdIsError() throws JsonProcessingException, Exception {
		id = UUID.randomUUID();
		doThrow(new BillException(BillEnum.GET_ERROR)).when(billService).getById(id.toString(), schoolId.toString());
		assertThat(resultBillApi(get("/school/{schoolId}/bill/{billId}", schoolId, id.toString())))
				.contains(BillEnum.GET_ERROR.getDescription());
	}

	@Test
	public void getUpdatePaymentIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(put("/school/{schoolId}/bill/{billId}", schoolId, id.toString())
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billStatus)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(BillEnum.PAYMENT_STATUS_CHANGED.name());
	}

	@Test
	public void whenUpdatePaymentIsError() throws JsonProcessingException, Exception {

		doThrow(new BillException(BillEnum.GET_ERROR)).when(billService).updatePayment(Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		MvcResult result = mockMvc
				.perform(put("/school/{schoolId}/bill/{billId}", schoolId, id.toString())
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billStatus)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(BillEnum.GET_ERROR.getDescription());
	}
	
	@Test
	public void whenGetAllTest() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(resultBillApi(get("/school/{schoolId}/bill/getAll", schoolId)),
				new TypeReference<List<BillDTO>>() {}).get(0).getId()).contains(id.toString());
	}

	@Test
	public void whenAutomaticCreationIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/bill/automatic", "123")
						.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(BillEnum.CREATE_OK.name());

	}
	
	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	private String resultBillApi(MockHttpServletRequestBuilder requestBuilder)
			throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content(toJson(billApi)))
				.andReturn().getResponse().getContentAsString();
	}

}
