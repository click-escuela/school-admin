package click.escuela.school.admin.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

import click.escuela.school.admin.enumerator.EducationLevels;
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
@Table(name = "student")
@Entity
@Builder
public class Student {

	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "name", nullable = false)
	@NotBlank(message = "El nombre no puede estar vacío")
	private String name;

	@Column(name = "surname", nullable = false)
	@NotBlank(message = "El apellido no puede estar vacío")
	private String surname;

	@Column(name = "document", nullable = false)
	private String document;

	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private GenderType gender;

	@Column(name = "school_id", nullable = false)
	private Integer schoolId;

	@Column(name = "grade", nullable = false)
	private String grade;

	@Column(name = "division", nullable = false)
	private String division;

	@Column(name = "level", nullable = false)
	@Enumerated(EnumType.STRING)
	private EducationLevels level;

	@Column(name = "birthday", nullable = false, columnDefinition = "DATETIME")
	private LocalDate birthday;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_adress", nullable = false)
	private Adress adress;

	@Column(name = "cell_phone", nullable = false)
	private String cellPhone;

	@Column(name = "email", nullable = false)
	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_parent", nullable = false)
	private Parent parent;

	@Column(name = "absences", nullable = true)
	private Integer absences;

	@OneToOne
	@JoinColumn(name = "course", nullable = true)
	private Course course;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", nullable = true)
	private List<Bill> bills;

}
