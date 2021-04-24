package click.escuela.student.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.student.enumerator.EducationLevels;
import click.escuela.student.enumerator.GenderType;
import click.escuela.student.model.Adress;
import click.escuela.student.model.Parent;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class StudentDTO {
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "name")
	private String name;
	
	@JsonProperty(value = "surname")
	private String surname;

	@JsonProperty(value = "document")
	private String document;
	
	@JsonProperty(value = "gender")
	private GenderType gender;
	
	@JsonProperty(value = "grade")
	private String grade;
	
	@JsonProperty(value = "division")
	private String division;
	
	@JsonProperty(value = "level")
	private EducationLevels level;

	@JsonProperty(value = "birthday")
	private LocalDate birthday;
	
	@JsonProperty(value = "adress")
	private AdressDTO adress;
	
	@JsonProperty(value = "cellPhone")
	private String cellPhone;
	
	@JsonProperty(value = "email")
	private String email;
	
	@JsonProperty(value = "parent")
	private ParentDTO parent;
	
	/*@JsonProperty(value = "course")
	private CourseDTO course;*/
}