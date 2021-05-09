package click.escuela.student.service;

import java.util.List;

import click.escuela.student.exception.TransactionException;

public interface BillServiceGeneric <T, S>{

	public void create(String id, T entity) throws TransactionException;
	
	public S getById(String id) throws TransactionException;
	
	public List<S> findAll()  throws TransactionException;

}
