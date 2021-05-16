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
import click.escuela.student.model.Bill;
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
			throw new TransactionException(StudentEnum.CREATE_ERROR.getCode(),
					StudentEnum.CREATE_ERROR.getDescription());
		}
	}

	@Override
	public StudentDTO getById(String id, Boolean fullDetail) throws TransactionException {

		Student student = findById(id);

		if (Boolean.TRUE.equals(fullDetail)) {
			return Mapper.mapperToStudentFullDTO(student);
		} else {
			return Mapper.mapperToStudentDTO(student);
		}
	}

	public Student findById(String id) throws TransactionException {

		Optional<Student> optional = studentRepository.findById(UUID.fromString(id));

		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new TransactionException(StudentEnum.GET_ERROR.getCode(), StudentEnum.GET_ERROR.getDescription());
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

	public List<StudentDTO> getBySchool(String school, Boolean fullDetail) {

		if (Boolean.TRUE.equals(fullDetail)) {
			return Mapper.mapperToStudentsFullDTO(studentRepository.findBySchoolId((Integer.valueOf(school))));
		} else {
			return Mapper.mapperToStudentsDTO(studentRepository.findBySchoolId((Integer.valueOf(school))));
		}
	}

	public List<StudentDTO> getByCourse(String courseId, Boolean fullDetail) {

		if (Boolean.TRUE.equals(fullDetail)) {
			return Mapper.mapperToStudentsFullDTO(studentRepository.findByCourseId(UUID.fromString(courseId)));
		} else {
			return Mapper.mapperToStudentsDTO(studentRepository.findByCourseId(UUID.fromString(courseId)));
		}
	}

	public List<StudentDTO> findAll() {

		return Mapper.mapperToStudentsDTO(studentRepository.findAll());
	}

	public void exists(StudentApi student) throws TransactionException {

		Optional<Student> studentExist = studentRepository.findByDocumentAndGender(student.getDocument(),
				Mapper.mapperToEnum(student.getGender()));
		if (studentExist.isPresent()) {
			throw new TransactionException(StudentEnum.EXIST.getCode(), StudentEnum.EXIST.getDescription());
		}
	}

	public void deleteCourse(String idStudent, String idCourse) throws TransactionException {

		Student student = findById(idStudent);

		if (student.getCourse().getId().toString().equals(idCourse)) {
			student.setCourse(null);
			studentRepository.save(student);
		} else {
			throw new TransactionException(StudentEnum.UPDATE_ERROR.getCode(),
					StudentEnum.UPDATE_ERROR.getDescription());
		}
	}

	public void addBill(Bill bill, UUID studentId) throws TransactionException {

		Student student = findById(studentId.toString());
		List<Bill> bills = student.getBills();
		bills.add(bill);
		student.setBills(bills);

		studentRepository.save(student);
	}
}