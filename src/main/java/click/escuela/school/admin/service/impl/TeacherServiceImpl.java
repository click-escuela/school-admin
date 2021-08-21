package click.escuela.school.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.TeacherApi;
import click.escuela.school.admin.dto.CourseStudentsShortDTO;
import click.escuela.school.admin.dto.TeacherCourseStudentsDTO;
import click.escuela.school.admin.dto.TeacherDTO;
import click.escuela.school.admin.enumerator.TeacherMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.TeacherException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.repository.TeacherRepository;

@Service
public class TeacherServiceImpl {

	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private CourseServiceImpl courseService;	

	@Autowired
	private StudentServiceImpl studentService;


	public void create(String schoolId, TeacherApi teacherApi) throws TeacherException {
		exists(teacherApi);
		try {
			Teacher teacher = Mapper.mapperToTeacher(teacherApi);
			teacher.setSchoolId(Integer.valueOf(schoolId));
			teacherRepository.save(teacher);
		} catch (Exception e) {
			throw new TeacherException(TeacherMessage.CREATE_ERROR);
		}
	}

	public void update(String schoolId, TeacherApi teacherApi) throws TeacherException {
		Teacher teacher = findById(teacherApi.getId())
				.orElseThrow(() -> new TeacherException(TeacherMessage.GET_ERROR));
		teacher.setSchoolId(Integer.valueOf(schoolId));
		teacherRepository.save(Mapper.mapperToTeacher(teacherApi, teacher));
	}

	public List<TeacherDTO> findAll() {
		List<Teacher> teachers = teacherRepository.findAll();
		return Mapper.mapperToTeachersDTO(teachers);
	}

	public Optional<Teacher> findById(String idTeacher) throws TeacherException {
		return Optional.of(teacherRepository.findById(UUID.fromString(idTeacher))
				.orElseThrow(() -> new TeacherException(TeacherMessage.GET_ERROR)));
	}

	public TeacherDTO getById(String id) throws TeacherException {
		Teacher teacher = findById(id).orElseThrow(() -> new TeacherException(TeacherMessage.GET_ERROR));
		return Mapper.mapperToTeacherDTO(teacher);
	}

	public List<TeacherDTO> getBySchoolId(String schoolId) {
		return Mapper.mapperToTeachersDTO(teacherRepository.findBySchoolId(Integer.valueOf(schoolId)));
	}

	public List<TeacherDTO> getByCourseId(String courseId) throws CourseException {
		List<TeacherDTO> teacherDTO = new ArrayList<>();
		if(courseService.findById(courseId).isPresent()) {
			teacherDTO = Mapper.mapperToTeachersDTO(teacherRepository.findByCoursesId(UUID.fromString(courseId)));
		}
		return teacherDTO;
	}

	public void exists(TeacherApi teacherApi) throws TeacherException {
		Optional<Teacher> teacherExist = teacherRepository.findByDocumentAndGender(teacherApi.getDocument(),
				Mapper.mapperToEnum(teacherApi.getGender()));
		if (teacherExist.isPresent()) {
			throw new TeacherException(TeacherMessage.EXIST);
		}
	}

	public void addCourses(String idTeacher, List<String> idCourses) throws TeacherException, CourseException {
		Teacher teacher = findById(idTeacher)
				.orElseThrow(() -> new TeacherException(TeacherMessage.GET_ERROR));
		teacher.setCourses(courseService.getCourses(teacher.getCourses(),idCourses));
		teacherRepository.save(teacher);
	}

	public void deleteCourses(String teacherId, List<String> idCourses) throws TeacherException, CourseException {
		Teacher teacher = findById(teacherId)
				.orElseThrow(() -> new TeacherException(TeacherMessage.GET_ERROR));
		teacher.getCourses().removeAll(courseService.getCourses(new ArrayList<>(),idCourses));
		teacherRepository.save(teacher);
	}

	public TeacherCourseStudentsDTO getCourseAndStudents(String teacherId) throws TeacherException{
		Teacher teacher = findById(teacherId)
				.orElseThrow(() -> new TeacherException(TeacherMessage.GET_ERROR));
		TeacherCourseStudentsDTO teacherDTO = Mapper.mapperToTeacherCourseStudentsDTO(teacher);
		teacherDTO.setCourses(studentService.getCourseStudents(teacherDTO.getCourses()));
		return teacherDTO;
	}

	public List<CourseStudentsShortDTO> getCoursesByTeacherId(String teacherId) throws TeacherException {
		Teacher teacher = findById(teacherId)
				.orElseThrow(() -> new TeacherException(TeacherMessage.GET_ERROR));
		List<CourseStudentsShortDTO> courseStudentsShortDTO = Mapper.mapperToCoursesStudentsShortDTO(teacher.getCourses());
		return studentService.setStudentToCourseStudentsShort(courseStudentsShortDTO);
	}

}
