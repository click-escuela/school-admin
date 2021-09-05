package click.escuela.school.admin.service;

import java.util.List;

import click.escuela.school.admin.exception.BillException;

public interface BillServiceGeneric <T, S>{

	public void create(String idSchool, String id, T entity) throws BillException;
	
	public S getById(String id,String schoolId) throws BillException;
	
	public List<S> findAll()  throws BillException;

}
