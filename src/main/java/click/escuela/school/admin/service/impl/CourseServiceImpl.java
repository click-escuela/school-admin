package click.escuela.school.admin.service.impl;

import java.util.List;
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
public class CourseServiceImpl implements CourseServiceGeneric<CourseApi, CourseDTO> {
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

		return courseRepository.findById(UUID.fromString(idCourse))
				.orElseThrow(() -> new TransactionException(CourseEnum.UPDATE_ERROR.getCode(),
						CourseEnum.UPDATE_ERROR.getDescription()));


	}

}
