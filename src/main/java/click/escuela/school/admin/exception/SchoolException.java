package click.escuela.school.admin.exception;

import click.escuela.school.admin.enumerator.SchoolMessage;

public class SchoolException extends TransactionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SchoolException(SchoolMessage schoolMessage) {
		super(schoolMessage.getCode() ,schoolMessage.getDescription());
	}

}
