package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;
import java.util.Optional;
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

import click.escuela.school.admin.api.AdressApi;
import click.escuela.school.admin.api.TeacherApi;
import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.enumerator.TeacherMessage;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.repository.TeacherRepository;
import click.escuela.school.admin.service.impl.CourseServiceImpl;
import click.escuela.school.admin.service.impl.TeacherServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;
	
	@Mock
	private CourseServiceImpl courseService;

	private TeacherServiceImpl teacherServiceImpl = new TeacherServiceImpl();
	private TeacherApi teacherApi;
	private Teacher teacher;
	private UUID id;
	private UUID courseId;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		courseId = UUID.randomUUID();
		teacher = Teacher.builder().id(id).name("Mariana").surname("Lopez").birthday(LocalDate.now())
				.documentType(DocumentType.DNI).document("25897863").cellPhone("1589632485").email("mariAna@gmail.com")
				.courseId(courseId).adress(new Adress()).build();
		
		teacherApi = TeacherApi.builder().name("Mariana").surname("Lopez").birthday(LocalDate.now())
				.documentType("DNI").document("25897863").cellPhone("1589632485").email("mariAna@gmail.com")
				.courseId(courseId.toString()).adressApi(new AdressApi()).build();
		Optional<Teacher> optional=Optional.of(teacher);
		
		Mockito.when(teacherRepository.findById(id)).thenReturn(optional);
		Mockito.when(Mapper.mapperToTeacher(teacherApi)).thenReturn(teacher);
		Mockito.when(Mapper.mapperToAdress(Mockito.any())).thenReturn(new Adress());
		
		Mockito.when(teacherRepository.save(teacher)).thenReturn(teacher);
		
		ReflectionTestUtils.setField(teacherServiceImpl, "teacherRepository", teacherRepository);
		
		ReflectionTestUtils.setField(teacherServiceImpl, "courseService", courseService);
	}
	
	@Test
	public void whenCreateIsOk() throws TransactionException{
		Mockito.when(courseService.findById(courseId.toString())).thenReturn(Mockito.any());
		boolean hasError = false;
		try {
			teacherServiceImpl.create(teacherApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}
	
	@Test
	public void whenCreateIsError() {
		teacherApi = TeacherApi.builder().name("Ana").surname("Lopez").birthday(LocalDate.now())
				.documentType("DNI").document("25896354").cellPhone("1589632485").email("ana@gmail.com")
				.courseId(UUID.randomUUID().toString()).adressApi(new AdressApi()).build();
		
		Mockito.when(teacherRepository.save(null)).thenThrow(IllegalArgumentException.class);
		
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			teacherServiceImpl.create(teacherApi);
		}).withMessage(TeacherMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void whenUpdateIsOk() throws TransactionException{
	
		boolean hasError = false;
		try {
			teacherApi.setId(id.toString());
			teacherServiceImpl.update(teacherApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}
	
	@Test
	public void whenUpdateIsError() throws TransactionException{
		
		id=UUID.randomUUID();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(()-> {
			teacherApi.setId(id.toString());
			teacherServiceImpl.update(teacherApi);
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
	}
}
