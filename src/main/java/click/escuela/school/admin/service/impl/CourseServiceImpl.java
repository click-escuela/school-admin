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
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.repository.CourseRepository;

@Service
public class CourseServiceImpl{
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private SchoolServiceImpl schoolService;
	
	public void create(String schoolId, CourseApi courserApi) throws CourseException, SchoolException {
		School school = schoolService.getById(schoolId);
		try {
			Course course = Mapper.mapperToCourse(courserApi);
			course.setSchool(school);
			course.setCountStudent(0);
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
			idCourses.forEach(p -> { 
				Course course = courseRepository.findById(UUID.fromString(p)).get();
				courses.add(course); 
			});
		} catch (Exception e) {
			throw new CourseException(CourseMessage.GET_ERROR);
		}
		return courses;
	}

}
