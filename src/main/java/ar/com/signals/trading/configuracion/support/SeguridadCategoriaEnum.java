package ar.com.signals.trading.configuracion.support;

/**
 * Son las Categorias que se utilizan para las PropiedadCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum SeguridadCategoriaEnum {
	SEGURIDAD_NIVEL_UNO("Es el por defecto, no hace falta asignarlo, es el minimo privilegio de edicion", SeguridadCategoriaSetEnum.SEGURIDAD_NIVEL_EDICION),
	SEGURIDAD_NIVEL_DOS("Generalmente es para permitir editar la entidad hasta antes que finalize el movimiento", SeguridadCategoriaSetEnum.SEGURIDAD_NIVEL_EDICION),
	SEGURIDAD_NIVEL_TRES("Generalmente es para permitir editar la entidad en cualquier momento", SeguridadCategoriaSetEnum.SEGURIDAD_NIVEL_EDICION);

	private String descripcion;
	private SeguridadCategoriaSetEnum categoriaSet;
	
	private SeguridadCategoriaEnum(String descripcion, SeguridadCategoriaSetEnum categoriaSet) {
		this.setDescripcion(descripcion);
		this.categoriaSet = categoriaSet;
	}
	
	public SeguridadCategoriaSetEnum getCategoriaSet() {
		return categoriaSet;
	}
	public void setCategoriaSet(SeguridadCategoriaSetEnum categoriaSet) {
		this.categoriaSet = categoriaSet;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
