package click.escuela.school.admin.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.school.admin.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

	List<Teacher> findBySchoolId(Integer school);
	
	public List<Teacher> findByCourseId(UUID id);
	

}
