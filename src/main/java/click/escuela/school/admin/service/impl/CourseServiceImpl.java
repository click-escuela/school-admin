package click.escuela.school.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.dto.CourseDTO;
import click.escuela.school.admin.dto.CourseStudentsDTO;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.repository.CourseRepository;
import click.escuela.school.admin.service.CourseServiceGeneric;

@Service
public class CourseServiceImpl implements CourseServiceGeneric<CourseApi> {
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private StudentServiceImpl studentService;

	@Override
	public void create(CourseApi courserApi) throws CourseException {
		try {
			Course course = Mapper.mapperToCourse(courserApi);
			courseRepository.save(course);
		} catch (Exception e) {
			throw new CourseException(CourseMessage.CREATE_ERROR);
		}
	}

	public List<CourseDTO> findAll() {
		List<Course> listCourses = courseRepository.findAll();
		return Mapper.mapperToCoursesDTO(listCourses);
	}

	public Optional<Course> findById(String idCourse) throws CourseException {
		return Optional.of(courseRepository.findById(UUID.fromString(idCourse))
				.orElseThrow(() -> new CourseException(CourseMessage.GET_ERROR)));
	}

	public List<Course> getCourses(List<Course> courses, List<String> idCourses) throws CourseException {
		try {
			idCourses.forEach(p -> courses.add(courseRepository.findById(UUID.fromString(p)).get()));
		} catch (Exception e) {
			throw new CourseException(CourseMessage.GET_ERROR);
		}
		return courses;
	}

	public List<CourseStudentsDTO> getCourseStudents(List<String> listUUIDs) throws CourseException {
		List<CourseStudentsDTO> courses = Mapper.mapperToCoursesStudentDTO(getCourses(new ArrayList<>(),listUUIDs));
		courses.forEach(p -> p.setStudents(studentService.getByCourse(p.getId(), false)));
		return courses;
	}

}
