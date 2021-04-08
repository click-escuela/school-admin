package click.escuela.student.api;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Student Api")
public class StudentApi extends PersonApi{

	public StudentApi(String name, String document, String gender, LocalDate birthday, AdressApi adressApi,
			String cellPhone, String email,ParentApi parentApi, String school) {
		
		super(name, document, gender, birthday, adressApi, cellPhone, email);
		this.parentApi = parentApi;
		this.school = school;
	}

	@JsonProperty(value = "parent", required = true)
	@Valid
	private ParentApi parentApi;

	@NotBlank(message = "School cannot be null")
	@JsonProperty(value = "school", required = true)
	private String school;

}
