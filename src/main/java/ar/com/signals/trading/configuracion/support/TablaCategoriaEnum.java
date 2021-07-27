package ar.com.signals.trading.configuracion.support;

import ar.com.signals.trading.configuracion.support.TipoDato;

/**
 * Son algunas de las Categorias que se utilizan para las TablaCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum TablaCategoriaEnum {
	TABLA_BRIX_A_AZUCAR("De Brix a Azucar", TablaCategoriaSetEnum.TABLA_CONVERSION, TipoDato.Double);
	
	private String descripcion;
	private TablaCategoriaSetEnum categoriaSet;
	private TipoDato tipoDato;
	
	private TablaCategoriaEnum(String descripcion, TablaCategoriaSetEnum categoriaSet, TipoDato tipoDato) {
		this.setDescripcion(descripcion);
		this.categoriaSet = categoriaSet;
		this.tipoDato = tipoDato;
	}
	
	public TablaCategoriaSetEnum getCategoriaSet() {
		return categoriaSet;
	}
	public void setCategoriaSet(TablaCategoriaSetEnum categoriaSet) {
		this.categoriaSet = categoriaSet;
	}
	public TipoDato getTipoDato() {
		return tipoDato;
	}
	public void setTipoDato(TipoDato tipoDato) {
		this.tipoDato = tipoDato;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
