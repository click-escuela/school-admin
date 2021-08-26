package click.escuela.school.admin.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.SchoolApi;
import click.escuela.school.admin.dto.SchoolDTO;
import click.escuela.school.admin.enumerator.SchoolMessage;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.School;
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
	public void create(SchoolApi schoolApi) throws TransactionException {
		try {
			School school=Mapper.mapperToSchool(schoolApi);
			schoolRepository.save(school);
		} catch (Exception e) {
			throw new TransactionException(SchoolMessage.CREATE_ERROR.getCode(),
					SchoolMessage.CREATE_ERROR.getDescription());
		}
	}
	
	public School findById(String schoolId) throws TransactionException {
		return schoolRepository.findById(UUID.fromString(schoolId))
				.orElseThrow(() -> new TransactionException(SchoolMessage.GET_ERROR.getCode(),
						SchoolMessage.GET_ERROR.getDescription()));
	}
}
