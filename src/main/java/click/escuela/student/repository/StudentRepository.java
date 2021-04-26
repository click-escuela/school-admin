package click.escuela.student.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.student.enumerator.GenderType;
import click.escuela.student.model.Course;

import click.escuela.student.model.Student;

public interface StudentRepository extends JpaRepository<Student, UUID> {

	public List<Student> findBySchoolId(Integer school);

	public Optional<Student> findByDocumentAndGender(String document, GenderType gender);

	public List<Student> findByCourse(Course course);
}
