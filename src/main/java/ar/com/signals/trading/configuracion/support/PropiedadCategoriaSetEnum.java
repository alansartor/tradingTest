package ar.com.signals.trading.configuracion.support;

/**
 * Son las Categorias Set que se utilizan para las PropiedadCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum PropiedadCategoriaSetEnum implements ICategoriaSet{
	PROPIEDAD_CONFIGURACIONES_GLOBAL("Configuracion Globales, se aplican en toda la aplicacion", TipoSet.valor, NivelSet.global),
	
	PROPIEDAD_ENTIDADES_ID_GLOBAL("Configuracion Globales, hace referencia a id de entidades", TipoSet.id, NivelSet.global),
	
	PROPIEDAD_CONFIGURACIONES_UNIDADPRODUCTIVA("Configuracion Generales de la Empresa", TipoSet.valor, NivelSet.unidadProductiva),
	PROPIEDAD_CONFIGURACIONES_SUCURSAL("Configuracion Generales de la Planta", TipoSet.valor, NivelSet.sucursal),
	PROPIEDAD_CONFIGURACIONES_SUJETO("Configuracion Generales de la Empresa", TipoSet.valor, NivelSet.sujeto),
	
	
	PROPIEDAD_CARACTERISTICA_SUJETO("Caracteristica que se aplica a una Planta especifica", TipoSet.caracteristica, NivelSet.sujeto),
	PROPIEDAD_CARACTERISTICA_GLOBAL("Caracteristica Globales, se aplican a todas las Plantas", TipoSet.caracteristica, NivelSet.global);
	
	private String descripcion;
	private TipoSet tipoSet;
	private NivelSet nivelSet;
	
	private PropiedadCategoriaSetEnum(String descripcion, TipoSet tipoSet, NivelSet nivelSet) {
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
