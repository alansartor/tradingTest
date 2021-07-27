package ar.com.signals.trading.configuracion.domain;

import java.sql.Timestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

/**
 * clase abstracta que deben heredar aquellas entidades de configuracion de Categorias.
 * Ej: ProductoCategoria, PropiedadCategoria
 * @author Pepe
 *
 */
@MappedSuperclass
public abstract class AbstractCategoria {
	//public static final String SEQUENCE_NAME = "CATEGORIA_SEQ";//usar esta sequence, esto es debido a que solo en msc se crean registros
	@TableGenerator(name="abstract_categoria_seq", pkColumnValue="abstract_categoria" , allocationSize=10)//hibernate crea una tabla por defecto para mantener secuencias, para compartir la secuencia me parece que no queda otra que armar una superclass, porque de otra forma no se puede
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE , generator="abstract_categoria_seq")
	protected Long id;

	//No puedo armar id con estas entidades porque organization_id puede ser null, y no se puede poner null en in id
	@ManyToOne
	protected Categoria categoria;
	//no se necesita para mysql@Column(columnDefinition = "number(1) default 0")
	protected boolean activo;
	protected Timestamp creation_date;
	protected Timestamp last_update_date;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public Timestamp getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Timestamp creation_date) {
		this.creation_date = creation_date;
	}
	public Timestamp getLast_update_date() {
		return last_update_date;
	}

	public void setLast_update_date(Timestamp last_update_date) {
		this.last_update_date = last_update_date;
	}
}
