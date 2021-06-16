package click.escuela.school.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.ExcelApi;
import click.escuela.school.admin.enumerator.ExcelMessage;
import click.escuela.school.admin.exception.ExcelException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.repository.ExcelRepository;

@Service
public class ExcelServiceImpl {

	@Autowired
	private ExcelRepository excelRepository;

	public void save(ExcelApi excelApi) throws ExcelException {
		try {
			excelRepository.save(Mapper.mapperToExcel(excelApi));
		} catch (Exception e) {
			throw new ExcelException(ExcelMessage.CREATE_ERROR);
		}
	}
}
