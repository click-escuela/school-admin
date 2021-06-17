package click.escuela.school.admin.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.school.admin.model.Course;

public interface CourseRepository extends JpaRepository<Course, UUID>{

}
