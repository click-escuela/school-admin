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
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Excel;
import click.escuela.school.admin.repository.ExcelRepository;

@Service
public class ExcelServiceImpl {

	@Autowired
	private ExcelRepository excelRepository;

	public void save(ExcelApi excelApi) throws ExcelException {
		try {
			Excel excel = Mapper.mapperToExcel(excelApi);
			excel.setDate(LocalDate.now());
			excel.setStatus(FileStatus.PENDING);
			excelRepository.save(excel);
		} catch (Exception e) {
			throw new ExcelException(ExcelMessage.CREATE_ERROR);
		}
	}

	public List<ExcelDTO> getAll() {
		return Mapper.mapperToExcelsDTO(excelRepository.findAll());
	}
}
