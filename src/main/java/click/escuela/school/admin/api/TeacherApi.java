package click.escuela.school.admin.api;


import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.school.admin.model.Teacher;

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

	public TeacherApi(Teacher teacher, AdressApi adressApi, String documentType) {
		super(teacher.getName(), teacher.getSurname(), teacher.getDocument(), teacher.getGender().toString(),
				teacher.getBirthday(), adressApi, teacher.getCellPhone(), teacher.getEmail());
		this.documentType = documentType;
	}

	@JsonProperty(value = "id", required = false)
	private String id;

	@NotBlank(message = "Document type cannot be empty")
	@JsonProperty(value = "documentType", required = true)
	private String documentType;
  
	@JsonProperty(value = "courseId", required = false)
	private List<String> courses;
}
