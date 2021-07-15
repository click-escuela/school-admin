package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.school.admin.api.AdressApi;
import click.escuela.school.admin.api.TeacherApi;
import click.escuela.school.admin.dto.TeacherCourseStudentsDTO;
import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.TeacherMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.TeacherException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.repository.TeacherRepository;
import click.escuela.school.admin.service.impl.CourseServiceImpl;
import click.escuela.school.admin.service.impl.StudentServiceImpl;
import click.escuela.school.admin.service.impl.TeacherServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;

	@Mock
	private CourseServiceImpl courseService;

	@Mock
	private StudentServiceImpl studentService;

	private TeacherServiceImpl teacherServiceImpl = new TeacherServiceImpl();
	private TeacherApi teacherApi;
	private Teacher teacher;
	private UUID id;
	private UUID courseId;
	private Integer schoolId;
	private List<Teacher> teachers;
	private List<String> listStringIds = new ArrayList<>();
	private List<Course> courses = new ArrayList<>();
	private TeacherCourseStudentsDTO teacherDTO;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		schoolId = 1234;
		courseId = UUID.randomUUID();

		listStringIds.add(courseId.toString());
		courses.add(new Course());
		teacher = Teacher.builder().id(id).name("Mariana").surname("Lopez").birthday(LocalDate.now())
				.documentType(DocumentType.DNI).document("25897863").gender(GenderType.FEMALE).cellPhone("1589632485")
				.email("mariAna@gmail.com").courses(courses).schoolId(schoolId).adress(new Adress()).build();
		teacherApi = TeacherApi.builder().id(id.toString()).name("Mariana").surname("Lopez").birthday(LocalDate.now())
				.documentType("DNI").document("25897863").gender(GenderType.FEMALE.toString()).schoolId(schoolId)
				.cellPhone("1589632485").email("mariAna@gmail.com").adressApi(new AdressApi()).build();
		teacherDTO = TeacherCourseStudentsDTO.builder().courses(new ArrayList<>()).build();
		Optional<Teacher> optional = Optional.of(teacher);
		teachers = new ArrayList<>();
		teachers.add(teacher);

		Mockito.when(teacherRepository.findById(id)).thenReturn(optional);
		Mockito.when(Mapper.mapperToTeacher(teacherApi)).thenReturn(teacher);
		Mockito.when(Mapper.mapperToTeacher(teacherApi,teacher)).thenReturn(teacher);
		Mockito.when(Mapper.mapperToAdress(Mockito.any())).thenReturn(new Adress());
		Mockito.when(teacherRepository.save(teacher)).thenReturn(teacher);
		Mockito.when(teacherRepository.findById(id)).thenReturn(optional);
		Mockito.when(teacherRepository.findBySchoolId(schoolId)).thenReturn(teachers);
		Mockito.when(teacherRepository.findAll()).thenReturn(teachers);
		Mockito.when(Mapper.mapperToEnum(teacherApi.getGender())).thenReturn(teacher.getGender());
		Mockito.when(teacherRepository.findByDocumentAndGender(teacherApi.getDocument(),
				Mapper.mapperToEnum(teacherApi.getGender()))).thenReturn(optional);
		Mockito.when(Mapper.mapperToTeacherCourseStudentsDTO(teacher)).thenReturn(teacherDTO);

		ReflectionTestUtils.setField(teacherServiceImpl, "teacherRepository", teacherRepository);
		ReflectionTestUtils.setField(teacherServiceImpl, "courseService", courseService);
		ReflectionTestUtils.setField(teacherServiceImpl, "studentService", studentService);

	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		teacherServiceImpl.create(teacherApi);
		verify(teacherRepository).save(Mapper.mapperToTeacher(teacherApi));
	}

	@Test
	public void whenCreateIsError() {
		Mockito.when(teacherRepository.save(null)).thenThrow(IllegalArgumentException.class);
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.create(new TeacherApi());
		}).withMessage(TeacherMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenUpdateIsOk() throws TransactionException {
		teacherServiceImpl.update(teacherApi);
		verify(teacherRepository).save(Mapper.mapperToTeacher(teacherApi));
	}

	@Test
	public void whenUpdateIsError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherApi.setId(id.toString());
			teacherServiceImpl.update(teacherApi);
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetByIsOk() throws TransactionException {
		teacherServiceImpl.getById(id.toString());
		verify(teacherRepository).findById(id);
	}

	@Test
	public void whenGetByIdIsError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.getById(id.toString());
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetBySchoolIsOk() {
		teacherServiceImpl.getBySchoolId(schoolId.toString());
		verify(teacherRepository).findBySchoolId(schoolId);
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
	public void whenGetByCourseIsOk() throws CourseException {
		teacherServiceImpl.getByCourseId(courseId.toString());
		verify(courseService).findById(courseId.toString());
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

	@Test
	public void whenFindAllIsOk() {
		teacherServiceImpl.findAll();
		verify(teacherRepository).findAll();
	}

	@Test
	public void whenExistsIsOk() throws TeacherException {
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.exists(teacherApi);
		}).withMessage(TeacherMessage.EXIST.getDescription());
	}

	@Test
	public void whenAddCourseIdIsOk() throws TeacherException {
		boolean hasError = false;
		try {
			teacherServiceImpl.addCourses(id.toString(), listStringIds);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenDeleteCourseIdIsOk() throws TeacherException {
		boolean hasError = false;
		try {
			teacherServiceImpl.deleteCourses(id.toString(), listStringIds);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetCourseAndStudentsIsOk() throws TeacherException, CourseException {
		teacherServiceImpl.getCourseAndStudents(id.toString());
		verify(teacherRepository).findById(id);
	}

	@Test
	public void whenGetCourseAndStudentsIsError() {
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.getCourseAndStudents(UUID.randomUUID().toString());
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenTeacherExistsIsOk() throws TeacherException {
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.exists(teacherApi);
		}).withMessage(TeacherMessage.EXIST.getDescription());
	}
}
