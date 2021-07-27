package ar.com.signals.trading.configuracion.support;

public enum ImportarColumnaEnum {
	No_Tiene_Nombre_Columnas("No_Tiene_Nombre_Columnas", 0),
	Ignorar_Primera_linea("Ignorar_Primera_linea", 1),
	Ignorar_Dos_lineas("Ignorar_Dos_lineas", 2),
	Ignorar_Tres_lineas("Ignorar_Tres_lineas", 3);
	
	private String descripcion;
	private Integer lineasIgnorar;
	
	private ImportarColumnaEnum(String descripcion, Integer lineasIgnorar) {
		this.descripcion = descripcion;
		this.lineasIgnorar = lineasIgnorar;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getLineasIgnorar() {
		return lineasIgnorar;
	}

	public void setLineasIgnorar(Integer lineasIgnorar) {
		this.lineasIgnorar = lineasIgnorar;
	}
}
