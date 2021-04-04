package click.escuela.student.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import click.escuela.student.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID>{

}
