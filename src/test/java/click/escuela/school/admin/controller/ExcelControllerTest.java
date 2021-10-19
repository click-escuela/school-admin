package click.escuela.school.admin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
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

import click.escuela.school.admin.api.ExcelApi;
import click.escuela.school.admin.dto.ExcelDTO;
import click.escuela.school.admin.enumerator.ExcelMessage;
import click.escuela.school.admin.enumerator.FileStatus;
import click.escuela.school.admin.enumerator.Validation;
import click.escuela.school.admin.exception.ExcelException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Excel;
import click.escuela.school.admin.rest.ExcelController;
import click.escuela.school.admin.rest.handler.Handler;
import click.escuela.school.admin.service.impl.ExcelServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class ExcelControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private ExcelController excelController;

	@Mock
	private ExcelServiceImpl excelService;

	private ObjectMapper mapper;
	private ExcelApi excelApi;
	private UUID idExcel;
	private Integer idSchool;

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(excelController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(excelController, "excelService", excelService);

		idExcel = UUID.randomUUID();
		idSchool = 1234;
		excelApi = ExcelApi.builder().name("Archivo").file("Archivo.excel").studentCount(20).build();
		Excel excel = Excel.builder().id(idExcel).name("Archivo").date(LocalDate.now()).file("Archivo.excel")
				.studentCount(20).status(FileStatus.PENDING).build();
		List<Excel> excels = new ArrayList<>();
		excels.add(excel);

		doNothing().when(excelService).save(Mockito.anyString(),Mockito.any());
		Mockito.when(excelService.getAll()).thenReturn(Mapper.mapperToExcelsDTO(excels));
	}

	@Test
	public void whenCreateOk() throws JsonProcessingException, Exception {
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString())))
				.contains(ExcelMessage.CREATE_OK.name());
	}

	@Test
	public void whenCreateErrorNameEmpty() throws JsonProcessingException, Exception {
		excelApi.setName(StringUtils.EMPTY);
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString())))
				.contains(Validation.NAME_EMPTY.getDescription());
	}


	@Test
	public void whenCreateErrorFileEMpty() throws JsonProcessingException, Exception {
		excelApi.setFile(StringUtils.EMPTY);
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString())))
				.contains(Validation.FILE_EMPTY.getDescription());
	}

	@Test
	public void whenCreateErrorStudentCountNull() throws JsonProcessingException, Exception {
		excelApi.setStudentCount(null);
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString())))
				.contains(Validation.STUDENT_COUNT_NULL.getDescription());
	}

	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {
		doThrow(new ExcelException(ExcelMessage.CREATE_ERROR)).when(excelService).save(Mockito.anyString(),Mockito.any());
		assertThat(resultExcelApi(post("/school/{schoolId}/excel", idSchool.toString())))
				.contains(ExcelMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenGetExcelsIsOk() throws JsonProcessingException, Exception {
		List<ExcelDTO> excels = mapper.readValue(resultExcelApi(get("/school/{schoolId}/excel", idSchool.toString())),
				new TypeReference<List<ExcelDTO>>() {});
		assertThat(excels.get(0).getId()).contains(idExcel.toString());
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	private String resultExcelApi(MockHttpServletRequestBuilder requestBuilder)
			throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content(toJson(excelApi)))
				.andReturn().getResponse().getContentAsString();
	}
}
