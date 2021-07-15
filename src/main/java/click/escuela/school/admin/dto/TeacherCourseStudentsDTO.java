package click.escuela.school.admin.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.school.admin.model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherCourseStudentsDTO extends TeacherDTO {

	public TeacherCourseStudentsDTO(String id, Teacher teacher, AdressDTO adress, List<CourseStudentsDTO> courses) {
		super(id, teacher, adress);
		this.courses = courses;
	}

	@JsonProperty(value = "courses")
	private List<CourseStudentsDTO> courses;
}