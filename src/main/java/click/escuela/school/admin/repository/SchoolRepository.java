package click.escuela.school.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.school.admin.model.School;

public interface SchoolRepository extends JpaRepository<School, Long> {

}
