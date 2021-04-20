package click.escuela.student.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.student.api.CourseApi;
import click.escuela.student.dto.CourseDTO;
import click.escuela.student.enumerator.CourseEnum;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.mapper.Mapper;
import click.escuela.student.model.Course;
import click.escuela.student.repository.CourseRepository;
import click.escuela.student.service.ServiceGeneric;

@Service
public class CourseServiceImpl implements ServiceGeneric<CourseApi, CourseDTO>{
	@Autowired
	private CourseRepository courseRepository;
	
	@Override
	public void create(CourseApi courserApi) throws TransactionException {
		try {
			
			Course course = Mapper.mapperToCourse(courserApi);
			courseRepository.save(course);
		} catch (Exception e) {
			throw new TransactionException(CourseEnum.CREATE_ERROR.getCode(),
					CourseEnum.CREATE_ERROR.getDescription());
		}
	}

	@Override
	public CourseDTO get(String id) throws TransactionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(CourseApi entity) throws TransactionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) throws TransactionException {
		// TODO Auto-generated method stub
		
	}
	
	public List<CourseDTO> findAll(){
		List<Course> listCourses=courseRepository.findAll();
		return Mapper.mapperToCoursesDTO(listCourses);
	}

}
