package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import click.escuela.school.admin.enumerator.EducationLevels;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.SchoolMessage;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.model.Student;
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
	private Long id;
	private List<Student> students;
	private Student student;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);

		id = 1L;
		school = School.builder().id(id).name("Colegio Nacional").cellPhone("47589869")
				.email("colegionacional@edu.gob.com").adress("Entre Rios 1418")
				.build();
		schoolApi = SchoolApi.builder().name("Colegio Nacional").cellPhone("47589869")
				.email("colegionacional@edu.gob.com").adress("Entre Rios 1418").countCourses(10)
				.build();
		List<School> schools = new ArrayList<>();
		schools.add(school);
		student = Student.builder().id(UUID.randomUUID()).absences(3).birthday(LocalDate.now()).cellPhone("535435")
				.document("342343232").school(school).division("B").grade("2°").email("oscar@gmail.com").gender(GenderType.MALE)
				.name("oscar").level(EducationLevels.SECUNDARIO).build();
		students = new ArrayList<>();
		students.add(student);
		school.setStudents(students);
		Optional<School> optional = Optional.of(school);

		
		Mockito.when(Mapper.mapperToSchool(schoolApi)).thenReturn(school);
		Mockito.when(schoolRepository.save(school)).thenReturn(school);
		Mockito.when(schoolRepository.findAll()).thenReturn(schools);
		Mockito.when(schoolRepository.findById(id)).thenReturn(optional);

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
	public void whenGetAllIsOk() {
		schoolServiceImpl.getAll();
		verify(schoolRepository).findAll();
	}
	
	@Test
	public void whenGetByIdIsOK() throws SchoolException {
		schoolServiceImpl.getById(id.toString());
		verify(schoolRepository).findById(id);
	}

	@Test
	public void whenGetByIdIsError() {
		id = 2L;
		assertThatExceptionOfType(SchoolException.class).isThrownBy(() -> {
			schoolServiceImpl.getById(id.toString());
		}).withMessage(SchoolMessage.GET_ERROR.getDescription());
	}
	
	@Test
	public void whenGetStudents() throws SchoolException {
		schoolServiceImpl.getStudentsById(id.toString());
		verify(schoolRepository).findById(id);
	}
	
}