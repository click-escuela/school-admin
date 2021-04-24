package click.escuela.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {

	@JsonProperty(value = "gradeId")
	private String gradeId;

	@JsonProperty(value = "year")
	private Integer year;

	@JsonProperty(value = "division")
	private String division;

	@JsonProperty(value = "countStudent")
	private Integer countStudent;
	

}
