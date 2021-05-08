package click.escuela.student.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentFullDTO extends StudentDTO{

	@JsonProperty(value = "bill")
	private List<BillDTO> bill;
}