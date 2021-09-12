package click.escuela.school.admin.enumerator;

public enum ParentMessage {

	GET_ERROR("GET_ERROR", "El padre no existe");

	private String code;
	private String description;

	ParentMessage(String code, String description) {
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
