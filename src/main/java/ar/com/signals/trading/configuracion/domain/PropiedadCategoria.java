package ar.com.signals.trading.configuracion.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Tabla para especificar las categorias globales, de empresa y de planta
 * @author Pepe
 *
 */
@Entity
//@IdClass(PropiedadCategoriaPK.class)
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"categoria_id", "abstract_sujeto_id"}))//Nombre de las columnas en la base de datos
public class PropiedadCategoria extends AbstractCategoria{
	@Column(precision = 22, scale = 0)
	private Long abstract_sujeto_id;//referencia a Sujeto, Sucursal o UnidadProductiva, puede ser null. Esas tres entidades comparte id
	
	/**
	 * opcional
	 * se podria usar para armar equivalencias
	 * Ej: CategoriaSet=equivalencia, Categoria=oracle_item, Valor=inventory_item_id
	 */
	@Column(length=1000)
	private String valor;
	
	public Long getAbstract_sujeto_id() {
		return abstract_sujeto_id;
	}
	public void setAbstract_sujeto_id(Long abstract_sujeto_id) {
		this.abstract_sujeto_id = abstract_sujeto_id;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PropiedadCategoria))
			return false;
		PropiedadCategoria other = (PropiedadCategoria) obj;//lazy issue, comparar con instanceof
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}
