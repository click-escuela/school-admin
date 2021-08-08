package click.escuela.school.admin.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseStudentsDTO extends CourseDTO {

	@JsonProperty(value = "students")
	private List<StudentDTO> students;

}
