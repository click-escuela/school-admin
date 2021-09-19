package click.escuela.school.admin.exception;

import click.escuela.school.admin.enumerator.ParentMessage;

public class ParentException extends TransactionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ParentException(ParentMessage parentMessage) {
		super(parentMessage.getCode() ,parentMessage.getDescription());
	}

}
