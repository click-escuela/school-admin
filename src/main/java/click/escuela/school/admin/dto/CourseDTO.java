package click.escuela.school.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseDTO {

	public CourseDTO(String id, Integer year, String division, Integer countStudent) {
		super();
		this.id = id;
		this.year = year;
		this.division = division;
		this.countStudent = countStudent;
	}

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "year")
	private Integer year;

	@JsonProperty(value = "division")
	private String division;

	@JsonProperty(value = "countStudent")
	private Integer countStudent;

}
