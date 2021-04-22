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

	
	public void update(String id, StudentApi studentApi) throws TransactionException {
		UUID idReal = UUID.fromString(id);
		if (findId(idReal).equals(null)) {

			throw new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),
					StudentEnum.UPDATE_ERROR.getDescription());
		} else {
			Student student = null;
			Optional<Student> optional = studentRepository.findById(idReal);
			if (optional.isPresent()) {
				student = optional.get();
				student.setName(studentApi.getName());
				student.setSurname(studentApi.getSurname());
				student.setDocument(studentApi.getDocument());
				student.setGender(Mapper.mapperToEnum(studentApi.getGender()));
				student.setSchool(studentApi.getSchool());
				student.setGrade(studentApi.getGrade());
				student.setDivision(studentApi.getDivision());
				student.setBirthday(studentApi.getBirthday());
				student.setAdress(Mapper.mapperToAdress(studentApi.getAdressApi()));
				student.setCellPhone(studentApi.getCellPhone());
				student.setEmail(studentApi.getEmail());
				student.setParent(Mapper.mapperToParent(studentApi.getParentApi()));
				studentRepository.save(student);

			}

		}
	}

	@Override
	public void delete(String id) throws TransactionException {
		studentRepository.deleteById(UUID.fromString(id));
	}
	
	public List<StudentDTO> getBySchool(String school) {
		return Mapper.mapperToStudentsDTO(studentRepository.findBySchool(school));
	}
	
	
	public List<StudentDTO> findAll(){
		return Mapper.mapperToStudentsDTO(studentRepository.findAll());
	}

	public Student findId(UUID id) {
		Optional<Student> optional= studentRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		else {
			return null;
		}
	}
	
	@Override
	public void update(StudentApi studentApi) throws TransactionException {
		/*try {
			Student student = Mapper.mapperToStudent(studentApi);
			studentRepository.save(student);
		} catch (Exception e) {
			throw new TransactionException(StudentEnum.CREATE_ERROR.getCode(),
					StudentEnum.CREATE_ERROR.getDescription());
		}
		//studentRepository.saveAndFlush(Mapper.mapperToStudent(studentApi));*/
		
	}

}
