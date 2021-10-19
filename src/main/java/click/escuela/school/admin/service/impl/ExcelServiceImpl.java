package click.escuela.school.admin.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.ExcelApi;
import click.escuela.school.admin.dto.ExcelDTO;
import click.escuela.school.admin.enumerator.ExcelMessage;
import click.escuela.school.admin.enumerator.FileStatus;
import click.escuela.school.admin.exception.ExcelException;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Excel;
import click.escuela.school.admin.model.School;
import click.escuela.school.admin.repository.ExcelRepository;

@Service
public class ExcelServiceImpl {

	@Autowired
	private ExcelRepository excelRepository;
	
	@Autowired
	private SchoolServiceImpl schoolService;

	public void save(String schoolId, ExcelApi excelApi) throws ExcelException, SchoolException {
		School school = schoolService.getById(schoolId);
		try {
			Excel excel = Mapper.mapperToExcel(excelApi);
			excel.setDate(LocalDate.now());
			excel.setStatus(FileStatus.PENDING);
			excel.setSchool(school);
			excelRepository.save(excel);
		} catch (Exception e) {
			throw new ExcelException(ExcelMessage.CREATE_ERROR);
		}
	}

	public List<ExcelDTO> getAll() {
		return Mapper.mapperToExcelsDTO(excelRepository.findAll());
	}
}
