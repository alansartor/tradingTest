package ar.com.signals.trading.configuracion.support;

import java.util.ArrayList;
import java.util.List;

import ar.com.signals.trading.configuracion.support.TipoDato;

/**
 * Son algunas de las Categorias que se utilizan para las ProductoCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum ProductoGenericoCategoriaEnum {
	PRODUCTOGENERICO_EQUIVALENTE_AFIP_ID("Equivalente entre la especia del Inase y el id de la tabla de Afip CTG", ProductoCategoriaSetEnum.PRODUCTO_EQUIVALENTE, TipoDato.Integer),
	PRODUCTOGENERICO_EQUIVALENTE_AFIP_DESCRIPCION("Equivalente entre la especia del Inase y la descripcion de la tabla de Afip CTG", ProductoCategoriaSetEnum.PRODUCTO_EQUIVALENTE, TipoDato.String),
	PRODUCTOGENERICO_COTIZACION_REFERENCIA("Cotizacion en USD/TN de referencia, por si no hay otro lado donde buscar", "150", ProductoCategoriaSetEnum.PRODUCTO_EQUIVALENTE, TipoDato.BigDecimal);
	
	private String descripcion;
	private ProductoCategoriaSetEnum categoriaSet;
	private TipoDato tipoDato;
	private String defaultValue;
	
	private ProductoGenericoCategoriaEnum(String descripcion, ProductoCategoriaSetEnum categoriaSet, TipoDato tipoDato) {
		this.setDescripcion(descripcion);
		this.categoriaSet = categoriaSet;
		this.tipoDato = tipoDato;
	}
	
	private ProductoGenericoCategoriaEnum(String descripcion, String defaultValue, ProductoCategoriaSetEnum categoriaSet, TipoDato tipoDato) {
		this.setDescripcion(descripcion);
		this.categoriaSet = categoriaSet;
		this.tipoDato = tipoDato;
		this.defaultValue = defaultValue;
	}
	
	public ProductoCategoriaSetEnum getCategoriaSet() {
		return categoriaSet;
	}
	public void setCategoriaSet(ProductoCategoriaSetEnum categoriaSet) {
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

	public static List<ProductoGenericoCategoriaEnum> getBySet(ProductoCategoriaSetEnum categoriaSet) {
		List<ProductoGenericoCategoriaEnum> registros = new ArrayList<>();
		for (ProductoGenericoCategoriaEnum categoria: values()){
			if (categoriaSet.equals(categoria.getCategoriaSet())){
				registros.add(categoria);
			}
		}
		return registros;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
