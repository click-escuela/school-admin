package click.escuela.school.admin.enumerator;

public enum CourseMessage {

	CREATE_OK("CREATED_STUDENT","Se creó el curso correctamente"),
	CREATE_ERROR("CREATE_ERROR","No se pudo crear el curso correctamente"),
	UPDATE_OK("UPDATE_STUDENT","Se modificó el curso correctamente"),
	UPDATE_ERROR("UPDATE_ERROR","No se pudo modificar el curso correctamente"),
	GET_ERROR("GET_ERROR","No se pudo encontrar el curso");

	private String code;
	private String description;
	
	CourseMessage(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
	
}
