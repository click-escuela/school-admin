package click.escuela.student.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.student.api.StudentApi;
import click.escuela.student.dto.StudentDTO;
import click.escuela.student.enumerator.StudentMessage;
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

	@Autowired
	private CourseServiceImpl courseService;

	@Override
	public void create(StudentApi studentApi) throws TransactionException {

		exists(studentApi);
		try {

			Student student = Mapper.mapperToStudent(studentApi);
			studentRepository.save(student);
		} catch (Exception e) {
			throw new TransactionException(StudentMessage.CREATE_ERROR.getCode(),
					StudentMessage.CREATE_ERROR.getDescription());
		}
	}

	@Override
	public StudentDTO getById(String id) throws TransactionException {
		Student student = findById(id);
		return Mapper.mapperToStudentDTO(student);
	}

	public Student findById(String id) throws TransactionException {

		Optional<Student> optional = studentRepository.findById(UUID.fromString(id));

		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new TransactionException(StudentMessage.GET_ERROR.getCode(), StudentMessage.GET_ERROR.getDescription());
		}

	}

	@Override
	public void update(StudentApi studentApi) throws TransactionException {

		Student student = findById(studentApi.getId());

		student.setName(studentApi.getName());
		student.setSurname(studentApi.getSurname());
		student.setDocument(studentApi.getDocument());
		student.setGender(Mapper.mapperToEnum(studentApi.getGender()));
		student.setSchoolId(studentApi.getSchoolId());
		student.setGrade(studentApi.getGrade());
		student.setDivision(studentApi.getDivision());
		student.setBirthday(studentApi.getBirthday());
		student.setAdress(Mapper.mapperToAdress(studentApi.getAdressApi()));
		student.setCellPhone(studentApi.getCellPhone());
		student.setEmail(studentApi.getEmail());
		student.setParent(Mapper.mapperToParent(studentApi.getParentApi()));

		studentRepository.save(student);
	}

	public void addCourse(String idStudent, String idCourse) throws TransactionException {
		Student student = findById(idStudent);
		student.setCourse(courseService.findById(idCourse));
		studentRepository.save(student);

	}

	@Override
	public void delete(String id) throws TransactionException {
		studentRepository.deleteById(UUID.fromString(id));
	}

	public List<StudentDTO> getBySchool(String school) {
		List<Student> student = studentRepository.findBySchoolId((Integer.valueOf(school)));

		return Mapper.mapperToStudentsDTO(student);
	}

	public List<StudentDTO> findAll() {
		return Mapper.mapperToStudentsDTO(studentRepository.findAll());
	}

	public void exists(StudentApi student) throws TransactionException {
		
		Optional<Student> studentExist = studentRepository.findByDocumentAndGender(student.getDocument(),
				Mapper.mapperToEnum(student.getGender()));
		if (studentExist.isPresent()) {

			throw new TransactionException(StudentMessage.EXIST.getCode(), StudentMessage.EXIST.getDescription());
		}
	}

	public void deleteCourse(String idStudent, String idCourse) throws TransactionException {

		Student student = findById(idStudent);

		if (student.getCourse().getId().toString().equals(idCourse)) {
			student.setCourse(null);
			studentRepository.save(student);
		} else {
			throw new TransactionException(StudentMessage.UPDATE_ERROR.getCode(),
					StudentMessage.UPDATE_ERROR.getDescription());
		}
	}

	public List<StudentDTO> getByCourse(String courseId) throws TransactionException {
		Course course=new Course();
		course.setId(UUID.fromString(courseId));
		List<Student> student = studentRepository.findByCourse(course);
		if(!student.isEmpty()) {
			return Mapper.mapperToStudentsDTO(student);
		}
		else {
			throw new TransactionException(StudentMessage.GET_ERROR.getCode(), StudentMessage.GET_ERROR.getDescription());
		}

	}

}
