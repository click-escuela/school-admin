package click.escuela.school.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.TeacherApi;
import click.escuela.school.admin.dto.TeacherDTO;
import click.escuela.school.admin.enumerator.TeacherMessage;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.repository.TeacherRepository;

@Service
public class TeacherServiceImpl {

	@Autowired
	private TeacherRepository teacherRepository;

	public void create(TeacherApi teacherApi) throws TransactionException {

		try {
			Teacher teacher = Mapper.mapperToTeacher(teacherApi);
			teacherRepository.save(teacher);
		} catch (Exception e) {
			throw new TransactionException(TeacherMessage.CREATE_ERROR.getCode(),
					TeacherMessage.CREATE_ERROR.getDescription());
		}
	}

	public void update(TeacherApi teacherApi) throws TransactionException {
		findById(teacherApi.getId())
				.ifPresent(teacher -> teacherRepository.save(Mapper.mapperToTeacher(teacherApi, teacher)));
	}

	public List<TeacherDTO> findAll() {
		List<Teacher> teachers = teacherRepository.findAll();
		return Mapper.mapperToTeachersDTO(teachers);
	}

	public Optional<Teacher> findById(String idTeacher) throws TransactionException {
		return Optional.of(teacherRepository.findById(UUID.fromString(idTeacher))
				.orElseThrow(() -> new TransactionException(TeacherMessage.GET_ERROR.getCode(),
						TeacherMessage.GET_ERROR.getDescription())));
	}

	public TeacherDTO getById(String id) throws TransactionException {
		Teacher teacher = findById(id).orElseThrow(() -> new TransactionException(TeacherMessage.GET_ERROR.getCode(),
				TeacherMessage.GET_ERROR.getDescription()));
		return Mapper.mapperToTeacherDTO(teacher);
	}

	public List<TeacherDTO> getBySchoolId(String schoolId) {
		return Mapper.mapperToTeachersDTO(teacherRepository.findBySchoolId(Integer.valueOf(schoolId)));
	}

	public List<TeacherDTO> getByCourseId(String courseId) {
		return Mapper.mapperToTeachersDTO(teacherRepository.findByCourseId(UUID.fromString(courseId)));
	}

}
