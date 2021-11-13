package click.escuela.school.admin.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.school.admin.dto.TeacherDTO;
import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.model.Teacher;

@RunWith(PowerMockRunner.class)
public class MapperTeacherTest {

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private Mapper mapper;
	
	private Teacher teacher = new Teacher();
	private TeacherDTO teacherDTO = new TeacherDTO();
	private UUID id;

	@Before
	public void setUp() {
		id = UUID.randomUUID();
		List<Course> courses = new ArrayList<>();
		Course courseEntity = new Course();
		courseEntity.setId(UUID.randomUUID());
		courses.add(courseEntity);
		School school = new School();
		school.setId(UUID.randomUUID());
		teacher = Teacher.builder().id(id).name("Mariana").surname("Lopez").birthday(LocalDate.now())
				.documentType(DocumentType.DNI).document("25897863").gender(GenderType.FEMALE).cellPhone("1589632485")
				.email("mariAna@gmail.com").courses(courses).school(school).adress(new Adress()).build();
		
		when(modelMapper.map(teacher, TeacherDTO.class)).thenReturn(teacherDTO);
		ReflectionTestUtils.setField(mapper, "modelMapper", modelMapper);
	}
	
	@Test
	public void whenMapperTeacherTests() {
		TeacherDTO teacherDTO = Mapper.mapperToTeacherDTO(teacher);
		assertThat(teacherDTO).isNotNull();
		
		teacher.setSchool(null);
		TeacherDTO teacherDTO2 = Mapper.mapperToTeacherDTO(teacher);
		assertThat(teacherDTO2).isNotNull();
	}
	
	
}
