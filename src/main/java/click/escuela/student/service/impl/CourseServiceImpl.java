package click.escuela.student.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.student.api.CourseApi;
import click.escuela.student.dto.CourseDTO;
import click.escuela.student.enumerator.CourseMessage;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.mapper.Mapper;
import click.escuela.student.model.Course;
import click.escuela.student.repository.CourseRepository;
import click.escuela.student.service.ServiceGeneric;

@Service
public class CourseServiceImpl implements ServiceGeneric<CourseApi, CourseDTO> {
	@Autowired
	private CourseRepository courseRepository;

	@Override
	public void create(CourseApi courserApi) throws TransactionException {
		try {

			Course course = Mapper.mapperToCourse(courserApi);
			courseRepository.save(course);
		} catch (Exception e) {
			throw new TransactionException(CourseMessage.CREATE_ERROR.getCode(), CourseMessage.CREATE_ERROR.getDescription());
		}
	}

	@Override
	public CourseDTO getById(String id) throws TransactionException {
		return null;
	}

	@Override
	public void update(CourseApi entity) throws TransactionException {
		 // Metodo no implentado.
	}

	@Override
	public void delete(String id) throws TransactionException {
		// Metodo no implentado.
	}

	public List<CourseDTO> findAll() {
		List<Course> listCourses = courseRepository.findAll();
		return Mapper.mapperToCoursesDTO(listCourses);
	}

	public Course findById(String idCourse) throws TransactionException {
		Optional<Course> optional = courseRepository.findById(UUID.fromString(idCourse));
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new TransactionException(CourseMessage.GET_ERROR.getCode(), CourseMessage.GET_ERROR.getDescription());
		}
	}

}
