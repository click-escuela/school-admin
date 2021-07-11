package click.escuela.school.admin.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CourseStudentsDTO extends CourseDTO{

	@JsonProperty(value = "students")
	private List<StudentDTO> students;

}
