package click.escuela.school.admin.service;

import java.util.List;

import click.escuela.school.admin.exception.TransactionException;

public interface BillServiceGeneric <T, S>{

	public void create(String idSchool, String id, T entity) throws TransactionException;
	
	public S getById(String id) throws TransactionException;
	
	public List<S> findAll()  throws TransactionException;

}
