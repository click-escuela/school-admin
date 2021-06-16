package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.TeacherException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.repository.ExcellRepository;
import click.escuela.school.admin.service.impl.CourseServiceImpl;
import click.escuela.school.admin.service.impl.TeacherServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class CourseServiceTest {

	@Mock
	private ExcellRepository excellRepository;

	@Mock
	private TeacherServiceImpl teacherService;

	private CourseServiceImpl courseServiceImpl = new CourseServiceImpl();
	private CourseApi courseApi;
	private Teacher teacher;
	private UUID id;
	private UUID teacherId;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);

		Course course = Course.builder().year(6).division("C").countStudent(20).teacher(new Teacher()).schoolId(12345)
				.build();
		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();
		teacher = Teacher.builder().id(id).name("Mariana").surname("Lopez").birthday(LocalDate.now())
				.documentType(DocumentType.DNI).document("25897863").cellPhone("1589632485").email("mariAna@gmail.com")
				.courseId(id).schoolId(1234).adress(new Adress()).build();
		Optional<Course> optional = Optional.of(course);
		id = UUID.randomUUID();
		teacherId = UUID.randomUUID();
		
		List<Course> courses= new ArrayList<>();
		courses.add(course);

		Mockito.when(Mapper.mapperToCourse(courseApi)).thenReturn(course);
		Mockito.when(excellRepository.save(course)).thenReturn(course);
		Mockito.when(excellRepository.findById(id)).thenReturn(optional);
		Mockito.when(excellRepository.findAll()).thenReturn(courses);

		// inyecta en el servicio el objeto repository
		ReflectionTestUtils.setField(courseServiceImpl, "courseRepository", excellRepository);
		ReflectionTestUtils.setField(courseServiceImpl, "teacherService", teacherService);
	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		courseServiceImpl.create(courseApi);
		verify(excellRepository).save(Mapper.mapperToCourse(courseApi));
	}

	@Test
	public void whenCreateIsError() {
		CourseApi courseApi = CourseApi.builder().year(10).division("C").countStudent(40).schoolId(85252).build();
		Mockito.when(excellRepository.save(null)).thenThrow(IllegalArgumentException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			courseServiceImpl.create(courseApi);
		}).withMessage("No se pudo crear el curso correctamente");
	}

	@Test
	public void whenAddTeacherIsOk() throws CourseException, TeacherException{
		Mockito.when(teacherService.addCourseId(teacherId.toString(), id.toString())).thenReturn(teacher);
		courseServiceImpl.addTeacher(teacherId.toString(), id.toString());
		verify(excellRepository).save(Mapper.mapperToCourse(courseApi));
	}

	@Test
	public void whenAddTeacherIsError(){
		boolean hasError = false;
		try {
			courseServiceImpl.addTeacher(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isTrue();
	}

	@Test
	public void whenDeleteTeacherIsOk() throws CourseException, TeacherException{
		doNothing().when(teacherService).deleteCourseId(teacherId.toString());
		courseServiceImpl.deleteTeacher(teacherId.toString(), id.toString());
		verify(excellRepository).save(Mapper.mapperToCourse(courseApi));
	}

	@Test
	public void whenDeleteTeacherIsError() {
		boolean hasError = false;
		try {
			courseServiceImpl.deleteTeacher(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isTrue();
	}
	
	@Test
	public void whenFindAllIsOk() {
		courseServiceImpl.findAll();
		verify(excellRepository).findAll();
	}
}