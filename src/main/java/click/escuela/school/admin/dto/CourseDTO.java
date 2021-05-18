package click.escuela.school.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "year")
	private Integer year;

	@JsonProperty(value = "division")
	private String division;

	@JsonProperty(value = "countStudent")
	private Integer countStudent;

}
