package click.escuela.student.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Person {
	@Id
	@Column(name = "id_person", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2") 
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "surname", nullable = false)
	private String surname;
	
	@Column(name = "document", nullable = false)
	private String document;
	
	@Column(name = "birthday", nullable = false, columnDefinition = "DATETIME")
	private LocalDate birthday;
	
	@OneToOne
	@JoinColumn(name = "id_adress", nullable = false)
	private Adress adress;
	
	@Column(name = "cell_phone", nullable = false)
	private String cellPhone;
	
	@Column(name = "email", nullable = false)
	private String email;
}
