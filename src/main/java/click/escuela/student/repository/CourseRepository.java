package click.escuela.student.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.student.model.Course;

public interface CourseRepository extends JpaRepository<Course, UUID>{

}
