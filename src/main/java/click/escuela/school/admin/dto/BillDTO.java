package click.escuela.school.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import click.escuela.school.admin.enumerator.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDTO {

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "period")
	private Integer period;
	
	@JsonProperty(value = "amount")
	private Double amount;

	@JsonProperty(value = "file")
	private String file;
	
	@JsonProperty(value = "status")
	private PaymentStatus status;


}
