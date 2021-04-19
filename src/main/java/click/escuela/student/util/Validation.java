package click.escuela.student.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import click.escuela.student.api.StudentApi;
import click.escuela.student.mapper.Mapper;
import click.escuela.student.model.Student;
import click.escuela.student.repository.StudentRepository;

public class Validation {
	
	@Autowired
	private StudentRepository studentRepository;
	
	public boolean  StudentExists(StudentApi student) {
		Boolean exist = false;
		
			Optional<Student> studentExist=studentRepository.findAll().stream().filter(
					p->p.getDocument().equals(student.getDocument()) && 
					p.getGender().equals(Mapper.mapperToEnum(student.getGender()))).findAny();
			if(studentExist.isPresent()) {
				exist=true;
			}
			else {	
					exist=false;
				}
		
		return exist;
	}

}
