package click.escuela.school.admin.api;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Course Api")
@SuperBuilder
public class ExcelApi {
	@JsonProperty(value = "id", required = false)
	private String id;

	@NotBlank(message = "Name cannot be empty")
	@Size(max = 50, message = "Name must be 50 characters")
	@JsonProperty(value = "name", required = true)
	private String name;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty(value = "date", required = true)
	private LocalDate date;
	
	@NotNull(message = "School cannot be null")
	@JsonProperty(value = "schoolId", required = true)
	private Integer schoolId;
	
	@NotBlank(message = "File cannot be empty")
	@JsonProperty(value = "file", required = true)
	private String file;

	@NotNull(message = "StudentCount cannot be null")
	@JsonProperty(value = "studentCount", required = true)
	private Integer studentCount;
	
	@NotBlank(message = "Status cannot be null")
	@JsonProperty(value = "status", required = true)
	private String status;
}
