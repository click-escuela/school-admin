package click.escuela.school.admin.service;

import click.escuela.school.admin.exception.CourseException;

public interface CourseServiceGeneric<T> {

	public void create(String id, T entity) throws CourseException;

}
