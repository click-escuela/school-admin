package click.escuela.student.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.student.api.TeacherApi;
import click.escuela.student.dto.TeacherDTO;
import click.escuela.student.enumerator.TeacherMessage;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.mapper.Mapper;
import click.escuela.student.model.Teacher;
import click.escuela.student.repository.TeacherRepository;

@Service
public class TeacherServiceImpl {
	
	@Autowired
	private TeacherRepository teacherRepository;

	public void create(TeacherApi teacherApi) throws TransactionException {
		try {
			Teacher teacher = Mapper.mapperToTeacher(teacherApi);
			teacherRepository.save(teacher);
		} catch (Exception e) {
			throw new TransactionException(TeacherMessage.CREATE_ERROR.getCode(), TeacherMessage.CREATE_ERROR.getDescription());
		}
	}

	public List<TeacherDTO> findAll() {
		List<Teacher> teachers = teacherRepository.findAll();
		return Mapper.mapperToTeachersDTO(teachers);
	}

}
