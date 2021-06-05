package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.school.admin.api.SchoolApi;
import click.escuela.school.admin.dto.SchoolDTO;
import click.escuela.school.admin.enumerator.SchoolMessage;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.repository.SchoolRepository;
import click.escuela.school.admin.service.impl.SchoolServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class SchoolServiceTest {

	@Mock
	private SchoolRepository schoolRepository;

	@Mock
	ModelMapper modelMapper;
	
	private SchoolServiceImpl schoolServiceImpl = new SchoolServiceImpl();
	private SchoolApi schoolApi;
	private School school;
	private SchoolDTO schoolDTO;
	private UUID id;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);
		modelMapper = new ModelMapper();
		id = UUID.randomUUID();
		school = School.builder().id(id).name("Colegio Nacional").cellPhone("47589869")
				.email("colegionacional@edu.gob.com").adress("Entre Rios 1418").countCourses(10).countStudent(20)
				.build();
		schoolApi = SchoolApi.builder().name("Colegio Nacional").cellPhone("47589869")
				.email("colegionacional@edu.gob.com").adress("Entre Rios 1418").countCourses(10).countStudent(20)
				.build();
		schoolDTO = new SchoolDTO();
		
		Mockito.when(Mapper.mapperToSchool(schoolApi)).thenReturn(school);
		Mockito.when(schoolRepository.save(school)).thenReturn(school);

		// inyecta en el servicio el objeto repository
		ReflectionTestUtils.setField(schoolServiceImpl, "schoolRepository", schoolRepository);
	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		schoolServiceImpl.create(schoolApi);
		verify(schoolRepository).save(Mapper.mapperToSchool(schoolApi));
	}

	@Test
	public void whenCreateIsError() throws TransactionException {
		Mockito.when(schoolRepository.save(null)).thenThrow(IllegalArgumentException.class);

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			schoolServiceImpl.create(Mockito.any());
		}).withMessage(SchoolMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void convertSchoolToSchoolDTO() {
		schoolDTO = modelMapper.map(school, SchoolDTO.class);
		assertEquals(school.getName(), schoolDTO.getName());
	}

	@Test
	public void convertSchoolApiToSchool() {
		school = new School();
		school = modelMapper.map(schoolApi, School.class);
		assertEquals(school.getName(), schoolApi.getName());
	}

}