package click.escuela.student.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.student.model.Student;

public interface StudentRepository extends JpaRepository <Student, UUID> {

	public List<Student> findBySchool(String school);
}
