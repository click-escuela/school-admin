package click.escuela.student.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.student.model.Adress;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentDTO {

	@JsonProperty(value = "name")
	private String name;
	
	@JsonProperty(value = "surname")
	private String surname;
	
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
	
}
