package click.escuela.school.admin.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.enumerator.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teacher")
@Entity
@Builder
public class Teacher {

	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "name", nullable = false)
	@NotBlank(message = "El nombre no puede estar vacía")
	private String name;

	@Column(name = "surname", nullable = false)
	@NotBlank(message = "El apellido no puede estar vacío")
	private String surname;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentType documentType;

	@Column(name = "document", nullable = false)
	private String document;

	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private GenderType gender;

	@Column(name = "birthday", nullable = false, columnDefinition = "DATETIME")
	private LocalDate birthday;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_adress", nullable = false)
	private Adress adress;

	@Column(name = "cell_phone", nullable = false)
	private String cellPhone;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "course_id", columnDefinition = "BINARY(16)", nullable = true)
	private UUID courseId;

	@Column(name = "school_id", nullable = false)
	private Integer schoolId;

}
