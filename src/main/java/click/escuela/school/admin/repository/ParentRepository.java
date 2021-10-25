package click.escuela.school.admin.repository;

import java.util.Optional;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.model.Parent;

public interface ParentRepository extends JpaRepository<Parent, UUID> {
	
	public Optional<Parent> findByNameAndSurnameAndDocumentAndGender(String name, String surname, String document, GenderType gender);

}
