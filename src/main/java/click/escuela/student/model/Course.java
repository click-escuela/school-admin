package click.escuela.student.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {
	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2") 
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "student_count", nullable = false)
	private Integer studentCount;
}
