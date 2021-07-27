package ar.com.signals.trading.configuracion.support;

/**
 * Son algunas de las CategoriasSet que se utilizan para las ProductoCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum SeguridadCategoriaSetEnum {
	SEGURIDAD_NIVEL_EDICION("Nivel de Edicion", TipoSet.workflow, NivelSet.global);
	
	private String descripcion;
	private TipoSet tipoSet;
	private NivelSet nivelSet;
	
	private SeguridadCategoriaSetEnum(String descripcion, TipoSet tipoSet, NivelSet nivelSet) {
		this.descripcion = descripcion;
		this.tipoSet = tipoSet;
		this.nivelSet = nivelSet;
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public TipoSet getTipoSet() {
		return tipoSet;
	}
	public void setTipoSet(TipoSet tipoSet) {
		this.tipoSet = tipoSet;
	}
	public NivelSet getNivelSet() {
		return nivelSet;
	}
	public void setNivelSet(NivelSet nivelSet) {
		this.nivelSet = nivelSet;
	}
}
