package click.escuela.student.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import click.escuela.student.api.AdressApi;
import click.escuela.student.api.ParentApi;
import click.escuela.student.api.StudentApi;
import click.escuela.student.dto.StudentDTO;
import click.escuela.student.model.Adress;
import click.escuela.student.model.Parent;
import click.escuela.student.model.Student;

@Component
public class Mapper {

	private static ModelMapper modelMapper = new ModelMapper();
	
	public static Student mapperToStudent(StudentApi studentApi) {
		Student student = modelMapper.map(studentApi, Student.class);
		student.setAdress(mapperToAdress(studentApi.getAdressApi()));
		student.setParent(mapperToParent(studentApi.getParentApi()));
		return student;
	}
	
	public static StudentDTO mapperToStudentDTO(Student student) {
		return modelMapper.map(student, StudentDTO.class);
	}
	
	public static Parent mapperToParent(ParentApi parentApi) {
		Parent parent = modelMapper.map(parentApi, Parent.class);
		parent.setAdress(mapperToAdress(parentApi.getAdressApi()));
		return parent;
	}
	
	public static ParentApi mapperToParentApi(Parent parent) {
		return modelMapper.map(parent, ParentApi.class);
	}
	
	public static AdressApi mapperToAdressApi(Adress adress) {
		return modelMapper.map(adress, AdressApi.class);
	}
	
	public static Adress mapperToAdress(AdressApi adressApi) {
		return modelMapper.map(adressApi, Adress.class);
	}
	
	public static List<StudentDTO> mapperToStudentsDTO(List<Student> students) {
		List<StudentDTO> studentDTO = new ArrayList<StudentDTO>();
		students.stream().forEach(p->studentDTO.add(mapperToStudentDTO(p)));
		return studentDTO;
	}
}
