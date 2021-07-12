package click.escuela.school.admin.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.school.admin.enumerator.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherCourseStudentsDTO extends TeacherDTO {

	public TeacherCourseStudentsDTO(String id, String name, String surname, DocumentType documentType, String document,
			LocalDate birthday, AdressDTO adress, String cellPhone, String email, Integer schoolId,
			List<CourseStudentsDTO> courses) {
		super(id, name, surname, documentType, document, birthday, adress, cellPhone, email, schoolId);
		this.courses = courses;
	}

	@JsonProperty(value = "courses")
	private List<CourseStudentsDTO> courses;
}