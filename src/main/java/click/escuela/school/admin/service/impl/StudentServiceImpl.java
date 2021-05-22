package click.escuela.school.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import click.escuela.school.admin.api.StudentApi;
import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.enumerator.StudentMessage;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Bill;
import click.escuela.school.admin.model.Student;
import click.escuela.school.admin.repository.StudentRepository;
import click.escuela.school.admin.service.ServiceGeneric;

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
	public StudentDTO getById(String id, Boolean fullDetail) throws TransactionException {

		Student student = findById(id).orElseThrow(() -> new TransactionException(StudentMessage.GET_ERROR.getCode(),
				StudentMessage.GET_ERROR.getDescription()));

		return Boolean.TRUE.equals(fullDetail) ? Mapper.mapperToStudentFullDTO(student)
				: Mapper.mapperToStudentDTO(student);

	}

	public Optional<Student> findById(String id) throws TransactionException {

		return Optional.of(studentRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new TransactionException(StudentMessage.GET_ERROR.getCode(),
						StudentMessage.GET_ERROR.getDescription())));


	}

	@Override
	public void update(StudentApi studentApi) throws TransactionException {

		findById(studentApi.getId()).ifPresent(student -> 
			studentRepository.save(Mapper.mapperToStudent(studentApi))
		);

	
	}

	public void addCourse(String idStudent, String idCourse) throws TransactionException {

		Student student = findById(idStudent).orElseThrow(() -> new TransactionException(StudentMessage.GET_ERROR.getCode(),
				StudentMessage.GET_ERROR.getDescription()));
		student.setCourse(courseService.findById(idCourse));

		studentRepository.save(student);
	}

	@Override
	public void delete(String id) throws TransactionException {

		studentRepository.deleteById(UUID.fromString(id));
	}

	public List<StudentDTO> getBySchool(String school, Boolean fullDetail) {

		return Boolean.TRUE.equals(fullDetail)
				? Mapper.mapperToStudentsFullDTO(studentRepository.findBySchoolId((Integer.valueOf(school))))
				: Mapper.mapperToStudentsDTO(studentRepository.findBySchoolId((Integer.valueOf(school))));

	}

	public List<StudentDTO> getByCourse(String courseId, Boolean fullDetail) {

		return Boolean.TRUE.equals(fullDetail)
				? Mapper.mapperToStudentsFullDTO(studentRepository.findByCourseId(UUID.fromString(courseId)))
				: Mapper.mapperToStudentsDTO(studentRepository.findByCourseId(UUID.fromString(courseId)));
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

		Student student = findById(idStudent).filter(p -> p.getCourse().getId().toString().equals(idCourse))
				.orElseThrow(() -> new TransactionException(StudentMessage.UPDATE_ERROR.getCode(),
						StudentMessage.UPDATE_ERROR.getDescription()));

		student.setCourse(null);
		studentRepository.save(student);

	}
	
	public void addBill(Bill bill, UUID studentId) throws TransactionException {

		findById(studentId.toString()).ifPresent(student -> {

			List<Bill> bills = student.getBills();
			bills.add(bill);
			student.setBills(bills);

			studentRepository.save(student);
		});

	}
}
