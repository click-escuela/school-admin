package click.escuela.school.admin.model;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import click.escuela.school.admin.enumerator.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bill")
@Entity
@Builder
public class Bill  {
	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "id_school", nullable = false)
	private UUID schoolId;
	
	@Column(name = "month", nullable = false)
	private Integer month;
	
	@Column(name = "year", nullable = false)
	private Integer year;

	@Column(name = "amount", nullable = false)
	private Double amount;

	@Column(name = "file", nullable = false)
	private String file;
	
	@ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="id_student")
    private Student student; 
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
}
