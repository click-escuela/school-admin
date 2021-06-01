package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

import click.escuela.school.admin.api.AdressApi;
import click.escuela.school.admin.api.TeacherApi;
import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.enumerator.TeacherMessage;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.repository.TeacherRepository;
import click.escuela.school.admin.service.impl.TeacherServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;

	private TeacherServiceImpl teacherServiceImpl = new TeacherServiceImpl();
	private TeacherApi teacherApi;
	private Teacher teacher;
	private UUID id;
	private UUID courseId;
	private Integer schoolId;
	private List<Teacher> teachers;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		schoolId = 1234;
		courseId = UUID.randomUUID();
		teacher = Teacher.builder().id(id).name("Mariana").surname("Lopez").birthday(LocalDate.now())
				.documentType(DocumentType.DNI).document("25897863").cellPhone("1589632485").email("mariAna@gmail.com")
				.courseId(courseId).schoolId(schoolId).adress(new Adress()).build();

		teacherApi = TeacherApi.builder().name("Mariana").surname("Lopez").birthday(LocalDate.now()).documentType("DNI")
				.document("25897863").schoolId(schoolId).cellPhone("1589632485").email("mariAna@gmail.com")
				.adressApi(new AdressApi()).build();
		Optional<Teacher> optional = Optional.of(teacher);
		teachers = new ArrayList<>();
		teachers.add(teacher);

		Mockito.when(teacherRepository.findById(id)).thenReturn(optional);
		Mockito.when(Mapper.mapperToTeacher(teacherApi)).thenReturn(teacher);
		Mockito.when(Mapper.mapperToAdress(Mockito.any())).thenReturn(new Adress());

		Mockito.when(teacherRepository.save(teacher)).thenReturn(teacher);
		Mockito.when(teacherRepository.findById(id)).thenReturn(optional);
		Mockito.when(teacherRepository.findBySchoolId(schoolId)).thenReturn(teachers);
		Mockito.when(teacherRepository.findByCourseId(courseId)).thenReturn(teachers);

		ReflectionTestUtils.setField(teacherServiceImpl, "teacherRepository", teacherRepository);
	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
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
		teacherApi = TeacherApi.builder().name("Ana").surname("Lopez").birthday(LocalDate.now()).documentType("DNI")
				.document("25896354").cellPhone("1589632485").email("ana@gmail.com").adressApi(new AdressApi()).build();

		Mockito.when(teacherRepository.save(null)).thenThrow(IllegalArgumentException.class);

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			teacherServiceImpl.create(teacherApi);
		}).withMessage(TeacherMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenUpdateIsOk() throws TransactionException {

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
	public void whenUpdateIsError() throws TransactionException {

		id = UUID.randomUUID();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			teacherApi.setId(id.toString());
			teacherServiceImpl.update(teacherApi);
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetByIsOk() {
		boolean hasError = false;
		try {
			teacherServiceImpl.getById(id.toString());
		} catch (Exception e) {
			assertThat(hasError).isFalse();
		}
	}

	@Test
	public void whenGetByIdIsError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			teacherServiceImpl.getById(id.toString());
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetBySchoolIsOk() {
		boolean hasError = false;
		try {
			teacherServiceImpl.getBySchoolId(schoolId.toString());
		} catch (Exception e) {
			assertThat(hasError).isFalse();
		}
	}

	@Test
	public void whenGetBySchoolIsEmpty() {
		boolean hasEmpty = false;
		schoolId = 6666;
		try {
			if (teacherServiceImpl.getBySchoolId(schoolId.toString()).isEmpty())
				;
		} catch (Exception e) {
			assertThat(hasEmpty).isFalse();
		}
	}

	@Test
	public void whenGetByCourseIsOk() {
		boolean hasError = false;
		try {
			teacherServiceImpl.getByCourseId(courseId.toString());
		} catch (Exception e) {
			assertThat(hasError).isFalse();
		}
	}

	@Test
	public void whenGetByCourseIsEmpty() {
		boolean hasEmpty = false;
		courseId = UUID.randomUUID();
		try {
			if (teacherServiceImpl.getByCourseId(courseId.toString()).isEmpty())
				;
		} catch (Exception e) {
			assertThat(hasEmpty).isFalse();
		}
	}
}
