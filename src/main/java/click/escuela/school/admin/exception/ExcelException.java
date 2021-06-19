package click.escuela.school.admin.exception;

import click.escuela.school.admin.enumerator.ExcelMessage;

public class ExcelException extends TransactionException{

	private static final long serialVersionUID = 1L;

	public ExcelException(ExcelMessage excelMessage) {
		super(excelMessage.getCode(), excelMessage.getDescription());
	}
}
