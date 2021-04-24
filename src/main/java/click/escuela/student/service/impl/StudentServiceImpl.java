package click.escuela.student.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.student.api.StudentApi;
import click.escuela.student.api.StudentUpdateApi;
import click.escuela.student.dto.StudentDTO;
import click.escuela.student.enumerator.StudentEnum;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.mapper.Mapper;
import click.escuela.student.model.Course;
import click.escuela.student.model.Student;
import click.escuela.student.repository.StudentRepository;
import click.escuela.student.service.ServiceGeneric;

@Service
public class StudentServiceImpl implements ServiceGeneric<StudentApi, StudentDTO> {

	@Autowired
	private StudentRepository studentRepository;

	@Override
	public void create(StudentApi studentApi) throws TransactionException {

		if (!studentExists(studentApi)) {
			try {

				Student student = Mapper.mapperToStudent(studentApi);
				studentRepository.save(student);
			} catch (Exception e) {
				throw new TransactionException(StudentEnum.CREATE_ERROR.getCode(),
						StudentEnum.CREATE_ERROR.getDescription());
			}
		} else {
			throw new TransactionException(StudentEnum.STUDENT_EXIST.getCode(),
					StudentEnum.STUDENT_EXIST.getDescription());
		}
	}

	@Override
	public StudentDTO getById(String id) throws TransactionException {

		StudentDTO studentDto = Mapper.mapperToStudentDTO(findById(id));
		return studentDto;
	}
	
	public Student findById(String id) throws TransactionException {
		
		Optional<Student> optional = studentRepository.findById(UUID.fromString(id));

		if (optional.isPresent()) {
			return optional.get();
		}else {
			throw new TransactionException(StudentEnum.GET_ERROR.getCode(),
					StudentEnum.GET_ERROR.getDescription());
		}
		
	}

	@Override
	public void update(StudentApi studentApi) throws TransactionException {

		Student student = findById(studentApi.getId());

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

	public void addCourse(String idStudent, Course course) throws TransactionException {
		UUID idReal = UUID.fromString(idStudent);

		Student student = null;
		Optional<Student> optional = studentRepository.findById(idReal);

		if (optional.isPresent()) {
			student = optional.get();
			student.setCourse(course);
			studentRepository.save(student);
		} else {
			throw new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),
					StudentEnum.UPDATE_ERROR.getDescription());
		}
	}

	@Override
	public void delete(String id) throws TransactionException {
		studentRepository.deleteById(UUID.fromString(id));
	}

	public List<StudentDTO> getBySchool(String school) {
		List<Student> student = new ArrayList<>();
		student = studentRepository.findBySchool(school);

		return Mapper.mapperToStudentsDTO(student);
	}

	public List<StudentDTO> findAll() {
		return Mapper.mapperToStudentsDTO(studentRepository.findAll());
	}

	public Student findStudentById(String idStudent) throws TransactionException {
		UUID idReal = UUID.fromString(idStudent);
		Optional<Student> optional = studentRepository.findById(idReal);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),
					StudentEnum.UPDATE_ERROR.getDescription());
		}
	}

	public boolean studentExists(StudentApi student) {
		Boolean exist = false;

		Optional<Student> studentExist = studentRepository.findByDocumentAndGender(student.getDocument(),
				Mapper.mapperToEnum(student.getGender()));
		if (studentExist.isPresent()) {
			exist = true;
		} else {
			exist = false;
		}

		return exist;
	}

	public void deleteCourse(String idStudent,Course course) throws TransactionException {
		UUID idReal = UUID.fromString(idStudent);
		Student student = null;
		Optional<Student> optional = studentRepository.findById(idReal);
		if (optional.isPresent() && optional.get().getCourse().getGradeId()==course.getGradeId()) {
			student = optional.get();
			student.setCourse(null);
			studentRepository.save(student);
		} else {
			throw new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),
					StudentEnum.UPDATE_ERROR.getDescription());
		}

	}


	public List<StudentDTO> getByCourse(Course course) {
		List<Student> student = new ArrayList<>();
		student = studentRepository.findByCourse(course);

		return Mapper.mapperToStudentsDTO(student);
	}

}
