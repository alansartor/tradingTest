package ar.com.signals.trading.configuracion.support;

import ar.com.signals.trading.configuracion.support.TipoDato;

/**
 * Son las Categorias que se utilizan para las LineaCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum LineaCategoriaEnum {
	//una linea de una bPepeza debe tener solo una de estas categorias! Ya que en WorkflowHistoryAccion se guarda la linea por donde paso, entonces tendriamos que saber que bPepeza se uso con la linea
	LINEA_BPepeZA_ETHERNET("BPepeza Ethernet", LineaCategoriaSetEnum.LINEA_BPepeZA, TipoDato.String),
	LINEA_BPepeZA_MANUAL("Opcion especial para ingreso manual", LineaCategoriaSetEnum.LINEA_BPepeZA, TipoDato.String),
	LINEA_IMPRESORA_RED_PRINCIPAL("Es la impresora de red principal de la linea", LineaCategoriaSetEnum.LINEA_IMPRESORA_ETHERNET, TipoDato.String),
	LINEA_CAMARA_PRINCIPAL("Camara de red principal", LineaCategoriaSetEnum.LINEA_CAMARA, TipoDato.String),
	LINEA_CAMARA_SECUNDARIA("Camara de red secundaria", LineaCategoriaSetEnum.LINEA_CAMARA, TipoDato.String);
	
	private String descripcion;
	private LineaCategoriaSetEnum categoriaSet;
	private TipoDato tipoDato;
	
	private LineaCategoriaEnum(String descripcion, LineaCategoriaSetEnum categoriaSet, TipoDato tipoDato) {
		this.setDescripcion(descripcion);
		this.categoriaSet = categoriaSet;
		this.tipoDato = tipoDato;
	}
	
	public LineaCategoriaSetEnum getCategoriaSet() {
		return categoriaSet;
	}
	public void setCategoriaSet(LineaCategoriaSetEnum categoriaSet) {
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
