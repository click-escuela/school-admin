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
import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.api.ParentApi;
import click.escuela.school.admin.api.StudentApi;
import click.escuela.school.admin.enumerator.EducationLevels;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.StudentMessage;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Parent;
import click.escuela.school.admin.model.Student;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.repository.StudentRepository;
import click.escuela.school.admin.service.impl.CourseServiceImpl;
import click.escuela.school.admin.service.impl.StudentServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class StudentServiceTest {

	@Mock
	private StudentRepository studentRepository;

	@Mock
	private CourseServiceImpl courseService;

	private StudentServiceImpl studentServiceImpl = new StudentServiceImpl();
	private StudentApi studentApi;

	private CourseApi courseApi;
	private UUID id;
	private UUID idCourse;
	private Integer idSchool;
	private List<Student> students;
	private Student student;

	@Before
	public void setUp() throws TransactionException {

		PowerMockito.mockStatic(Mapper.class);

		idSchool = 1234;
		id = UUID.randomUUID();
		idCourse = UUID.randomUUID();
		Course course = Course.builder().id(idCourse).year(6).division("C").countStudent(20).teacher(new Teacher())
				.schoolId(12345).build();

		student = Student.builder().absences(3).birthday(LocalDate.now()).cellPhone("535435").document("342343232")
				.division("B").grade("2°").email("oscar@gmail.com").gender(GenderType.MALE).name("oscar")
				.level(EducationLevels.SECUNDARIO).parent(new Parent()).course(course).build();

		ParentApi parentApi = new ParentApi();
		parentApi.setAdressApi(new AdressApi());

		studentApi = StudentApi.builder().adressApi(new AdressApi()).birthday(LocalDate.now()).cellPhone("4534543")
				.division("C").grade("3°").document("435345").email("oscar@gmail.com")
				.gender(GenderType.MALE.toString()).name("oscar").level(EducationLevels.SECUNDARIO.toString())
				.parentApi(parentApi).schoolId(1234).build();
		Optional<Student> optional = Optional.of(student);
		Optional<Course> optionalCourse=Optional.of(course);
		students = new ArrayList<>();
		students.add(student);

		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();

		Mockito.when(Mapper.mapperToCourse(courseApi)).thenReturn(course);
		Mockito.when(Mapper.mapperToAdress(Mockito.any())).thenReturn(new Adress());
		Mockito.when(Mapper.mapperToParent(Mockito.any())).thenReturn(new Parent());
		Mockito.when(Mapper.mapperToStudent(studentApi)).thenReturn(student);

		Mockito.when(studentRepository.save(student)).thenReturn(student);
		Mockito.when(studentRepository.findById(id)).thenReturn(optional);
		Mockito.when(studentRepository.findBySchoolId(idSchool)).thenReturn(students);
		Mockito.when(studentRepository.findByCourseId(idCourse)).thenReturn(students);

		Mockito.when(courseService.findById(idCourse.toString())).thenReturn(optionalCourse);

		// inyecta en el servicio el objeto repository
		ReflectionTestUtils.setField(studentServiceImpl, "studentRepository", studentRepository);
		// inyecta en el servicio Student el servicio Course
		ReflectionTestUtils.setField(studentServiceImpl, "courseService", courseService);
	}

	@Test
	public void whenCreateIsOk() {
		Optional<Student> optional = Optional.empty();
		Mockito.when(studentRepository.findByDocumentAndGender(Mockito.anyString(), Mockito.any()))
				.thenReturn(optional);

		boolean hasError = false;
		try {
			studentServiceImpl.create(studentApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateOk() {
		boolean hasError = false;
		try {
			studentApi.setId(id.toString());
			studentServiceImpl.update(studentApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateIsError() {

		id = UUID.randomUUID();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentApi.setId(id.toString());
			studentServiceImpl.update(studentApi);
		}).withMessage(StudentMessage.GET_ERROR.getDescription());

	}

	@Test
	public void whenCreateIsError() {

		Optional<Student> optional = Optional.of(student);

		StudentApi studentApi = StudentApi.builder().adressApi(new AdressApi()).birthday(LocalDate.now())
				.cellPhone("4534543").document("55555").division("F").grade("3°").email("oscar@gmail.com")
				.gender(GenderType.MALE.toString()).name("oscar").parentApi(new ParentApi()).schoolId(1234).build();
		Mockito.when(studentRepository.findByDocumentAndGender(Mockito.anyString(), Mockito.any()))
				.thenReturn(optional);

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			studentServiceImpl.create(studentApi);
		}).withMessage(StudentMessage.EXIST.getDescription());

	}

	@Test
	public void whenAddCourseOk() {
		boolean hasError = false;
		try {
			studentServiceImpl.addCourse(id.toString(), idCourse.toString());
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenAddCourseError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.addCourse(id.toString(), idCourse.toString());
		}).withMessage(StudentMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenDeleteCourseOk() {

		boolean hasError = false;
		try {
			studentServiceImpl.deleteCourse(id.toString(), idCourse.toString());
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenDeletedCourseError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.deleteCourse(id.toString(), idCourse.toString());
		}).withMessage(StudentMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetByIdIsOK() throws TransactionException {
		boolean hasError = false;
		try {
			studentServiceImpl.getById(id.toString(), false);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdIsError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			studentServiceImpl.getById(id.toString(), false);
		}).withMessage(StudentMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetBySchoolIsOK() throws TransactionException {
		boolean hasError = false;
		try {
			studentServiceImpl.getBySchool(idSchool.toString(), false);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetBySchoolIsError() throws TransactionException {

		boolean hasError = false;

		try {
			studentServiceImpl.getBySchool(null, false);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isTrue();
	}

	@Test
	public void whenGetByIdCourseIsOK() throws TransactionException {

		Course course = new Course();
		course.setId(idCourse);
		Mockito.when(studentRepository.findByCourseId(Mockito.any())).thenReturn(students);

		boolean hasError = false;
		try {
			studentServiceImpl.getByCourse(idCourse.toString(), false);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdCourseIsError() throws TransactionException {
		boolean hasError = false;

		try {
			studentServiceImpl.getByCourse(null, false);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isTrue();
	}

}