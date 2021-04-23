package click.escuela.student.enumerator;

public enum StudentEnum {

	CREATE_OK("CREATED_STUDENT","Se creó el estudiante correctamente"),
	CREATE_ERROR("CREATE_ERROR","No se pudo crear el estudiante correctamente"),
	UPDATE_OK("UPDATE_STUDENT","Se modificó el estudiante correctamente"),
	UPDATE_ERROR("UPDATE_ERROR","No se pudo modificar el estudiante correctamente"),
	DELETE_OK("DELETE_STUDENT","se eliminó el estudiante correctamente"), 
	STUDENT_EXIST("STUDENT_EXIST","Student ya existe");

	private String code;
	private String description;
	
	StudentEnum(String code, String description) {
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
