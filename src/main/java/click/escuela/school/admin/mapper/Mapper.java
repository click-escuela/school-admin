package click.escuela.school.admin.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import click.escuela.school.admin.model.School;
import click.escuela.school.admin.api.AdressApi;
import click.escuela.school.admin.api.BillApi;
import click.escuela.school.admin.api.CourseApi;
import click.escuela.school.admin.api.ExcelApi;
import click.escuela.school.admin.api.ParentApi;
import click.escuela.school.admin.api.SchoolApi;
import click.escuela.school.admin.api.StudentApi;
import click.escuela.school.admin.api.TeacherApi;
import click.escuela.school.admin.dto.BillDTO;
import click.escuela.school.admin.dto.CourseDTO;
import click.escuela.school.admin.dto.CourseStudentsDTO;
import click.escuela.school.admin.dto.CourseStudentsShortDTO;
import click.escuela.school.admin.dto.ExcelDTO;
import click.escuela.school.admin.dto.SchoolDTO;
import click.escuela.school.admin.dto.StudentDTO;
import click.escuela.school.admin.dto.StudentShortDTO;
import click.escuela.school.admin.dto.TeacherCourseStudentsDTO;
import click.escuela.school.admin.dto.TeacherDTO;
import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.enumerator.EducationLevels;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.PaymentStatus;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Bill;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Excel;
import click.escuela.school.admin.model.Parent;
import click.escuela.school.admin.model.Student;
import click.escuela.school.admin.model.Teacher;

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

	public static Student mapperToStudent(StudentApi studentApi, Student student) {

		modelMapper.map(studentApi, student);

		student.setGender(mapperToEnum(studentApi.getGender()));
		student.setLevel(mapperToEnumLevel(studentApi.getLevel()));
		student.setAdress(mapperToAdress(studentApi.getAdressApi()));
		student.setParent(mapperToParent(studentApi.getParentApi()));
		return student;
	}

	public static StudentDTO mapperToStudentDTO(Student student) {

		StudentDTO studentDTO = modelMapper.map(student, StudentDTO.class);
		studentDTO.setBills(null);
		if(!Objects.isNull(student.getCourse()))
			studentDTO.setCourseId(student.getCourse().getId().toString());

		return studentDTO;
	}

	public static Student mapperToStudent(StudentDTO studentdto) {
		return modelMapper.map(studentdto, Student.class);
		
	}

	public static StudentApi mapperToStudent(Student student) {
		return modelMapper.map(student, StudentApi.class);
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

	public static StudentDTO mapperToStudentFullDTO(Student student) {
		StudentDTO studentFull = modelMapper.map(student, StudentDTO.class);
		if(student.getCourse() != null) {
			studentFull.setCourseId(student.getCourse().getId().toString());
		}
		List<Bill> bills = student.getBills();
		studentFull.setBills(mapperToBillsDTO(bills));
		return studentFull;
	}

	public static List<StudentDTO> mapperToStudentsFullDTO(List<Student> students) {
		List<StudentDTO> studentFullList = new ArrayList<>();
		students.stream().forEach(p -> studentFullList.add(mapperToStudentFullDTO(p)));
		return studentFullList;
	}

	// All mapper courses
	public static Course mapperToCourse(CourseApi courseApi) {
		return modelMapper.map(courseApi, Course.class);
	}

	public static CourseDTO mapperToCourseDTO(Course course) {
		return modelMapper.map(course, CourseDTO.class);
	}
	
	public static CourseStudentsDTO mapperToCourseStudentsDTO(Course course) {
		return modelMapper.map(course, CourseStudentsDTO.class);
	}

	public static List<CourseDTO> mapperToCoursesDTO(List<Course> courses) {
		List<CourseDTO> courseDTOList = new ArrayList<>();
		courses.stream().forEach(p -> courseDTOList.add(mapperToCourseDTO(p)));
		return courseDTOList;
	}
	
	public static List<CourseStudentsDTO> mapperToCoursesStudentDTO(List<Course> courses) {
		List<CourseStudentsDTO> courseDTOList = new ArrayList<>();
		courses.stream().forEach(p -> courseDTOList.add(mapperToCourseStudentsDTO(p)));
		return courseDTOList;
	}
	
	public static List<CourseStudentsShortDTO> mapperToCoursesStudentsShortDTO(List<Course> courses) {
		List<CourseStudentsShortDTO> courseDTOList = new ArrayList<>();
		courses.stream().forEach(p -> courseDTOList.add(mapperToCourseStudentsShortDTO(p)));
		return courseDTOList;
	}
	
	public static CourseStudentsShortDTO mapperToCourseStudentsShortDTO(Course course) {
		return modelMapper.map(course, CourseStudentsShortDTO.class);
	}

	public static CourseApi mapperToCourseApi(Course course) {
		return modelMapper.map(course, CourseApi.class);
	}

	// Mapper Teacher
	public static Teacher mapperToTeacher(TeacherApi teacherApi) {
		Teacher teacher = modelMapper.map(teacherApi, Teacher.class);
		teacher.setAdress(mapperToAdress(teacherApi.getAdressApi()));
		teacher.setGender(mapperToEnum(teacherApi.getGender()));
		teacher.setDocumentType(mapperToEnumDocument(teacherApi.getDocumentType()));
		return teacher;
	}

	public static Teacher mapperToTeacher(TeacherApi teacherApi, Teacher teacher) {
		modelMapper.map(teacherApi, teacher);
		teacher.setAdress(mapperToAdress(teacherApi.getAdressApi()));
		teacher.setGender(mapperToEnum(teacherApi.getGender()));
		teacher.setDocumentType(mapperToEnumDocument(teacherApi.getDocumentType()));
		return teacher;
	}

	public static TeacherDTO mapperToTeacherDTO(Teacher teacher) {
		return modelMapper.map(teacher, TeacherDTO.class);
	}
	
	public static TeacherCourseStudentsDTO mapperToTeacherCourseStudentsDTO(Teacher teacher) {
		return modelMapper.map(teacher, TeacherCourseStudentsDTO.class);
	}

	public static List<TeacherDTO> mapperToTeachersDTO(List<Teacher> teachers) {
		List<TeacherDTO> teachersDTO = new ArrayList<>();
		teachers.stream().forEach(p -> teachersDTO.add(mapperToTeacherDTO(p)));
		return teachersDTO;
	}

	// Mapper bill
	public static List<BillDTO> mapperToBillsDTO(List<Bill> bills) {
		List<BillDTO> billDTOList = new ArrayList<>();
		bills.stream().forEach(p -> billDTOList.add(mapperToBillDTO(p)));
		return billDTOList;
	}

	public static Bill mapperToBill(BillApi billApi) {
		return modelMapper.map(billApi, Bill.class);
	}

	public static BillDTO mapperToBillDTO(Bill bill) {
		return modelMapper.map(bill, BillDTO.class);
	}
	
	public static PaymentStatus mapperToEnumPaymentStatus(String status) {
		return modelMapper.map(PaymentStatus.valueOf(status), PaymentStatus.class);
	}

	// School
	public static School mapperToSchool(SchoolApi schoolApi) {
		return modelMapper.map(schoolApi, School.class);
	}

	public static List<SchoolDTO> mapperToSchoolsDTO(List<School> schools) {
		List<SchoolDTO> schoolDTOList = new ArrayList<>();
		schools.stream().forEach(p -> schoolDTOList.add(mapperToSchool(p)));
		return schoolDTOList;
	}

	private static SchoolDTO mapperToSchool(School school) {
		return modelMapper.map(school, SchoolDTO.class);
	}

	// Excel
	public static Excel mapperToExcel(ExcelApi excelApi) {
		return modelMapper.map(excelApi, Excel.class);
	}

	public static ExcelDTO mapperToExcel(Excel excel) {
		return modelMapper.map(excel, ExcelDTO.class);
	}

	public static List<ExcelDTO> mapperToExcelsDTO(List<Excel> excels) {
		List<ExcelDTO> excelsDTO = new ArrayList<>();
		excels.stream().forEach(p -> excelsDTO.add(mapperToExcel(p)));
		return excelsDTO;
	}

	public static List<StudentShortDTO> mapperToStudentShort(List<StudentDTO> studentsShortByCourse) {
		 List<StudentShortDTO> students = new ArrayList<>();
		 studentsShortByCourse.stream().forEach(s -> students.add(mapperToStudentShort(s)));
		 return students;
	}

	private static StudentShortDTO mapperToStudentShort(StudentDTO student) {
		return modelMapper.map(student, StudentShortDTO.class);
	}

}
