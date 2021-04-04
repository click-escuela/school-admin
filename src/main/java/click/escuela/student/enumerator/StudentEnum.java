package click.escuela.student.enumerator;

public enum StudentEnum {

	CREATE_OK("CREATED_STUDENT","se creo el estudiante correctamente"),
	UPDATE_OK("UPDATE_STUDENT","se modifico el estudiante correctamente"),
	DELETE_OK("DELETE_STUDENT","se elimino el estudiante correctamente");

	private String code;
	private String description;
	
	StudentEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
