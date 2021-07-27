package ar.com.signals.trading.configuracion.support;

import ar.com.signals.trading.configuracion.support.TipoDato;

/**
 * Son algunas de las Categorias que se utilizan para las SucursalCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum SucursalCategoriaEnum {
	SUCURSAL_EQUIVALENTE_ORACLE_EBS("Equivalente Oracle EBS Or_prov_sucursal.vendor_site_id", SucursalCategoriaSetEnum.SUCURSAL_EQUIVALENTE, TipoDato.BigDecimal),
	SUCURSAL_TIPO_COSECHA("Es el tipo de cosecha que realiza el productor en la sucursal (Manual o Mecanica)", SucursalCategoriaSetEnum.SUCURSAL_CARACTERISTICA, TipoDato.String);
	
	private String descripcion;
	private SucursalCategoriaSetEnum categoriaSet;
	private TipoDato tipoDato;
	
	private SucursalCategoriaEnum(String descripcion, SucursalCategoriaSetEnum categoriaSet, TipoDato tipoDato) {
		this.setDescripcion(descripcion);
		this.categoriaSet = categoriaSet;
		this.tipoDato = tipoDato;
	}
	
	public SucursalCategoriaSetEnum getCategoriaSet() {
		return categoriaSet;
	}
	public void setCategoriaSet(SucursalCategoriaSetEnum categoriaSet) {
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
