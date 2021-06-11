package click.escuela.school.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.dto.CourseDTO;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.repository.CourseRepository;
import click.escuela.school.admin.service.CourseServiceGeneric;

@Service
public class CourseServiceImpl implements CourseServiceGeneric<CourseApi> {
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TeacherServiceImpl teacherService;

	@Override
	public void create(CourseApi courserApi) throws TransactionException {
		try {
			Course course = Mapper.mapperToCourse(courserApi);
			courseRepository.save(course);
		} catch (Exception e) {
			throw new TransactionException(CourseMessage.CREATE_ERROR.getCode(),
					CourseMessage.CREATE_ERROR.getDescription());
		}
	}

	public List<CourseDTO> findAll() {
		List<Course> listCourses = courseRepository.findAll();
		return Mapper.mapperToCoursesDTO(listCourses);
	}

	public Optional<Course> findById(String idCourse) throws TransactionException {
		return Optional.of(courseRepository.findById(UUID.fromString(idCourse))
				.orElseThrow(() -> new TransactionException(CourseMessage.GET_ERROR.getCode(),
						CourseMessage.GET_ERROR.getDescription())));
	}

	public void addTeacher(String teacherId, String courseId) throws TransactionException {
		Course course = findById(courseId).orElseThrow(() -> new TransactionException(CourseMessage.GET_ERROR.getCode(),
				CourseMessage.GET_ERROR.getDescription()));
		course.setTeacher(teacherService.addCourseId(teacherId, courseId));
		courseRepository.save(course);
	}

	public void deleteTeacher(String teacherId, String courseId) throws TransactionException {
		Course course = findById(courseId).orElseThrow(() -> new TransactionException(CourseMessage.GET_ERROR.getCode(),
				CourseMessage.GET_ERROR.getDescription()));
		teacherService.deleteCourseId(teacherId);
		course.setTeacher(null);
		courseRepository.save(course);
	}

}
