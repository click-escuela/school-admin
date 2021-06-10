package click.escuela.school.admin.service;

import click.escuela.school.admin.exception.CourseException;

public interface CourseServiceGeneric <T, S>{

	public void create(T entity) throws CourseException;
	
	public S getById(String id) throws CourseException;

}
