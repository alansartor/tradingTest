package ar.com.signals.trading.configuracion.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import ar.com.signals.trading.configuracion.support.TipoDato;

/**
 * Tabla para agrupar entidades
 * @author Pepe
 *
 */
@Entity
public class Categoria {
	@Id
	@Column(length=50)
	private String id;
	private String descripcion;
	@ManyToOne
	private CategoriaSet categoriaSet;
	@Column(length=40)
	@Enumerated(EnumType.STRING)
	private TipoDato tipoDato;//opcional, solo si tiene valor
	//no se necesita para mysql@Column(columnDefinition = "number(1) default 0")
	private boolean activo;
	private Timestamp creation_date;
	@Column(nullable = false)//todas las entidades que se sincronizan deben tener este dato obligatoriamente
	private Timestamp last_update_date;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public CategoriaSet getCategoriaSet() {
		return categoriaSet;
	}
	public void setCategoriaSet(CategoriaSet categoriaSet) {
		this.categoriaSet = categoriaSet;
	}
	public TipoDato getTipoDato() {
		return tipoDato;
	}
	public void setTipoDato(TipoDato tipoDato) {
		this.tipoDato = tipoDato;
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
		if (!(obj instanceof Categoria))
			return false;
		Categoria other = (Categoria) obj;//lazy issue, comparar con instanceof
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return id + "-" + descripcion;
	}
}
