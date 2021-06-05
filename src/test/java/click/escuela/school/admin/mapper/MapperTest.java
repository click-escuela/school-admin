package click.escuela.school.admin.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import click.escuela.school.admin.api.SchoolApi;
import click.escuela.school.admin.dto.SchoolDTO;
import click.escuela.school.admin.model.School;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class MapperTest {

	@Mock
	ModelMapper modelMapper;

	private SchoolDTO schoolDTO;
	private School school;
	private SchoolApi schoolApi;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);
		modelMapper = new ModelMapper();
		school = School.builder().name("Colegio Nacional").build();
		schoolDTO = new SchoolDTO();
		schoolApi = SchoolApi.builder().name("Colegio Nacional").build();

	}

	@Test
	public void convertSchoolToSchoolDTO() {
		schoolDTO = modelMapper.map(school, SchoolDTO.class);
		assertEquals(school.getName(), schoolDTO.getName());
	}

	@Test
	public void convertSchoolApiToSchool() {
		school = new School();
		school = modelMapper.map(schoolApi, School.class);
		assertEquals(school.getName(), schoolApi.getName());
	}
}
