package click.escuela.school.admin.exception;

import click.escuela.school.admin.enumerator.BillEnum;

public class BillException extends TransactionException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6832544749211260468L;
	
	public BillException(BillEnum billEnum) {
		super(billEnum.getCode(), billEnum.getDescription());
	}
}
