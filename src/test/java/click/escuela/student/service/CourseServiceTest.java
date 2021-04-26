package click.escuela.student.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
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

import click.escuela.student.api.AdressApi;
import click.escuela.student.api.CourseApi;
import click.escuela.student.api.ParentApi;
import click.escuela.student.api.StudentApi;
import click.escuela.student.enumerator.CourseEnum;
import click.escuela.student.enumerator.EducationLevels;
import click.escuela.student.enumerator.GenderType;
import click.escuela.student.enumerator.StudentEnum;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.mapper.Mapper;
import click.escuela.student.model.Adress;
import click.escuela.student.model.Course;
import click.escuela.student.model.Parent;
import click.escuela.student.model.Student;
import click.escuela.student.model.Teacher;
import click.escuela.student.repository.CourseRepository;
import click.escuela.student.repository.StudentRepository;
import click.escuela.student.service.impl.CourseServiceImpl;
import click.escuela.student.service.impl.StudentServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class CourseServiceTest {

	@Mock
	private CourseRepository courseRepository;
	
	private CourseServiceImpl courseServiceImpl = new CourseServiceImpl();
	private CourseApi courseApi;
	private UUID id;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);

		Course course=Course.builder().year(6).division("C").countStudent(20).teacher(new Teacher()).schoolId(12345).build();
		
		courseApi=CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();

		Optional<Course> optional=Optional.of(course);

		Mockito.when(Mapper.mapperToCourse(courseApi)).thenReturn(course);
		id = UUID.randomUUID();
		Mockito.when(courseRepository.save(course)).thenReturn(course);
		Mockito.when(courseRepository.findById(id)).thenReturn(optional);

		// inyecta en el servicio el objeto repository
		ReflectionTestUtils.setField(courseServiceImpl, "courseRepository", courseRepository);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			courseServiceImpl.create(courseApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	
	@Test
	public void whenCreateIsError() {

		CourseApi courseApi=CourseApi.builder().year(10).division("C").countStudent(40).schoolId(85252).build();

		Mockito.when(courseRepository.save(null)).thenThrow(IllegalArgumentException.class);

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			courseServiceImpl.create(courseApi);
		}).withMessage("No se pudo crear el curso correctamente");

	}

}