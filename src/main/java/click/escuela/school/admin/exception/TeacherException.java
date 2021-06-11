package click.escuela.school.admin.exception;

import click.escuela.school.admin.enumerator.TeacherMessage;

public class TeacherException extends TransactionException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2180809463240249594L;

	public TeacherException(TeacherMessage teacherMessage) {
		super(teacherMessage.getCode(), teacherMessage.getDescription());
		
	}
}
