package click.escuela.school.admin.service;

import static org.mockito.Mockito.verify;

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

import click.escuela.school.admin.exception.ParentException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Parent;
import click.escuela.school.admin.repository.ParentRepository;
import click.escuela.school.admin.service.impl.ParentServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class ParentServiceTest {

	@Mock
	private ParentRepository parentRepository;
	
	private ParentServiceImpl parentServiceImpl = new ParentServiceImpl();
	private UUID id;

	@Before
	public void setUp() {

		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		Parent parent = new Parent();
		parent.setId(id);
	
		Optional<Parent> optional = Optional.of(parent);
		Mockito.when(parentRepository.findById(id)).thenReturn(optional);
		
		ReflectionTestUtils.setField(parentServiceImpl, "parentRepository", parentRepository);
	}

	
	@Test
	public void whenGetByIdIsOK() throws ParentException {
		parentServiceImpl.findById(id.toString());
		verify(parentRepository).findById(id);

	}
	
}