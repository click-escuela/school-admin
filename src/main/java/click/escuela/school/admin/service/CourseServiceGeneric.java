package click.escuela.school.admin.service;

import click.escuela.school.admin.exception.TransactionException;

public interface CourseServiceGeneric <T, S>{

	public void create(T entity) throws TransactionException;
	
	public S getById(String id) throws TransactionException;
	
	public void update(T entity) throws TransactionException;
	
	public void delete(String id) throws TransactionException;

}
