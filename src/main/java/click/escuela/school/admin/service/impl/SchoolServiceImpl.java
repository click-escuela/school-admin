package click.escuela.school.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.SchoolApi;
import click.escuela.school.admin.dto.SchoolDTO;
import click.escuela.school.admin.enumerator.SchoolMessage;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.model.Student;
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
	
	public Optional<School> findById(String id) throws SchoolException {
		return Optional.of(schoolRepository.findById(Long.valueOf(id)))
				.orElseThrow(() -> new SchoolException(SchoolMessage.GET_ERROR));
	}
	
	public void update(Student student, String schoolId) throws SchoolException {
		School school = getById(schoolId);
		student.setSchool(school);
		school.getStudents().add(student);
		schoolRepository.save(school);
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
		List<Student> students = new ArrayList<>();
		students = getById(schoolId).getStudents();
		return students;
	}
}
