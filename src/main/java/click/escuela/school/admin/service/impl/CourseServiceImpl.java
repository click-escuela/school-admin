package click.escuela.school.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.dto.CourseDTO;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.TeacherException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.repository.ExcellRepository;
import click.escuela.school.admin.service.CourseServiceGeneric;

@Service
public class CourseServiceImpl implements CourseServiceGeneric<CourseApi> {
	@Autowired
	private ExcellRepository excellRepository;

	@Autowired
	private TeacherServiceImpl teacherService;

	@Override
	public void create(CourseApi courserApi) throws CourseException {
		try {
			Course course = Mapper.mapperToCourse(courserApi);
			excellRepository.save(course);
		} catch (Exception e) {
			throw new CourseException(CourseMessage.CREATE_ERROR);
		}
	}

	public List<CourseDTO> findAll() {
		List<Course> listCourses = excellRepository.findAll();
		return Mapper.mapperToCoursesDTO(listCourses);
	}

	public Optional<Course> findById(String idCourse) throws CourseException {
		return Optional.of(excellRepository.findById(UUID.fromString(idCourse))
				.orElseThrow(() -> new CourseException(CourseMessage.GET_ERROR)));
	}

	public void addTeacher(String teacherId, String courseId) throws CourseException, TeacherException {
		Course course = findById(courseId).orElseThrow(() -> new CourseException(CourseMessage.GET_ERROR));
		course.setTeacher(teacherService.addCourseId(teacherId, courseId));
		excellRepository.save(course);
	}

	public void deleteTeacher(String teacherId, String courseId) throws CourseException, TeacherException {
		Course course = findById(courseId).orElseThrow(() -> new CourseException(CourseMessage.GET_ERROR));
		teacherService.deleteCourseId(teacherId);
		course.setTeacher(null);
		excellRepository.save(course);

	}

	

}
