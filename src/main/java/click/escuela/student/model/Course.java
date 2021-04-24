package click.escuela.student.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
@Entity
public class Course {
	@Id
	@Column(name = "grade_id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID gradeId;

	@Column(name = "year", nullable = false)
	private Integer year;

	@Column(name = "division", nullable = false)
	private String division;

	@Column(name = "count_student", nullable = false)
	private Integer countStudent;

	@Column(name = "teacher_id", nullable = true)
	private String teacherId;
	
	/*@Column(name = "school_id", nullable = false)
	private String schoolId;*/
}
