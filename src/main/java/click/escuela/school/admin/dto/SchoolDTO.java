package click.escuela.school.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchoolDTO {
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "cellPhone")
	private String cellPhone;
	
	@JsonProperty(value = "email")
	private String email;
	
	@JsonProperty(value = "adress")
	private String adress;
	
	@JsonProperty(value = "countStudent")
	private Integer countStudent;
	
	@JsonProperty(value = "countCourses")
	private Integer countCourses;
}
