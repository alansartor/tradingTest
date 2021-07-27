package ar.com.signals.trading.configuracion.support;

import ar.com.signals.trading.configuracion.support.TipoDato;

/**
 * Son algunas de las Categorias que se utilizan para las clienteCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum ClienteCategoriaEnum {
	CLIENTE_EQUIVALENTE_ORACLE_EBS("Equivalente Oracle EBS or_proveedor.vendor_id", ClienteCategoriaSetEnum.CLIENTE_EQUIVALENTE, TipoDato.BigDecimal);
	
	private String descripcion;
	private ClienteCategoriaSetEnum categoriaSet;
	private TipoDato tipoDato;
	
	private ClienteCategoriaEnum(String descripcion, ClienteCategoriaSetEnum categoriaSet, TipoDato tipoDato) {
		this.setDescripcion(descripcion);
		this.categoriaSet = categoriaSet;
		this.tipoDato = tipoDato;
	}
	
	public ClienteCategoriaSetEnum getCategoriaSet() {
		return categoriaSet;
	}
	public void setCategoriaSet(ClienteCategoriaSetEnum categoriaSet) {
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
