package click.escuela.school.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.SchoolApi;
import click.escuela.school.admin.dto.SchoolDTO;
import click.escuela.school.admin.enumerator.SchoolMessage;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.model.Student;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.repository.SchoolRepository;
import click.escuela.school.admin.service.SchoolServiceGeneric;

@Service
public class SchoolServiceImpl implements SchoolServiceGeneric<SchoolApi,SchoolDTO>{

	@Autowired
	private SchoolRepository schoolRepository;

	@Override
	public List<SchoolDTO> getAll() {
		return Mapper.mapperToSchoolsDTO(schoolRepository.findAll());
	}
	
	@Override
	public void create(SchoolApi schoolApi) throws SchoolException {
		try {
			School school=Mapper.mapperToSchool(schoolApi);
			schoolRepository.save(school);
		} catch (Exception e) {
			throw new SchoolException(SchoolMessage.CREATE_ERROR);
		}
	}
	
	private Optional<School> findById(String id) throws SchoolException {
		return Optional.of(schoolRepository.findById(UUID.fromString(id)))
				.orElseThrow(() -> new SchoolException(SchoolMessage.GET_ERROR));
	}

	public School getById(String id) throws SchoolException {
		Optional<School> schoolOptional = findById(id);
		if (schoolOptional.isPresent()) {
			return schoolOptional.get();
		} else {
			throw new SchoolException(SchoolMessage.GET_ERROR);
		}
	}
	
	public List<Student> getStudentsById(String schoolId) throws SchoolException{
		return  getById(schoolId).getStudents();
	}
	
	public List<Teacher> getTeachersById(String schoolId) throws SchoolException{
		return  getById(schoolId).getTeachers();
	}
}
