package click.escuela.school.admin.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStudentsDTO extends CourseDTO {

	public CourseStudentsDTO(String id, Integer year, String division, Integer countStudent,
			List<StudentDTO> students) {
		super(id, year, division, countStudent);
		this.students = students;
	}

	@JsonProperty(value = "students")
	private List<StudentDTO> students;

}
