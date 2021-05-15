package click.escuela.school.admin.enumerator;

public enum BillEnum {

	CREATE_OK("CREATED_BILL", "Se creó la factura correctamente"),
	CREATE_ERROR("CREATE_ERROR", "No se pudo crear la factura correctamente"), 
	GET_ERROR("GET_ERROR", "No se encontró la factura");

	private String code;
	private String description;

	BillEnum(String code, String description) {
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
