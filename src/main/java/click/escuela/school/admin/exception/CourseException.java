package click.escuela.school.admin.exception;

import click.escuela.school.admin.enumerator.CourseMessage;

public class CourseException extends TransactionException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CourseException(CourseMessage courseMessage) {
		super(courseMessage.getCode(), courseMessage.getDescription());
	}
}
