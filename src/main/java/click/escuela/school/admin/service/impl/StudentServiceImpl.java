package click.escuela.school.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import click.escuela.school.admin.api.StudentApi;
import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.enumerator.StudentMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.StudentException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Bill;
import click.escuela.school.admin.model.Course;
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
	public void create(String schoolId, StudentApi studentApi) throws StudentException {
		exists(studentApi);
		try {
			Student student = Mapper.mapperToStudent(studentApi);
			student.setSchoolId(Integer.valueOf(schoolId));
			studentRepository.save(student);
		} catch (Exception e) {
			throw new StudentException(StudentMessage.CREATE_ERROR);
		}
	}

	@Override
	public StudentDTO getById(String schoolId, String id, Boolean fullDetail) throws StudentException {
		Student student = findByIdAndSchoolId(schoolId, id)
				.orElseThrow(() -> new StudentException(StudentMessage.GET_ERROR));

		return Boolean.TRUE.equals(fullDetail) ? Mapper.mapperToStudentFullDTO(student)
				: Mapper.mapperToStudentDTO(student);
	}

	public Optional<Student> findById(String id) throws StudentException {
		return Optional.of(studentRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new StudentException(StudentMessage.GET_ERROR)));
	}

	public Optional<Student> findByIdAndSchoolId(String schoolId, String id) throws StudentException {
		return Optional.of(studentRepository.findByIdAndSchoolId(UUID.fromString(id), Integer.valueOf(schoolId))
				.orElseThrow(() -> new StudentException(StudentMessage.GET_ERROR)));
	}

	@Override
	public void update(String schoolId, StudentApi studentApi) throws StudentException {
		Student student = findById(studentApi.getId()).orElseThrow(() -> new StudentException(StudentMessage.GET_ERROR));
		student.setSchoolId(Integer.valueOf(schoolId));
		studentRepository.save(Mapper.mapperToStudent(studentApi, student));
	}

	public void addCourse(String idStudent, String idCourse) throws StudentException, CourseException {
		Student student = findById(idStudent).orElseThrow(() -> new StudentException(StudentMessage.GET_ERROR));
		Optional<Course> optional = courseService.findById(idCourse);
		if (optional.isPresent()) {
			student.setCourse(optional.get());
		}
		studentRepository.save(student);
	}

	@Override
	public void delete(String id) throws StudentException {

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

	public void exists(StudentApi student) throws StudentException {

		Optional<Student> studentExist = studentRepository.findByDocumentAndGender(student.getDocument(),
				Mapper.mapperToEnum(student.getGender()));
		if (studentExist.isPresent()) {
			throw new StudentException(StudentMessage.EXIST);
		}
	}

	public void deleteCourse(String idStudent, String idCourse) throws StudentException {
		Student student = findById(idStudent).filter(p -> p.getCourse().getId().toString().equals(idCourse))
				.orElseThrow(() -> new StudentException(StudentMessage.UPDATE_ERROR));

		student.setCourse(null);
		studentRepository.save(student);
	}

	public void addBill(Bill bill, UUID studentId) throws StudentException {
		findById(studentId.toString()).ifPresent(student -> {
			List<Bill> bills = student.getBills();
			bills.add(bill);
			student.setBills(bills);
			studentRepository.save(student);
		});
	}
}
