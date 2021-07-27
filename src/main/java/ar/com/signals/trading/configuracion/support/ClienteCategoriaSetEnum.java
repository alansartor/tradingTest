package ar.com.signals.trading.configuracion.support;

/**
 * Son algunas de las CategoriasSet que se utilizan para las ProductoCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum ClienteCategoriaSetEnum implements ICategoriaSet{
	CLIENTE_EQUIVALENTE("Equivalencia con otros Sistemas", TipoSet.equivalencia, NivelSet.global);
	
	private String descripcion;
	private TipoSet tipoSet;
	private NivelSet nivelSet;
	
	private ClienteCategoriaSetEnum(String descripcion, TipoSet tipoSet, NivelSet nivelSet) {
		this.descripcion = descripcion;
		this.tipoSet = tipoSet;
		this.nivelSet = nivelSet;
	}
	@Override
	public String getName() {
		return this.name();
	}
	@Override
	public String getDescripcion() {
		return descripcion;
	}
	@Override
	public TipoSet getTipoSet() {
		return tipoSet;
	}
	@Override
	public NivelSet getNivelSet() {
		return nivelSet;
	}
}
