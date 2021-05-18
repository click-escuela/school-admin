package click.escuela.school.admin.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.school.admin.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

}
