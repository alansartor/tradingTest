package ar.com.signals.trading.configuracion.support;

/**
 * Son las CategoriasSet que se utilizan para las LineaCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum LineaCategoriaSetEnum implements ICategoriaSet{
	LINEA_IMPRESORA_ETHERNET("Impresoras de Red", TipoSet.agrupador, NivelSet.global),
	LINEA_BPepeZA("Cabezal con Conversor Ethernet, Cabezal Ethernet, o Ingreso Manual", TipoSet.agrupador, NivelSet.global),
	LINEA_CAMARA("Camaras Ip", TipoSet.agrupador, NivelSet.global);
	
	private String descripcion;
	private TipoSet tipoSet;
	private NivelSet nivelSet;
	
	private LineaCategoriaSetEnum(String descripcion, TipoSet tipoSet, NivelSet nivelSet) {
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
