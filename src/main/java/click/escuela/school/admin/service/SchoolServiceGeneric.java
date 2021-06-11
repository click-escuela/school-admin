package click.escuela.school.admin.service;

import java.util.List;

import click.escuela.school.admin.exception.TransactionException;

public interface SchoolServiceGeneric<T, S> {

	public List<S> getAll();

	public void create(T entity) throws TransactionException;

}
