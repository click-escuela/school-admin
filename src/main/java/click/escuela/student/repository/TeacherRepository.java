package click.escuela.student.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.student.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

}
