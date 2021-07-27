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
public enum ProductoCategoriaEnum {
	PRODUCTO_EQUIVALENTE_INASE("Equivalente con la codificacion del Inase", ProductoCategoriaSetEnum.PRODUCTO_EQUIVALENTE, TipoDato.Long),
	
	//en vez de hacer una categoria para cada toxicidad, no seria mejor hacer una categoria PRODUCTO_TOXICIDAD con un String, pero que solo pueda tomar valores de un Enum
	//creo que si seria conveniente usar una categoria individual, cuando guardamos referencia en una entidad, ejemplo en Movimiento de Uvas guardamos el color
	//PRODUCTO_TOXICIDAD_CLASE_1("Altamente Peligroso", ProductoCategoriaSetEnum.PRODUCTO_TOXICIDAD, null),
	//PRODUCTO_TOXICIDAD_CLASE_2("Moderadamente Peligroso", ProductoCategoriaSetEnum.PRODUCTO_TOXICIDAD, null),
	//PRODUCTO_TOXICIDAD_CLASE_3("Ligeramente Peligroso", ProductoCategoriaSetEnum.PRODUCTO_TOXICIDAD, null),
	//PRODUCTO_TOXICIDAD_CLASE_4("Improbable que presenten Peligro", ProductoCategoriaSetEnum.PRODUCTO_TOXICIDAD, null),
	PRODUCTO_TOXICIDAD("Categoria de Toxicidad del Producto", ProductoCategoriaSetEnum.PRODUCTO_CARACTERISTICA_ESPECIAL, TipoDato.Enum),
	
	//id del productoGenerico equivalente, en la liquidacion se necesita un producto para hacer el comprobante asociado, pero la liquidacion esta relacionada con un productoGenerico, entonces con esta equivalencia se busca el producto que le corresponde a ese productoGenerico
	PRODUCTO_EQUIVALENTE_GENERICO("Equivalente en la tabla ProductoGenerico", ProductoCategoriaSetEnum.PRODUCTO_EQUIVALENTE, TipoDato.Long);
	
/*	PRODUCTO_UVA("Uva", ProductoCategoriaSetEnum.PRODUCTO_GENERAL, null),
	PRODUCTO_TIPO_CONVENCIONAL("Convencional", ProductoCategoriaSetEnum.PRODUCTO_CARACTERISTICA_ESPECIAL, null),//primero se habia pensado en marcar solo las organicas, pero para dejar mas en claro se identifican las dos
	PRODUCTO_TIPO_ORGANICA("Organica", ProductoCategoriaSetEnum.PRODUCTO_CARACTERISTICA_ESPECIAL, null),
	PRODUCTO_EQUIVALENTE_ORACLE_EBS("Equivalente Oracle EBS items.inventory_item_id", ProductoCategoriaSetEnum.PRODUCTO_EQUIVALENTE, TipoDato.BigDecimal),
	PRODUCTO_EQUIVALENTE_CONVENCIONAL("Es la variedad equivalente convencional", ProductoCategoriaSetEnum.PRODUCTO_EQUIVALENTE, TipoDato.Long),//es el producto.id convencional
	PRODUCTO_COLOR_BLANCA("Convencional", ProductoCategoriaSetEnum.PRODUCTO_COLOR, null),
	PRODUCTO_COLOR_ROSADA("Organica", ProductoCategoriaSetEnum.PRODUCTO_COLOR, null),
	PRODUCTO_COLOR_TINTA("Organica", ProductoCategoriaSetEnum.PRODUCTO_COLOR, null);*/
	
	private String descripcion;
	private ProductoCategoriaSetEnum categoriaSet;
	private TipoDato tipoDato;
	
	private ProductoCategoriaEnum(String descripcion, ProductoCategoriaSetEnum categoriaSet, TipoDato tipoDato) {
		this.setDescripcion(descripcion);
		this.categoriaSet = categoriaSet;
		this.tipoDato = tipoDato;
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

	public static List<ProductoCategoriaEnum> getBySet(ProductoCategoriaSetEnum categoriaSet) {
		List<ProductoCategoriaEnum> registros = new ArrayList<>();
		for (ProductoCategoriaEnum categoria: values()){
			if (categoriaSet.equals(categoria.getCategoriaSet())){
				registros.add(categoria);
			}
		}
		return registros;
	}
}
