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
import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.api.ParentApi;
import click.escuela.school.admin.api.StudentApi;
import click.escuela.school.admin.dto.CourseStudentsDTO;
import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.enumerator.EducationLevels;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.ParentMessage;
import click.escuela.school.admin.enumerator.StudentMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.ParentException;
import click.escuela.school.admin.exception.SchoolException;

import click.escuela.school.admin.exception.StudentException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Parent;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.model.Student;
import click.escuela.school.admin.repository.StudentRepository;
import click.escuela.school.admin.service.impl.CourseServiceImpl;
import click.escuela.school.admin.service.impl.ParentServiceImpl;
import click.escuela.school.admin.service.impl.SchoolServiceImpl;

import click.escuela.school.admin.service.impl.StudentServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class StudentServiceTest {

	@Mock
	private StudentRepository studentRepository;

	@Mock
	private CourseServiceImpl courseService;
	
	@Mock
	private ParentServiceImpl parentService;
	
	@Mock
	private SchoolServiceImpl schoolService;


	private StudentServiceImpl studentServiceImpl = new StudentServiceImpl();
	private StudentApi studentApi;
	private CourseApi courseApi;
	private UUID id;
	private UUID idCourse;
	private UUID idSchool;

	private UUID parentId;
	private List<Student> students;
	private Student student;
	private Course course;
	private List<UUID> uuids;
	private String name = "Patrick";
	private String surname = "Brown";
	private String document = "256936985";
	private GenderType gender = GenderType.MALE;
	private Parent parent = new Parent();
	private Optional<Parent> optionalParent;

	@Before
	public void setUp() throws CourseException, ParentException, SchoolException {


		PowerMockito.mockStatic(Mapper.class);

		idSchool = UUID.randomUUID();
		id = UUID.randomUUID();
		idCourse = UUID.randomUUID();
		parentId = UUID.randomUUID();		
		parent.setId(parentId);
		parent.setName(name);
		parent.setName(surname);
		parent.setDocument(document);
		parent.setGender(gender);
		School school = new School();
		school.setId(idSchool);
		course = Course.builder().id(idCourse).year(6).division("C").countStudent(20).school(school).build();

		student = Student.builder().id(id).absences(3).birthday(LocalDate.now()).cellPhone("535435")
				.document("342343232").school(school).division("B").grade("2°").email("oscar@gmail.com").gender(GenderType.MALE)
				.name("oscar").level(EducationLevels.SECUNDARIO).parent(parent).course(course).build();
		ParentApi parentApi = new ParentApi();
		parentApi.setName(name);
		parentApi.setSurname(surname);
		parentApi.setDocument(document);
		parentApi.setGender(gender.toString());
		parentApi.setAdressApi(new AdressApi());
		studentApi = StudentApi.builder().adressApi(new AdressApi()).birthday(LocalDate.now()).cellPhone("4534543")
				.division("C").grade("3°").document("435345").email("oscar@gmail.com")
				.gender(GenderType.MALE.toString()).name("oscar").level(EducationLevels.SECUNDARIO.toString())
				.parentApi(parentApi).build();
		Optional<Student> optional = Optional.of(student);
		Optional<Course> optionalCourse = Optional.of(course);
		optionalParent = Optional.of(parent);

		students = new ArrayList<>();
		students.add(student);
		courseApi = CourseApi.builder().year(8).division("B").build();
		uuids =  new ArrayList<>();
		uuids.add(idCourse);

		Mockito.when(Mapper.mapperToCourse(courseApi)).thenReturn(course);
		Mockito.when(Mapper.mapperToAdress(Mockito.any())).thenReturn(new Adress());
		Mockito.when(Mapper.mapperToParent(Mockito.any())).thenReturn(new Parent());
		Mockito.when(Mapper.mapperToStudent(studentApi)).thenReturn(student);
		Mockito.when(Mapper.mapperToStudent(studentApi, student)).thenReturn(student);
		Mockito.when(studentRepository.save(student)).thenReturn(student);
		Mockito.when(studentRepository.findById(id)).thenReturn(optional);
		Mockito.when(studentRepository.findByIdAndSchoolId(id,idSchool)).thenReturn(optional);
		Mockito.when(studentRepository.findBySchoolId(idSchool)).thenReturn(students);
		Mockito.when(studentRepository.findByCourseId(idCourse)).thenReturn(students);
		Mockito.when(studentRepository.findByCourseIdIn(uuids)).thenReturn(students);
		Mockito.when(courseService.findById(idCourse.toString())).thenReturn(optionalCourse);
		Mockito.when(parentService.findById(parentId.toString())).thenReturn(optionalParent);
		Mockito.when(studentRepository.findByParentId(parentId)).thenReturn(students);
		Mockito.when(schoolService.getById(idSchool.toString())).thenReturn(school);
		Mockito.when(schoolService.getStudentsById(idSchool.toString())).thenReturn(students);
		Mockito.when(studentRepository.findAll()).thenReturn(students);


		ReflectionTestUtils.setField(studentServiceImpl, "studentRepository", studentRepository);
		ReflectionTestUtils.setField(studentServiceImpl, "courseService", courseService);
		ReflectionTestUtils.setField(studentServiceImpl, "parentService", parentService);
		ReflectionTestUtils.setField(studentServiceImpl, "schoolService", schoolService);

	}

	@Test
	public void whenCreateIsOk() throws StudentException, SchoolException {
		Mockito.when(parentService.findByOptions(parent.getName(), parent.getSurname(), parent.getDocument(), parent.getGender())).thenReturn(optionalParent);
		studentServiceImpl.create(idSchool.toString(), studentApi);
		verify(studentRepository).save(student);
	}
	
	@Test
	public void whenCreateIsOkTwo() throws StudentException, SchoolException {
		studentServiceImpl.create(idSchool.toString(), studentApi);
		verify(studentRepository).save(student);
	}
	

	@Test
	public void whenUpdateOk() throws StudentException {
		studentApi.setId(id.toString());
		studentServiceImpl.update(idSchool.toString(), studentApi);
		verify(studentRepository).save(student);
	}

	@Test
	public void whenUpdateIsError() {
		id = UUID.randomUUID();
		assertThatExceptionOfType(StudentException.class).isThrownBy(() -> {
			studentApi.setId(id.toString());
			studentServiceImpl.update(idSchool.toString(), studentApi);
		}).withMessage(StudentMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenCreateIsError() {
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
			studentServiceImpl.update("6", studentApi);
		}).withMessage(null);
	}

	@Test
	public void whenAddCourseOk() throws StudentException, CourseException {
		studentServiceImpl.addCourse(id.toString(), idCourse.toString());
		verify(studentRepository).save(student);
	}

	@Test
	public void whenAddCourseError() {
		id = UUID.randomUUID();
		assertThatExceptionOfType(StudentException.class).isThrownBy(() -> {
			studentServiceImpl.addCourse(id.toString(), idCourse.toString());
		}).withMessage(StudentMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenDeleteCourseOk() throws StudentException, CourseException {
		studentServiceImpl.deleteCourse(id.toString(), idCourse.toString());
		verify(studentRepository).save(student);
	}

	@Test
	public void whenDeletedCourseError() {
		id = UUID.randomUUID();
		assertThatExceptionOfType(StudentException.class).isThrownBy(() -> {
			studentServiceImpl.deleteCourse(id.toString(), idCourse.toString());
		}).withMessage(StudentMessage.GET_ERROR.getDescription());
	}
	@Test
	public void whenGetByIdIsOK() throws TransactionException {
		studentServiceImpl.getById(idSchool.toString(),id.toString(), false);
		verify(studentRepository).findByIdAndSchoolId(id, idSchool);
	}
	
	@Test
	public void whenGetAllIsOK() {
		studentServiceImpl.getAll();
		verify(studentRepository).findAll();
	}

	@Test
	public void whenGetByIdIsError() {
		id = UUID.randomUUID();
		assertThatExceptionOfType(StudentException.class).isThrownBy(() -> {
			studentServiceImpl.getById(idSchool.toString(),id.toString(), false);

		}).withMessage(StudentMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetBySchoolIsOK() throws TransactionException {
		studentServiceImpl.getBySchool(idSchool.toString(), false);
		verify(schoolService).getStudentsById(idSchool.toString());
	}

	@Test
	public void whenGetBySchoolIsError() throws TransactionException {
		idSchool = UUID.randomUUID();
		Mockito.when(schoolService.getStudentsById(idSchool.toString())).thenReturn(new ArrayList<>());
		List<StudentDTO> listEmpty = studentServiceImpl.getBySchool(idSchool.toString(), false);
		assertThat(listEmpty).isEmpty();
	}

	@Test
	public void whenGetByIdCourseIsOK() throws TransactionException {
		studentServiceImpl.getByCourse(idCourse.toString(), false);
		verify(studentRepository).findByCourseId(idCourse);
	}

	@Test
	public void whenGetByIdCourseIsError() throws TransactionException {
		idCourse = UUID.randomUUID();
		List<StudentDTO> listEmpty = studentServiceImpl.getByCourse(idCourse.toString(), false);
		assertThat(listEmpty).isEmpty();
	}
	
	@Test
	public void whenGetStudentsByCourseIsOK() throws TransactionException {
		List<CourseStudentsDTO> listCoursesStudents = new ArrayList<>();
		CourseStudentsDTO course = new CourseStudentsDTO();
		course.setId(idCourse.toString());
		course.setStudents(Mapper.mapperToStudentsDTO(students));
		listCoursesStudents.add(course);
	
		studentServiceImpl.getCourseStudents(listCoursesStudents);
		verify(studentRepository).findByCourseIdIn(uuids);
	}
	
	@Test
	public void whenGetStudentsByCourseIsError() throws TransactionException {
		List<CourseStudentsDTO> listEmpty = studentServiceImpl.getCourseStudents(new ArrayList<>());
		assertThat(listEmpty).isEmpty();
	}
	
	@Test
	public void whenGetStudentsByParentIdIsOk() throws ParentException {
		studentServiceImpl.getStudentsByParentId(parentId.toString(), false);
		verify(studentRepository).findByParentId(parentId);
	}

	@Test
	public void whenGetStudentsByParentIdWithBillIsOk() throws ParentException {
		studentServiceImpl.getStudentsByParentId(parentId.toString(), true);
		verify(studentRepository).findByParentId(parentId);
	}
	
	@Test
	public void whenGetByParentIdIsError() {
		parentId = UUID.randomUUID();
		assertThatExceptionOfType(ParentException.class).isThrownBy(() -> {
			studentServiceImpl.getStudentsByParentId(parentId.toString(), true);
		}).withMessage(ParentMessage.GET_ERROR.getDescription());
	}

}