package click.escuela.school.admin.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.school.admin.enumerator.FileStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExcelDTO {

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "date")
	private LocalDate date;

	@JsonProperty(value = "file")
	private String file;

	@JsonProperty(value = "studentCount")
	private Integer studentCount;

	@JsonProperty(value = "status")
	private FileStatus status;

}