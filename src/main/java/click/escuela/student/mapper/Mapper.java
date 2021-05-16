package click.escuela.student.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import click.escuela.student.api.AdressApi;
import click.escuela.student.api.CourseApi;
import click.escuela.student.api.CourseApiUpdate;
import click.escuela.student.api.ParentApi;
import click.escuela.student.api.StudentApi;
import click.escuela.student.api.StudentUpdateApi;
import click.escuela.student.api.TeacherApi;
import click.escuela.student.dto.CourseDTO;
import click.escuela.student.dto.StudentDTO;
import click.escuela.student.dto.TeacherDTO;
import click.escuela.student.enumerator.DocumentType;
import click.escuela.student.enumerator.EducationLevels;

import click.escuela.student.enumerator.GenderType;
import click.escuela.student.model.Adress;
import click.escuela.student.model.Course;
import click.escuela.student.model.Parent;
import click.escuela.student.model.Student;
import click.escuela.student.model.Teacher;

@Component
public class Mapper {

	private static ModelMapper modelMapper = new ModelMapper();

	public static Student mapperToStudent(StudentApi studentApi) {
		Student student = modelMapper.map(studentApi, Student.class);
		student.setGender(mapperToEnum(studentApi.getGender()));
		student.setLevel(mapperToEnumLevel(studentApi.getLevel()));
		student.setAdress(mapperToAdress(studentApi.getAdressApi()));
		student.setParent(mapperToParent(studentApi.getParentApi()));
		return student;
	}

	public static StudentDTO mapperToStudentDTO(Student student) {
		return modelMapper.map(student, StudentDTO.class);
	}

	public static Student mapperToStudent(StudentDTO studentdto) {
		return modelMapper.map(studentdto, Student.class);
	}

	public static StudentApi mapperToStudent(Student student) {
		return modelMapper.map(student, StudentApi.class);
	}

	public static StudentDTO mapperToStudentDTO(StudentUpdateApi studentUpdateApi) {
		return modelMapper.map(studentUpdateApi, StudentDTO.class);
	}

	public static StudentDTO mapperToStudentDTO(StudentApi studentApi) {
		return modelMapper.map(studentApi, StudentDTO.class);
	}

	public static Parent mapperToParent(ParentApi parentApi) {
		Parent parent = modelMapper.map(parentApi, Parent.class);
		parent.setAdress(mapperToAdress(parentApi.getAdressApi()));
		return parent;
	}

	public static ParentApi mapperToParentApi(Parent parent) {
		return modelMapper.map(parent, ParentApi.class);
	}

	public static AdressApi mapperToAdressApi(Adress adress) {
		return modelMapper.map(adress, AdressApi.class);
	}

	public static Adress mapperToAdress(AdressApi adressApi) {
		return modelMapper.map(adressApi, Adress.class);
	}

	public static GenderType mapperToEnum(String string) {
		return modelMapper.map(GenderType.valueOf(string), GenderType.class);
	}

	public static DocumentType mapperToEnumDocument(String string) {
		return modelMapper.map(DocumentType.valueOf(string), DocumentType.class);
	}

	public static EducationLevels mapperToEnumLevel(String string) {
		return modelMapper.map(EducationLevels.valueOf(string), EducationLevels.class);
	}

	public static List<StudentDTO> mapperToStudentsDTO(List<Student> students) {
		List<StudentDTO> studentDTOList = new ArrayList<>();
		students.stream().forEach(p -> studentDTOList.add(mapperToStudentDTO(p)));
		return studentDTOList;
	}

	public static List<Student> mapperToStudents(List<StudentDTO> students) {
		List<Student> studentList = new ArrayList<>();
		students.stream().forEach(p -> studentList.add(mapperToStudent(p)));
		return studentList;
	}

	// All mapper courses
	public static Course mapperToCourse(CourseApi courseApi) {
		return modelMapper.map(courseApi, Course.class);
	}

	public static CourseDTO mapperToCourseDTO(Course course) {
		return modelMapper.map(course, CourseDTO.class);
	}

	public static List<CourseDTO> mapperToCoursesDTO(List<Course> courses) {
		List<CourseDTO> courseDTOList = new ArrayList<>();
		courses.stream().forEach(p -> courseDTOList.add(mapperToCourseDTO(p)));
		return courseDTOList;
	}

	public static CourseApi mapperToCourseApi(Course course) {
		return modelMapper.map(course, CourseApi.class);
	}

	public static CourseApiUpdate mapperToCourseApiUpdate(Course course) {
		return modelMapper.map(course, CourseApiUpdate.class);
	}

	public static Teacher mapperToTeacher(TeacherApi teacherApi) {
		Teacher teacher = modelMapper.map(teacherApi, Teacher.class);
		teacher.setAdress(mapperToAdress(teacherApi.getAdressApi()));
		teacher.setCourseId(UUID.fromString(teacherApi.getCourseId()));
		teacher.setGender(mapperToEnum(teacherApi.getGender()));
		teacher.setDocumentType(mapperToEnumDocument(teacherApi.getDocumentType()));
		return teacher;
	}

	private static TeacherDTO mapperToTeacherDTO(Teacher teacher) {
		return modelMapper.map(teacher, TeacherDTO.class);
	}

	public static List<TeacherDTO> mapperToTeachersDTO(List<Teacher> teachers) {
		List<TeacherDTO> teachersDTO = new ArrayList<>();
		teachers.stream().forEach(p -> teachersDTO.add(mapperToTeacherDTO(p)));
		return teachersDTO;
	}

}
