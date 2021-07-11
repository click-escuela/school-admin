package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Iterator;
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
import click.escuela.school.admin.dto.CourseStudentsDTO;
import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Student;
import click.escuela.school.admin.repository.CourseRepository;
import click.escuela.school.admin.service.impl.CourseServiceImpl;
import click.escuela.school.admin.service.impl.StudentServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class CourseServiceTest {

	@Mock
	private CourseRepository courseRepository;
	
	@Mock
	private StudentServiceImpl studentService;

	private CourseServiceImpl courseServiceImpl = new CourseServiceImpl();
	private CourseApi courseApi;
	private UUID id;
	private List<String> ids = new ArrayList<>();
	private List<Course> courses= new ArrayList<>();
	private List<CourseStudentsDTO> coursesDTO= new ArrayList<>();
	List<StudentDTO> students= new ArrayList<>();

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);
		PowerMockito.mock(Iterator.class);
		
		Course course = Course.builder().id(id).year(6).division("C").countStudent(20).schoolId(12345)
				.build();
		Student student = new Student();
		student.setCourse(course);
		students.add(Mapper.mapperToStudentDTO(student));
		CourseStudentsDTO courseDTO = CourseStudentsDTO.builder().students(students).build();
		coursesDTO.add(courseDTO);
		courseApi = CourseApi.builder().year(8).division("B").countStudent(35).schoolId(45678).build();
		Optional<Course> optional = Optional.of(course);
		id = UUID.randomUUID();
		courses.add(course);
		ids.add(id.toString());

		Mockito.when(Mapper.mapperToCourse(courseApi)).thenReturn(course);
		Mockito.when(courseRepository.save(course)).thenReturn(course);
		Mockito.when(courseRepository.findById(id)).thenReturn(optional);
		Mockito.when(courseRepository.findAll()).thenReturn(courses);
		Mockito.when(studentService.getByCourse(id.toString(), false)).thenReturn(students);
		Mockito.when(Mapper.mapperToCoursesStudentDTO(courses)).thenReturn(coursesDTO);

		ReflectionTestUtils.setField(courseServiceImpl, "courseRepository", courseRepository);
		ReflectionTestUtils.setField(courseServiceImpl, "studentService", studentService);
	}

	@Test
	public void whenCreateIsOk() throws CourseException   {
		courseServiceImpl.create(courseApi);
		verify(courseRepository).save(Mapper.mapperToCourse(courseApi));
	}

	@Test
	public void whenCreateIsError() {
		CourseApi courseApi = CourseApi.builder().year(10).division("C").countStudent(40).schoolId(85252).build();
		Mockito.when(courseRepository.save(null)).thenThrow(IllegalArgumentException.class);
		assertThatExceptionOfType(CourseException.class).isThrownBy(() -> {
			courseServiceImpl.create(courseApi);
		}).withMessage(CourseMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void whenFindByIdIsOk() throws CourseException   {
		courseServiceImpl.findById(id.toString());
		verify(courseRepository).findById(id);
	}
	
	@Test
	public void whenFindByIdIsError() throws CourseException   {
		assertThatExceptionOfType(CourseException.class).isThrownBy(() -> {
			courseServiceImpl.findById(UUID.randomUUID().toString());
		}).withMessage(CourseMessage.GET_ERROR.getDescription());
	}
	
	@Test
	public void whenGetCoursesStudentsIsOk() throws CourseException   {
		courseServiceImpl.getCourseStudents(ids);
		verify(studentService).getByCourse(null, false);
	}
	
	@Test
	public void whenGetCoursesIsError() throws CourseException   {
		assertThatExceptionOfType(CourseException.class).isThrownBy(() -> {
			courseServiceImpl.getCourses(courses, null);
		}).withMessage(CourseMessage.GET_ERROR.getDescription());
	}
	
	@Test
	public void whenFindAllIsOk() {
		courseServiceImpl.findAll();
		verify(courseRepository).findAll();
	}
}