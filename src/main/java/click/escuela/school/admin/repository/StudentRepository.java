package click.escuela.school.admin.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.model.Student;

public interface StudentRepository extends JpaRepository<Student, UUID> {

	public List<Student> findBySchoolId(UUID school);

	public Optional<Student> findByDocumentAndGender(String document, GenderType gender);

	public List<Student> findByCourseId(UUID id);
	
	public List<Student> findByCourseIdIn(List<UUID> ids);
	
	public List<Student> findByParentId(UUID parentId);

	public Optional<Student> findByIdAndSchoolId(UUID id, UUID schoolId);

}
