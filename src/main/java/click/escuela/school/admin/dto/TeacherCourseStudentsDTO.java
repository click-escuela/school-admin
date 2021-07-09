package click.escuela.school.admin.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.school.admin.enumerator.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherCourseStudentsDTO {

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "surname")
	private String surname;

	@JsonProperty(value = "documentType")
	private DocumentType documentType;

	@JsonProperty(value = "document")
	private String document;

	@JsonProperty(value = "birthday")
	private LocalDate birthday;

	@JsonProperty(value = "adress")
	private AdressDTO adress;

	@JsonProperty(value = "cellPhone")
	private String cellPhone;

	@JsonProperty(value = "email")
	private String email;
	
	@JsonProperty(value = "courses")
	private List<CourseStudentsDTO> courses;
	
	@Column(name = "school_id", nullable = false)
	private Integer schoolId;
}