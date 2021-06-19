package click.escuela.school.admin.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import click.escuela.school.admin.enumerator.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "excel")
@Entity
@Builder
public class Excel {
	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "date", nullable = false, columnDefinition = "DATETIME")
	private LocalDate date;

	@Column(name = "school_id", nullable = false)
	private Integer schoolId;

	@Column(name = "file", nullable = false)
	private String file;

	@Column(name = "student_count", nullable = false)
	private Integer studentCount;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private FileStatus status;

}
