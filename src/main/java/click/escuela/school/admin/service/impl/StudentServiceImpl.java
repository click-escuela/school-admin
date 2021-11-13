package click.escuela.school.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import click.escuela.school.admin.api.StudentApi;
import click.escuela.school.admin.dto.CourseStudentsDTO;
import click.escuela.school.admin.dto.CourseStudentsShortDTO;
import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.dto.StudentParentDTO;
import click.escuela.school.admin.enumerator.ParentMessage;
import click.escuela.school.admin.enumerator.StudentMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.ParentException;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.exception.StudentException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Bill;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Parent;
import click.escuela.school.admin.model.School;

import click.escuela.school.admin.model.Student;
import click.escuela.school.admin.repository.StudentRepository;

@Service
public class StudentServiceImpl {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseServiceImpl courseService;
	
	@Autowired
	private ParentServiceImpl parentService;
	
	@Autowired
	private SchoolServiceImpl schoolService;

	public StudentDTO create(String schoolId, StudentApi studentApi) throws StudentException, SchoolException {

		exists(studentApi);
		School school = schoolService.getById(schoolId);
		try {
			Student student = Mapper.mapperToStudent(studentApi);
			student.setSchool(school);
			student = studentRepository.save(checkParent(student));
			return  Mapper.mapperToStudentDTOToReturn(student);

		} catch (Exception e) {
			throw new StudentException(StudentMessage.CREATE_ERROR);
		}
	}
	
	
	public void update(String schoolId, StudentApi studentApi) throws StudentException {
		Student student = findByIdAndSchoolId(schoolId,studentApi.getId()).orElseThrow(() -> new StudentException(StudentMessage.GET_ERROR));
		studentRepository.save(Mapper.mapperToStudent(studentApi, student));
	}
	
	private Student checkParent(Student student) {
		Parent parent = student.getParent();
		Optional<Parent> optionalParent = parentService.findByOptions(parent.getName(), parent.getSurname(), parent.getDocument(), parent.getGender());
		if(optionalParent.isPresent()) {
			student.setParent(optionalParent.get());
		}
		return student;
	}

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

	public Optional<Student> findByIdAndSchoolId(String school, String id) throws StudentException {
		return Optional.of(studentRepository.findByIdAndSchoolId(UUID.fromString(id), UUID.fromString(school))
				.orElseThrow(() -> new StudentException(StudentMessage.GET_ERROR)));
	}

	public void addCourse(String idStudent, String idCourse) throws StudentException, CourseException {
		Student student = findById(idStudent).orElseThrow(() -> new StudentException(StudentMessage.GET_ERROR));
		Optional<Course> optional = courseService.findById(idCourse);
		if (optional.isPresent()) {
			optional.get().setCountStudent(studentRepository.findByCourseId(UUID.fromString(idCourse)).size()+1);
			student.setCourse(optional.get());
		}
		studentRepository.save(student);
	}
	
	public void deleteCourse(String idStudent, String idCourse) throws StudentException, CourseException {
		Student student = findById(idStudent).filter(p -> p.getCourse().getId().toString().equals(idCourse))
				.orElseThrow(() -> new StudentException(StudentMessage.UPDATE_ERROR));
		
		Optional<Course> optional = courseService.findById(idCourse);
		if (optional.isPresent()) {
			optional.get().setCountStudent(studentRepository.findByCourseId(UUID.fromString(idCourse)).size()-1);
			student.setCourse(null);
		}
		studentRepository.save(student);
	}
  
	public void delete(String id){

		studentRepository.deleteById(UUID.fromString(id));
	}

	public List<StudentDTO> getBySchool(String school, Boolean fullDetail) throws SchoolException {
		return Boolean.TRUE.equals(fullDetail)
				? Mapper.mapperToStudentsFullDTO(schoolService.getStudentsById(school))
				: Mapper.mapperToStudentsDTO(schoolService.getStudentsById(school));
	}

	public List<StudentDTO> getByCourse(String courseId, Boolean fullDetail) {
		return Boolean.TRUE.equals(fullDetail)
				? Mapper.mapperToStudentsFullDTO(studentRepository.findByCourseId(UUID.fromString(courseId)))
				: Mapper.mapperToStudentsDTO(studentRepository.findByCourseId(UUID.fromString(courseId)));
	}
	
	public List<StudentDTO> getByCourses(List<String> courseId, Boolean fullDetail) {		
		List<UUID> list = courseId.stream().map(UUID::fromString).collect(Collectors.toList());

		return Boolean.TRUE.equals(fullDetail)
				? Mapper.mapperToStudentsFullDTO(studentRepository.findByCourseIdIn(list))
				: Mapper.mapperToStudentsDTO(studentRepository.findByCourseIdIn(list));
	}
	
	public List<StudentDTO> findAll() {
		return Mapper.mapperToStudentsDTO(studentRepository.findAll());
	}
	
	public List<Student> getAll() {
		return studentRepository.findAll();
	}

	public void exists(StudentApi student) throws StudentException {

		Optional<Student> studentExist = studentRepository.findByDocumentAndGender(student.getDocument(),
				Mapper.mapperToEnum(student.getGender()));
		if (studentExist.isPresent()) {
			throw new StudentException(StudentMessage.EXIST);
		}
	}

	public void addBill(Bill bill, UUID studentId) throws StudentException {
		findById(studentId.toString()).ifPresent(student -> {
			List<Bill> bills = student.getBills();
			bills.add(bill);
			student.setBills(bills);
			studentRepository.save(student);
		});
	}

	public List<CourseStudentsDTO> getCourseStudents(List<CourseStudentsDTO> courses) {
		List<String> coursesIds = courses.stream().map(CourseStudentsDTO::getId).collect(Collectors.toList());
		List<StudentDTO> students = getByCourses(coursesIds,false);
		courses.forEach(p-> p.setStudents(getStudentsByCourse(students, p)));
		return courses;
	}

	private List<StudentDTO> getStudentsByCourse(List<StudentDTO> result, CourseStudentsDTO course) {
		return result.stream()
				.filter(r -> r.getCourseId().equals(course.getId()))
				.collect(Collectors.toList());
	}
	
	public List<StudentParentDTO> getStudentsByParentId(String parentId, Boolean fullDetail) throws ParentException {
		Optional<Parent> parent = parentService.findById(parentId);
		if(parent.isPresent()) {
			return Boolean.TRUE.equals(fullDetail)
					? Mapper.mapperToStudentsParentFullDTO(studentRepository.findByParentId(UUID.fromString(parentId)))
					: Mapper.mapperToStudentsParentDTO(studentRepository.findByParentId(UUID.fromString(parentId)));
		}
		else {
			throw new ParentException(ParentMessage.GET_ERROR);
		}
	}
	
	public List<CourseStudentsShortDTO> setStudentToCourseStudentsShort(List<CourseStudentsShortDTO> courses) {
		List<String> coursesIds = courses.stream().map(CourseStudentsShortDTO::getId).collect(Collectors.toList());
		List<StudentDTO> students = getByCourses(coursesIds,false);
		courses.forEach(p-> p.setStudents(Mapper.mapperToStudentShort(getStudentsShortByCourse(students, p))));
		return courses;
	}
	
	private List<StudentDTO> getStudentsShortByCourse(List<StudentDTO> result, CourseStudentsShortDTO course) {
		return result.stream()
				.filter(r -> r.getCourseId().equals(course.getId()))
				.collect(Collectors.toList());
	}
}
