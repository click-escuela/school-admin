package click.escuela.school.admin.api;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

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
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Teacher Api")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TeacherApi extends PersonApi {

	public TeacherApi(String name, String surname, String documentType, String document, String gender, LocalDate birthday,
			AdressApi adressApi, String cellPhone, String email, String courseId) {

		super(name, surname, document, gender, birthday, adressApi, cellPhone, email);
		this.documentType = documentType;
		this.courseId = courseId;
	}

	@NotBlank(message = "Document type cannot be empty")
	@JsonProperty(value = "documentType", required = true)
	private String documentType;

	@NotBlank(message = "Course cannot be empty")
	@JsonProperty(value = "courseId", required = true)
	private String courseId;
}
