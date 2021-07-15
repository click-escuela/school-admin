package click.escuela.school.admin.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

	public List<Teacher> findBySchoolId(Integer school);

	public Optional<Teacher> findByDocumentAndGender(String document, GenderType gender);
	
	public List<Teacher> findByCoursesId(UUID id);
	
}
