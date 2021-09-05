package click.escuela.school.admin.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.enumerator.EducationLevels;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Parent;
import click.escuela.school.admin.model.Student;

@RunWith(PowerMockRunner.class)
public class MapperStudetnTest {

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private Mapper mapper;

	private Student student = new Student();
	private StudentDTO studentDTO = new StudentDTO();
	private UUID id = UUID.randomUUID();

	@Before
	public void setUp() {
		Course course = new Course();
		course.setId(UUID.randomUUID());
		student = Student.builder().id(id).absences(3).birthday(LocalDate.now()).cellPhone("535435")
				.document("342343232").division("B").grade("2°").email("oscar@gmail.com").gender(GenderType.MALE)
				.name("oscar").level(EducationLevels.SECUNDARIO).parent(new Parent()).course(course)
				.bills(new ArrayList<>()).build();

		when(modelMapper.map(student, StudentDTO.class)).thenReturn(studentDTO);
		ReflectionTestUtils.setField(mapper, "modelMapper", modelMapper);
	}

	@Test
	public void whenMapperStudentTests() {
		StudentDTO studentDTO = Mapper.mapperToStudentFullDTO(student);
		assertThat(studentDTO).isNotNull();

		student.setCourse(null);
		StudentDTO studentDTO2 = Mapper.mapperToStudentFullDTO(student);
		assertThat(studentDTO2).isNotNull();

	}

}
