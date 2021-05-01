package click.escuela.student.service;

import click.escuela.student.exception.TransactionException;

public interface ServiceGeneric <T, S>{

	public void create(T entity) throws TransactionException;
	
	public S getById(String id) throws TransactionException;
	
	public void update(T entity) throws TransactionException;
	
	public void delete(String id) throws TransactionException;
}
