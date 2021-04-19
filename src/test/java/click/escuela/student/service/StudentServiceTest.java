package click.escuela.student.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.student.api.AdressApi;
import click.escuela.student.api.ParentApi;
import click.escuela.student.api.StudentApi;
import click.escuela.student.dto.StudentDTO;
import click.escuela.student.enumerator.GenderType;
import click.escuela.student.exception.ErrorStudent;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.mapper.Mapper;
import click.escuela.student.model.Adress;
import click.escuela.student.model.Parent;
import click.escuela.student.model.Student;
import click.escuela.student.repository.StudentRepository;
import click.escuela.student.service.impl.StudentServiceImpl;
import click.escuela.student.util.StudentApiBuilder;
import click.escuela.student.util.StudentBuilder;


@RunWith(PowerMockRunner.class)
@PrepareForTest({StudentServiceImpl.class,Mapper.class})
//@ExtendWith (MockitoExtension.class)
public class StudentServiceTest{
	
	@Mock
	private StudentRepository studentRepository;
	private StudentServiceImpl studentService = new StudentServiceImpl();
	//private StudentApi studentApi;
	
	
	@Before
	public void init() throws Exception {
		
		//MockitoAnnotations.initMocks(this);
		//PowerMockito.mockStatic(Mapper.class);
		List<Student> studentList = new ArrayList<>();
		Student student1=new Student();
		Student student2=new Student();
		
		student1 = StudentBuilder.getBuilder().setAdress(new Adress()).setBirthday(LocalDate.now())
				.setCellPhone("4534543").setDocument("555555").setDivision("F").setGrade("3°").setEmail("oscar@gmail.com").setGender(Mapper.mapperToEnum("MALE"))
				.setName("Oscar").setSurname("Umbert").setParent(new Parent()).setSchool("1234").getStudent();
		
		student2 = StudentBuilder.getBuilder().setAdress(new Adress()).setBirthday(LocalDate.now())
				.setCellPhone("4534548").setDocument("666666").setDivision("F").setGrade("3°").setEmail("tony@gmail.com").setGender(Mapper.mapperToEnum("MALE"))
				.setName("Tony").setSurname("Liendro").setParent(new Parent()).setSchool("5678").getStudent();
		
		studentList.add(student1);
		studentList.add(student2);
		
		//when(studentRepository.findAll()).thenReturn(studentList);
		/*Student student = StudentBuilder.getBuilder().setAbsences(3).setBirthday(LocalDate.now()).setCellPhone("535435").
				setDocument("342343232").setDivision("B").setGrade("2°").setEmail("oscar@gmail.com").setGender(GenderType.MALE).setName("oscar").setParent(new Parent()).getStudent();
		*/
		/*ParentApi parentApi = new ParentApi();
		parentApi.setAdressApi(new AdressApi());
		
		StudentApi studentApi = StudentApiBuilder.getBuilder().setAdressApi(new AdressApi()).setBirthday(LocalDate.now())
				.setCellPhone("4534543").setDivision("F").setGrade("3°").setDocument("435345").setEmail("oscar@gmail.com").setGender(GenderType.MALE.toString())
				.setName("Oscar").setParentApi(parentApi).setSchool("1234").getStudentApi();
		//Optional<Student> optional = Optional.of(student);

		Mockito.when(Mapper.mapperToAdress(Mockito.any())).thenReturn(new Adress());
		Mockito.when(Mapper.mapperToParent(Mockito.any())).thenReturn(new Parent());
 		Mockito.when(Mapper.mapperToStudent(studentApi)).thenReturn(student1);

 		Mockito.when(studentRepository.save(student1)).thenReturn(student1);		
 		
 		//Mockito.when(clientRepository.findById(1L)).thenReturn(optional);

		//Mockito.when(clientRepository.findAll()).thenReturn(Arrays.asList(new Client(1L,"23423432","oscar",LocalDateTime.of(2020, 10, 20, 12, 12))));

		//Mockito.doNothing().when(clientRepository).delete(Mockito.any());
		//inyecta en el servicio el objeto repository*/
		/*ReflectionTestUtils.setField(studentService, "studentRepository", studentRepository);*/
	}
	
	@Test
	public void CreateStudent() throws TransactionException {
		StudentApi studentTest=new StudentApi();	
		studentTest.setName("Walter");
		studentTest.setSchool("1234");
		
		studentService.create(studentTest);
		
		List<Student> studentTestList=studentRepository.findAll();
		
		Mockito.when(studentRepository.findBySchool("1234")).thenReturn(studentTestList);
		
		assertEquals("Walter",studentTestList.get(2).getName());
	
	}
	
	
	@Test
	public void studentNotFoundException() {
		when(studentRepository.findBySchool(Mockito.anyString())).thenReturn(null);
		assertThrows(NullPointerException.class,
				() -> {
					studentService.getBySchool("1234");
				});
	}
	
	/*@Test
    public void testGetAll() {
      //  when(studentRepository.findAll()).thenReturn(new ArrayList<>());
        assertThat(studentService.findAll()).isNotEmpty();
        //verify(studentRepository, times(1)).findAll();
    }*/
	
	@Test
	public void whenCreateIsOk() {
		StudentApi studentApi=new StudentApi();
		boolean hasError;
		try {
			studentService.create(studentApi);
			hasError=true;
			
		} catch (Exception e) {
			hasError=false;
		}
		assertThat(hasError).isFalse();
	}
	
	@Test(expected = Test.None.class)
	public void whenCreateIsError() {
		
		when(studentRepository.save(null)).thenThrow(IllegalArgumentException.class);
		
		assertThatExceptionOfType(TransactionException.class)
		  .isThrownBy(() -> {
				studentService.create(null);
		}).withMessage("No se pudo crear el estudiante correctamente");
	}
	
	
	
}