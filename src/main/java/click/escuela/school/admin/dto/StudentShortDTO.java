package click.escuela.school.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentShortDTO {

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "surname")
	private String surname;
	
	@JsonProperty(value = "courseId")
	private String courseId;

}