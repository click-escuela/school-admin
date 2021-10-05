package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
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
import click.escuela.school.admin.dto.CourseStudentsShortDTO;
import click.escuela.school.admin.dto.StudentShortDTO;
import click.escuela.school.admin.dto.TeacherCourseStudentsDTO;
import click.escuela.school.admin.dto.TeacherDTO;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.TeacherMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.exception.TeacherException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.repository.TeacherRepository;
import click.escuela.school.admin.service.impl.CourseServiceImpl;
import click.escuela.school.admin.service.impl.SchoolServiceImpl;
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
	
	@Mock
	private SchoolServiceImpl schoolService;

	private TeacherServiceImpl teacherServiceImpl = new TeacherServiceImpl();
	private TeacherApi teacherApi;
	private Teacher teacher;
	private UUID id;
	private UUID courseId;
	private UUID schoolId;
	private List<Teacher> teachers;
	private List<String> listStringIds = new ArrayList<>();
	private List<Course> courses = new ArrayList<>();
	private TeacherCourseStudentsDTO teacherDTO = new TeacherCourseStudentsDTO();
	private List<CourseStudentsShortDTO> coursesStudents = new ArrayList<>();
	private UUID studentId;

	@Before
	public void setUp() throws SchoolException {
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		studentId = UUID.randomUUID();
		schoolId = UUID.randomUUID();
		courseId = UUID.randomUUID();
		listStringIds.add(courseId.toString());
		Course courseEntity = new Course();
		courseEntity.setCountStudent(20);
		courseEntity.setDivision("B");
		courseEntity.setId(courseId);
		courseEntity.setYear(10);
		courses.add(courseEntity);
		School school = new School();
		school.setId(schoolId);
		teacher = Teacher.builder().id(id).name("Mariana").surname("Lopez").birthday(LocalDate.now())
				.documentType(DocumentType.DNI).document("25897863").gender(GenderType.FEMALE).cellPhone("1589632485")
				.email("mariAna@gmail.com").courses(courses).school(school).adress(new Adress()).build();
    
		teacherApi = TeacherApi.builder().name("Mariana").surname("Lopez").birthday(LocalDate.now())
				.documentType("DNI").document("98623512").gender(GenderType.FEMALE.toString()).cellPhone("1589632485")
				.email("mariAna@gmail.com").adressApi(new AdressApi()).build();
    
		Optional<Teacher> optional = Optional.of(teacher);
		teacherDTO.setCourses(new ArrayList<>());
		teachers = new ArrayList<>();
		teachers.add(teacher);
		
		CourseStudentsShortDTO course = new CourseStudentsShortDTO();
		course.setCountStudent(20);
		course.setDivision("B");
		course.setId(courseId.toString());
		course.setYear(10);
		StudentShortDTO student = new StudentShortDTO();
		student.setId(studentId.toString());
		student.setName("Anotnio");
		student.setSurname("Liendro");
		List<StudentShortDTO> students = new ArrayList<>();
		students.add(student);
		course.setStudents(students);
		coursesStudents.add(course);
		
		Mockito.when(teacherRepository.findById(id)).thenReturn(optional);
		Mockito.when(Mapper.mapperToTeacher(teacherApi)).thenReturn(teacher);
		Mockito.when(Mapper.mapperToTeacher(teacherApi,teacher)).thenReturn(teacher);

		Mockito.when(Mapper.mapperToAdress(Mockito.any())).thenReturn(new Adress());
		Mockito.when(teacherRepository.save(teacher)).thenReturn(teacher);
		Mockito.when(teacherRepository.findById(id)).thenReturn(optional);
		Mockito.when(teacherRepository.findByIdAndSchoolId(id,schoolId)).thenReturn(optional);
		Mockito.when(teacherRepository.findByCoursesId(courseId)).thenReturn(teachers);
		Mockito.when(teacherRepository.findAll()).thenReturn(teachers);
		Mockito.when(Mapper.mapperToEnum(teacherApi.getGender())).thenReturn(teacher.getGender());
		Mockito.when(teacherRepository.findByDocumentAndGender(teacherApi.getDocument(),
				Mapper.mapperToEnum(teacherApi.getGender()))).thenReturn(optional);
		Mockito.when(Mapper.mapperToTeacherCourseStudentsDTO(teacher)).thenReturn(teacherDTO);
		Mockito.when(studentService.setStudentToCourseStudentsShort(Mockito.any())).thenReturn(coursesStudents);
		Mockito.when(schoolService.getById(schoolId.toString())).thenReturn(school);
		Mockito.when(schoolService.getTeachersById(schoolId.toString())).thenReturn(teachers);
		
		ReflectionTestUtils.setField(teacherServiceImpl, "teacherRepository", teacherRepository);
		ReflectionTestUtils.setField(teacherServiceImpl, "courseService", courseService);
		ReflectionTestUtils.setField(teacherServiceImpl, "studentService", studentService);
		ReflectionTestUtils.setField(teacherServiceImpl, "schoolService", schoolService);

	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		teacherApi.setDocument("11111111");
		teacherServiceImpl.create(schoolId.toString(), teacherApi);
		verify(teacherRepository).save(Mapper.mapperToTeacher(teacherApi));
	}

	@Test
	public void whenCreateIsError() {
		Mockito.when(teacherRepository.save(null)).thenThrow(IllegalArgumentException.class);
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.create(schoolId.toString(), new TeacherApi());

		}).withMessage(TeacherMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenUpdateIsOk() throws TransactionException {
		teacherApi.setId(id.toString());
		teacherServiceImpl.update(schoolId.toString(), teacherApi);

		verify(teacherRepository).save(Mapper.mapperToTeacher(teacherApi));
	}

	@Test
	public void whenUpdateIsError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherApi.setId(id.toString());
			teacherServiceImpl.update(schoolId.toString(), teacherApi);
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
	public void whenGetBySchoolIsOk() throws SchoolException {
		teacherServiceImpl.getBySchoolId(schoolId.toString());
		verify(schoolService).getTeachersById(schoolId.toString());
	}

	@Test
	public void whenGetBySchoolIsEmpty() throws SchoolException {
		schoolId = UUID.randomUUID();
		List<TeacherDTO> listEmpty = teacherServiceImpl.getBySchoolId(schoolId.toString());
		assertThat(listEmpty).isEmpty();
	}

	@Test
	public void whenGetByCourseIsOk() throws CourseException, TeacherException {
		teacherServiceImpl.getByCourseId(courseId.toString());
		if (courseService.findById(courseId.toString()).isPresent()) {
			verify(teacherRepository).findByCoursesId(courseId);
		}
	}

	@Test
	public void whenGetByCourseIsEmpty() throws CourseException {
		courseId = UUID.randomUUID();		
		List<TeacherDTO> listEmpty = teacherServiceImpl.getByCourseId(courseId.toString());
		assertThat(listEmpty).isEmpty();;
	}
	
	@Test
	public void whenGetByCourseIsError() throws CourseException {
		doThrow(new CourseException(CourseMessage.GET_ERROR)).when(courseService).findById(courseId.toString());
		assertThatExceptionOfType(CourseException.class).isThrownBy(() -> {
			teacherServiceImpl.getByCourseId(courseId.toString());
		}).withMessage(CourseMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenFindAllIsOk() {
		teacherServiceImpl.findAll();
		verify(teacherRepository).findAll();
	}

	@Test
	public void whenAddCoursesIsOk() throws TeacherException, CourseException {
		teacherServiceImpl.addCourses(id.toString(), listStringIds);
		verify(teacherRepository).save(Mapper.mapperToTeacher(teacherApi));
	}
	@Test
	public void whenAddCoursesIsError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.addCourses(id.toString(), listStringIds);
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenDeleteCoursesIsOk() throws TeacherException, CourseException {
		teacherServiceImpl.deleteCourses(id.toString(), listStringIds);
		verify(teacherRepository).save(Mapper.mapperToTeacher(teacherApi));
	}
	
	@Test
	public void whenDeleteCoursesIsError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.deleteCourses(id.toString(), listStringIds);
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
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

		Optional<Teacher> optional = Optional.of(teacher);
		Mockito.when(Mapper.mapperToEnum(teacherApi.getGender())).thenReturn(teacher.getGender());
		Mockito.when(teacherRepository.findByDocumentAndGender(teacherApi.getDocument(),
				Mapper.mapperToEnum(teacherApi.getGender()))).thenReturn(optional);

		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.exists(teacherApi);
		}).withMessage(TeacherMessage.EXIST.getDescription());
	}
	
	@Test
	public void whengGetCoursesByTeacherIdIsOk() throws TeacherException {
		List<CourseStudentsShortDTO> courseStudents = teacherServiceImpl.getCoursesByTeacherId(id.toString());
		verify(teacherRepository).findById(id);
		assertThat(courseStudents).isNotEmpty();
	}
	
	@Test
	public void whenGetCoursesByTeacherIdIsError() {
		assertThatExceptionOfType(TeacherException.class).isThrownBy(() -> {
			teacherServiceImpl.getCoursesByTeacherId(UUID.randomUUID().toString());
		}).withMessage(TeacherMessage.GET_ERROR.getDescription());
	}
	
}
