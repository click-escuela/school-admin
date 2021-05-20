package click.escuela.school.admin.api;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Teacher Api")
@AllArgsConstructor
@SuperBuilder
public class TeacherApi extends PersonApi {

	public TeacherApi(String name, String surname, String document, String gender,
			LocalDate birthday, AdressApi adressApi, String cellPhone, String email, String documentType, String courseId) {

		super(name, surname, document, gender, birthday, adressApi, cellPhone, email);
		this.documentType = documentType;
		this.courseId = courseId;
	}
	
	@JsonProperty(value = "id", required = false)
	private String id;

	@NotBlank(message = "Document type cannot be empty")
	@JsonProperty(value = "documentType", required = true)
	private String documentType;

	@NotNull(message = "School ID cannot be null")
	@JsonProperty(value = "schoolId", required = true)
	private Integer schoolId;

	@JsonProperty(value = "courseId", required = false)
	private String courseId;
}
