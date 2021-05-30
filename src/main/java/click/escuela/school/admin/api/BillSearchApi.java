package click.escuela.school.admin.api;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
@Schema(description = "Bill Api")
@SuperBuilder
public class BillSearchApi {

	@Min(value = 1, message = "Month should not be less than 1")
    @Max(value = 12, message = "Month should not be greater than 12")
	@JsonProperty(value = "month", required = false)
	private Integer month;
	
	@JsonProperty(value = "year", required = false)
	private Integer year;

	@JsonProperty(value = "status", required = false)
	private String status;
	
}
