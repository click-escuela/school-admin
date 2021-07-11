package click.escuela.school.admin.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherCourseStudentsDTO extends TeacherDTO {
	
	@JsonProperty(value = "courses")
	private List<CourseStudentsDTO> courses;
}