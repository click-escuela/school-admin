package click.escuela.student.model;


import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "student")
@Entity
public class Student{
	
	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2") 
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	@Column(name = "name", nullable = false)
    @NotBlank(message="El nombre no puede estar vacía")	
	private String name;
	
	@Column(name = "document", nullable = false)
	private String document;
	
	@Column(name = "gender", nullable = false)
	private String gender;
	
	@Column(name = "school", nullable = false)
	private String school;
	
	@Column(name = "birthday", nullable = false, columnDefinition = "DATETIME")
	private LocalDate birthday;
	
	@OneToOne(cascade= CascadeType.ALL)
	@JoinColumn(name = "id_adress", nullable = false)
	private Adress adress;
	
	@Column(name = "cell_phone", nullable = false)
	private String cellPhone;
	
	@Column(name = "email", nullable = false)
	private String email;
	@OneToOne(cascade= CascadeType.ALL)
	@JoinColumn(name = "id_parent", nullable = false)
	private Parent parent;
	
	@Column(name = "absences", nullable = true)
	private Integer absences;
	
	@OneToOne
	@JoinColumn(name = "id", nullable = true)
	private Course course;
	
}
