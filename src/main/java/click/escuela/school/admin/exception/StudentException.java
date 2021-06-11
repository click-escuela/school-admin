package click.escuela.school.admin.exception;

import click.escuela.school.admin.enumerator.StudentMessage;

public class StudentException extends TransactionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StudentException(StudentMessage studentMessage) {
		super(studentMessage.getCode() ,studentMessage.getDescription());
	}

}
