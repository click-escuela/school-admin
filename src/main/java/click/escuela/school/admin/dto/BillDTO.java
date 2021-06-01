package click.escuela.school.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.school.admin.enumerator.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillDTO {

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "month")
	private Integer month;
	
	@JsonProperty(value = "year")
	private Integer year;
	
	@JsonProperty(value = "amount")
	private Double amount;

	@JsonProperty(value = "file")
	private String file;
	
	@JsonProperty(value = "status")
	private PaymentStatus status;


}
