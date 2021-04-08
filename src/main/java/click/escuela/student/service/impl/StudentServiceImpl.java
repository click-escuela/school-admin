package click.escuela.student.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.student.api.StudentApi;
import click.escuela.student.dto.StudentDTO;
import click.escuela.student.enumerator.StudentEnum;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.mapper.Mapper;
import click.escuela.student.model.Student;
import click.escuela.student.repository.StudentRepository;
import click.escuela.student.service.ServiceGeneric;

@Service
public class StudentServiceImpl implements ServiceGeneric<StudentApi, StudentDTO> {

	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public void create(StudentApi studentApi) throws TransactionException {

		try {
			Student student = Mapper.mapperToStudent(studentApi);
			studentRepository.save(student);
		} catch (Exception e) {
			throw new TransactionException(StudentEnum.CREATE_ERROR.getCode(),
					StudentEnum.CREATE_ERROR.getDescription());
		}

	}

	@Override
	public StudentDTO get(String id) throws TransactionException {
		
		Optional<Student> optional = studentRepository.findById(UUID.fromString(id));
		StudentDTO studentDto = null;
		if(optional.isPresent()) {
			studentDto = Mapper.mapperToStudentDTO(optional.get());
		}
		return studentDto;
	}

	@Override
	public void update(StudentApi studentApi) throws TransactionException {
		studentRepository.saveAndFlush(Mapper.mapperToStudent(studentApi));
	}

	@Override
	public void delete(String id) throws TransactionException {
		studentRepository.deleteById(UUID.fromString(id));
	}
	
	public List<StudentDTO> getBySchool(String school) {
		return Mapper.mapperToStudentsDTO(studentRepository.findBySchool(school));
	}
	

}
