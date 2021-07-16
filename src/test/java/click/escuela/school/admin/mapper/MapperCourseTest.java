package click.escuela.school.admin.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.dto.CourseDTO;
import click.escuela.school.admin.dto.CourseStudentsDTO;
import click.escuela.school.admin.model.Course;

@RunWith(PowerMockRunner.class)
public class MapperCourseTest {

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private Mapper mapper;
	
	private CourseApi courseApi;
	private List<Course> courses = new ArrayList<>();
	private List<CourseStudentsDTO> courseStudentsDTOList= new ArrayList<>();
	private Course course = new Course();
	private CourseStudentsDTO courseStudentsDTO = new CourseStudentsDTO();
	private CourseDTO courseDTO = new CourseDTO();

	@Before
	public void setUp() {
		courses.add(course);
		courseStudentsDTOList.add(courseStudentsDTO);
		
		when(modelMapper.map(course, CourseStudentsDTO.class)).thenReturn(courseStudentsDTO);
		when(modelMapper.map(course, CourseDTO.class)).thenReturn(courseDTO);
		when(modelMapper.map(courseApi, Course.class)).thenReturn(course);
		ReflectionTestUtils.setField(mapper, "modelMapper", modelMapper);
	}
	
	@Test
	public void whenMapperCourseTests() {
		CourseStudentsDTO courseStudentsDTOTest = Mapper.mapperToCourseStudentsDTO(course);
		assertThat(courseStudentsDTOTest).isNotNull();
		
		CourseDTO courseDTOTest = Mapper.mapperToCourseDTO(course);
		assertThat(courseDTOTest).isNotNull();
		
		List<CourseStudentsDTO> courseStudentsDTOListTest = new ArrayList<>();
		courses.stream().forEach(p -> courseStudentsDTOListTest.add(Mapper.mapperToCourseStudentsDTO(p)));
		assertThat(courseStudentsDTOListTest).isNotEmpty();
		
		Course courseTest = Mapper.mapperToCourse(courseApi);
		assertThat(courseTest).isNotNull();
	}
	
	
}
