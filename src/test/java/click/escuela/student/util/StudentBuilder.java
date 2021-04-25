package click.escuela.student.util;

import java.time.LocalDate;
import java.util.UUID;

import click.escuela.student.enumerator.GenderType;
import click.escuela.student.model.Adress;
import click.escuela.student.model.Course;
import click.escuela.student.model.Parent;
import click.escuela.student.model.Student;

public class StudentBuilder {

	private UUID id;
	private String name;
	private String surname;
	private String document;
	private GenderType gender;
	private Integer school;
	private String grade;
	private String division;
	private LocalDate birthday;
	private Adress adress;
	private String cellPhone;
	private String email;
	private Parent parent;
	private Integer absences;
	private Course course;

	public static StudentBuilder getBuilder() {
		return new StudentBuilder();
	}
	
	public StudentBuilder setId(UUID id) {
		this.id = id;
		return this;
	}

	public StudentBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public StudentBuilder setSurname(String surname) {
		this.surname = surname;
		return this;
	}

	public StudentBuilder setDocument(String document) {
		this.document = document;
		return this;
	}

	public StudentBuilder setGender(GenderType gender) {
		this.gender = gender;
		return this;
	}

	public StudentBuilder setSchool(Integer school) {
		this.school = school;
		return this;
	}
	
	public StudentBuilder setDivision(String division) {
		this.division = division;
		return this;
	}
	
	public StudentBuilder setGrade(String grade) {
		this.grade = grade;
		return this;
	}

	public StudentBuilder setBirthday(LocalDate birthday) {
		this.birthday = birthday;
		return this;
	}

	public StudentBuilder setAdress(Adress adress) {
		this.adress = adress;
		return this;
	}

	public StudentBuilder setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
		return this;
	}

	public StudentBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	public StudentBuilder setParent(Parent parent) {
		this.parent = parent;
		return this;
	}

	public StudentBuilder setAbsences(Integer absences) {
		this.absences = absences;
		return this;
	}

	public StudentBuilder setCourse(Course course) {
		this.course = course;
		return this;
	}

	public Student getStudent() {
		
		Student student = new Student();
		
		student.setAbsences(absences);
		student.setAdress(adress);
		student.setBirthday(birthday);
		student.setCellPhone(cellPhone);
		student.setCourse(course);
		student.setDocument(document);
		student.setEmail(email);
		student.setGender(gender);
		student.setDivision(division);
		student.setGrade(grade);
		student.setId(id);
		student.setName(name);
		student.setSurname(surname);
		student.setParent(parent);
		student.setSchoolId(school);
		
		return student;
	}

}