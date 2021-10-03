package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.school.admin.api.ExcelApi;
import click.escuela.school.admin.enumerator.ExcelMessage;
import click.escuela.school.admin.enumerator.FileStatus;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.ExcelException;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Excel;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.repository.ExcelRepository;
import click.escuela.school.admin.service.impl.ExcelServiceImpl;
import click.escuela.school.admin.service.impl.SchoolServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class ExcelServiceTest {

	@Mock
	private ExcelRepository excelRepository;
	
	@Mock
	private SchoolServiceImpl schoolService;

	private ExcelServiceImpl excelServiceImpl = new ExcelServiceImpl();
	private ExcelApi excelApi;
	private UUID id;
	private Long idSchool;
	private List<Excel> excels;
	private Excel excel;

	@Before
	public void setUp() throws CourseException, SchoolException {
		PowerMockito.mockStatic(Mapper.class);

		idSchool = 1L;
		School school = new School();
		school.setId(idSchool);
		id = UUID.randomUUID();
		excel = Excel.builder().id(id).name("Archivo").date(LocalDate.now()).file("Archivo.excel").studentCount(20)
				.status(FileStatus.PENDING).school(school).build();
		excelApi = ExcelApi.builder().name("Archivo").file("Archivo.excel").studentCount(20).build();
		excels = new ArrayList<>();
		excels.add(excel);

		Mockito.when(Mapper.mapperToExcel(excelApi)).thenReturn(excel);
		Mockito.when(excelRepository.save(excel)).thenReturn(excel);
		Mockito.when(excelRepository.findAll()).thenReturn(excels);
		Mockito.when(schoolService.getById(idSchool.toString())).thenReturn(school);

		ReflectionTestUtils.setField(excelServiceImpl, "excelRepository", excelRepository);
		ReflectionTestUtils.setField(excelServiceImpl, "schoolService", schoolService);
	}

	@Test
	public void whenCreateIsOk() throws ExcelException, SchoolException {
		excelServiceImpl.save(idSchool.toString(), excelApi);
		verify(excelRepository).save(excel);
	}

	@Test
	public void whenCreateIsError() {
		assertThatExceptionOfType(ExcelException.class).isThrownBy(() -> {
			excelServiceImpl.save(idSchool.toString(), Mockito.any());
		}).withMessage(ExcelMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenGetAllIsOk() throws ExcelException {
		excelServiceImpl.getAll();
		verify(excelRepository).findAll();
	}

}