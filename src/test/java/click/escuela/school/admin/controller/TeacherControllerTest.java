package click.escuela.school.admin.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.school.admin.api.AdressApi;
import click.escuela.school.admin.api.TeacherApi;
import click.escuela.school.admin.dto.CourseStudentsShortDTO;
import click.escuela.school.admin.dto.StudentShortDTO;
import click.escuela.school.admin.dto.TeacherCourseStudentsDTO;
import click.escuela.school.admin.dto.TeacherDTO;
import click.escuela.school.admin.enumerator.CourseMessage;
import click.escuela.school.admin.enumerator.DocumentType;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.TeacherMessage;
import click.escuela.school.admin.enumerator.Validation;
import click.escuela.school.admin.exception.CourseException;
import click.escuela.school.admin.exception.SchoolException;
import click.escuela.school.admin.exception.TeacherException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Adress;
import click.escuela.school.admin.model.Course;
import click.escuela.school.admin.model.Teacher;
import click.escuela.school.admin.rest.TeacherController;
import click.escuela.school.admin.rest.handler.Handler;
import click.escuela.school.admin.service.impl.TeacherServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class TeacherControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private TeacherController teacherController;

	@Mock
	private TeacherServiceImpl teacherService;

	private ObjectMapper mapper;
	private TeacherApi teacherApi;
	private AdressApi adressApi;
	private static String EMPTY = "";
	private String id;
	private String schoolId;
	private String studentId;
	private String courseId;
	private final static String URL = "/school/{schoolId}/teacher/";
	private List<String> listStringIds = new ArrayList<String>();
	private List<CourseStudentsShortDTO> courses = new ArrayList<>();

	@Before
	public void setUp() throws TeacherException, CourseException, SchoolException {
		mockMvc = MockMvcBuilders.standaloneSetup(teacherController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(teacherController, "teacherService", teacherService);

		id = UUID.randomUUID().toString();
		schoolId = "1234";
		courseId = UUID.randomUUID().toString();
		adressApi = new AdressApi("Calle falsa", "6458", "Nogues");
		studentId = UUID.randomUUID().toString();
		List<Course> course = new ArrayList<>();
		listStringIds.add(courseId);
		Course courseEntity = new Course();
		courseEntity.setCountStudent(20);
		courseEntity.setDivision("B");
		courseEntity.setId(UUID.fromString(courseId));
		courseEntity.setYear(10);
		course.add(courseEntity);
		teacherApi = TeacherApi.builder().id(id).gender(GenderType.FEMALE.toString()).name("Mariana").surname("Lopez")
				.birthday(LocalDate.now()).documentType(DocumentType.DNI.toString()).document("25897863")
				.cellPhone("1589632485").email("mariAna@gmail.com").adressApi(adressApi).build();
		Teacher teacher = Teacher.builder().id(UUID.fromString(id)).gender(GenderType.FEMALE).name("Mariana")
				.surname("Lopez").birthday(LocalDate.now()).documentType(DocumentType.DNI).document("25897863")
				.cellPhone("1589632485").email("mariAna@gmail.com").courses(course)
				.adress(new Adress()).build();
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);
		
		CourseStudentsShortDTO courseStudent = new CourseStudentsShortDTO();
		courseStudent.setCountStudent(20);
		courseStudent.setDivision("B");
		courseStudent.setId(courseId);
		courseStudent.setYear(10);
		StudentShortDTO student = new StudentShortDTO();
		student.setId(studentId);
		student.setName("Anotnio");
		student.setSurname("Liendro");
		List<StudentShortDTO> students = new ArrayList<>();
		students.add(student);
		courseStudent.setStudents(students);
		courses.add(courseStudent);

		Mockito.when(teacherService.getById(id)).thenReturn(Mapper.mapperToTeacherDTO(teacher));
		Mockito.when(teacherService.getCoursesByTeacherId(id)).thenReturn(courses);
		Mockito.when(teacherService.getCourseAndStudents(id))
				.thenReturn(Mapper.mapperToTeacherCourseStudentsDTO(teacher));
		Mockito.when(teacherService.getBySchoolId(schoolId)).thenReturn(Mapper.mapperToTeachersDTO(teachers));
		Mockito.when(teacherService.getByCourseId(courseId)).thenReturn(Mapper.mapperToTeachersDTO(teachers));
		Mockito.when(teacherService.findAll()).thenReturn(Mapper.mapperToTeachersDTO(teachers));
	}

	@Test
	public void whenCreateTests() throws JsonProcessingException, Exception {
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi)))).contains(TeacherMessage.CREATE_OK.name());

		doThrow(new TeacherException(TeacherMessage.CREATE_ERROR)).when(teacherService).create(Mockito.anyString(),
				Mockito.any());

		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(TeacherMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenCreateNameEmpty() throws JsonProcessingException, Exception {
		teacherApi.setName(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.NAME_EMPTY.getDescription());
	}

	@Test
	public void whenCreateSurnameEmpty() throws JsonProcessingException, Exception {
		teacherApi.setSurname(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.SURNAME_EMPTY.getDescription());
	}

	@Test
	public void whenCreateDocumentEmpty() throws JsonProcessingException, Exception {
		teacherApi.setDocument(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.DOCUMENT_EMPTY.getDescription());
	}

	@Test
	public void whenCreateDocumentGreaterCharacters() throws JsonProcessingException, Exception {
		teacherApi.setDocument("53454646546456564");
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.DOCUMENT_BAD_SIZE.getDescription());
	}

	@Test
	public void whenCreateDocumentLessCharacters() throws JsonProcessingException, Exception {
		teacherApi.setDocument("3453");
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.DOCUMENT_BAD_SIZE.getDescription());
	}

	@Test
	public void whenCreateDocumentTypeEmpty() throws JsonProcessingException, Exception {
		teacherApi.setDocumentType(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.DOCUMENT_TYPE_EMPTY.getDescription());
	}

	@Test
	public void whenCreateGenderEmpty() throws JsonProcessingException, Exception {
		teacherApi.setGender(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.GENDER_NULL.getDescription());
	}

	@Test
	public void whenCreateCellphoneEmpty() throws JsonProcessingException, Exception {
		teacherApi.setCellPhone(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.CELL_PHONE_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressEmpty() throws JsonProcessingException, Exception {
		teacherApi.setAdressApi(null);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.ADRESS_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressNumberEmpty() throws JsonProcessingException, Exception {
		adressApi.setNumber(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.NUMBER_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressStreetEmpty() throws JsonProcessingException, Exception {
		adressApi.setStreet(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.STREET_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressLocalityEmpty() throws JsonProcessingException, Exception {
		adressApi.setLocality(EMPTY);
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.LOCALITY_NULL.getDescription());
	}

	@Test
	public void whenCreateAdressNumberGreaterCharacters() throws JsonProcessingException, Exception {
		adressApi.setNumber("544546546464");
		assertThat(result(post(URL, schoolId).content(toJson(teacherApi))))
				.contains(Validation.NUMBER_BAD_SIZE.getDescription());
	}

	@Test
	public void whenUpdateTests() throws JsonProcessingException, Exception {
		assertThat(result(put(URL, schoolId, id).content(toJson(teacherApi))))
				.contains(TeacherMessage.UPDATE_OK.name());
		doThrow(new TeacherException(TeacherMessage.UPDATE_ERROR)).when(teacherService).update(Mockito.anyString(),
				Mockito.any());
		assertThat(result(put(URL, schoolId, id).content(toJson(teacherApi))))
				.contains(TeacherMessage.UPDATE_ERROR.getDescription());
	}
	
	@Test
	public void getByIdIsTests() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(result(get(URL + "{teacherId}", schoolId, id)), TeacherDTO.class))
				.hasFieldOrPropertyWithValue("id", id);

		doThrow(new TeacherException(TeacherMessage.GET_ERROR)).when(teacherService).getById(id);
		assertThat(result(get(URL + "{teacherId}", schoolId, id))).contains(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void getBySchoolIdTests() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(result(get(URL, schoolId)), new TypeReference<List<TeacherDTO>>() {
		}).get(0).getId()).contains(id.toString());

		assertThat(mapper.readValue(result(get(URL, "6666")), new TypeReference<List<TeacherDTO>>() {
		})).isEmpty();
	}

	@Test
	public void getByCourseIdTests() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(result(get(URL + "course/{courseId}", schoolId, courseId)),
				new TypeReference<List<TeacherDTO>>() {
				}).get(0).getId()).contains(id.toString());

		assertThat(mapper.readValue(result(get(URL + "course/{courseId}", schoolId, UUID.randomUUID().toString())),
				new TypeReference<List<TeacherDTO>>() {
				})).isEmpty();
	}

	@Test
	public void getByCoursesAndStudentsTests() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(result(get(URL + "{teacherId}/courses", schoolId, id)),
				TeacherCourseStudentsDTO.class)).hasFieldOrPropertyWithValue("id", id);

		doThrow(new TeacherException(TeacherMessage.GET_ERROR)).when(teacherService).getCourseAndStudents(id);
		assertThat(result(get(URL + "{teacherId}/courses", schoolId, id)))
				.contains(TeacherMessage.GET_ERROR.getDescription());
	}
	
	@Test
	public void getCoursesByTeacherIdTests() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(result(get(URL + "{teacherId}/coursesList", schoolId, id)),
				new TypeReference<List<CourseStudentsShortDTO>>() {}).get(0).getId()).contains(courseId);

		doThrow(new TeacherException(TeacherMessage.GET_ERROR)).when(teacherService).getCoursesByTeacherId(id);
		assertThat(result(get(URL + "{teacherId}/coursesList", schoolId, id)))
				.contains(TeacherMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenAddCoursesTests() throws JsonProcessingException, Exception {
		assertThat(result(put(URL + "{idTeacher}/add/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(TeacherMessage.UPDATE_OK.name());

		doThrow(new TeacherException(TeacherMessage.GET_ERROR)).when(teacherService).addCourses(id, listStringIds);
		assertThat(result(put(URL + "{idTeacher}/add/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(TeacherMessage.GET_ERROR.getDescription());

		doThrow(new CourseException(CourseMessage.GET_ERROR)).when(teacherService).addCourses(id, listStringIds);
		assertThat(result(put(URL + "{idTeacher}/add/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(CourseMessage.GET_ERROR.getDescription());
	}
	
	@Test
	public void whenDeleteCoursesTests() throws JsonProcessingException, Exception {
		assertThat(result(put(URL + "{idTeacher}/del/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(TeacherMessage.UPDATE_OK.name());

		doThrow(new TeacherException(TeacherMessage.GET_ERROR)).when(teacherService).deleteCourses(id, listStringIds);
		assertThat(result(put(URL + "{idTeacher}/del/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(TeacherMessage.GET_ERROR.getDescription());

		doThrow(new CourseException(CourseMessage.GET_ERROR)).when(teacherService).deleteCourses(id, listStringIds);
		assertThat(result(put(URL + "{idTeacher}/del/courses", schoolId, id).content(toJson(listStringIds))))
				.contains(CourseMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetAllTest() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(result(get(URL + "getAll", schoolId)), 
				new TypeReference<List<TeacherDTO>>() {}).get(0).getId()).contains(id.toString());

	}
	
	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	private String result(MockHttpServletRequestBuilder requestBuilder) throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse()
				.getContentAsString();
	}
}
