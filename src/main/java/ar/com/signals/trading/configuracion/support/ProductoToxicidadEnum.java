package ar.com.signals.trading.configuracion.support;

public enum ProductoToxicidadEnum {
	CLASE_1("Altamente Peligroso"),
	CLASE_2("Moderadamente Peligroso"),
	CLASE_3("Ligeramente Peligroso"),
	CLASE_4("Improbable que presenten Peligro");
	
	private String descripcion;
	
	private ProductoToxicidadEnum(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public static String valoresAsString() {
		String valores = "";
		for (ProductoToxicidadEnum item : ProductoToxicidadEnum.values()) {
			valores += item.name() + ", ";
		}
		if(valores.length() > 0) {
			valores = valores.substring(0, valores.length() -2);
		}
		return valores;
	}
}
