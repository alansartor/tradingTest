package ar.com.signals.trading.eventos.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"usuario_id", "evento"}))//Nombre de las columnas en la base de datos
public class Suscripcion {	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//no usar mas  GenerationType.AUTO con hibernate 5
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY, optional= false)
	private Usuario usuario;
	@Column(length=60)
	@Enumerated(EnumType.STRING)
	private Privilegio evento;
	private Timestamp creation_date;
	@Column(nullable = false)//todas las entidades que se sincronizan deben tener este dato obligatoriamente
	private Timestamp last_update_date;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Privilegio getEvento() {
		return evento;
	}
	public void setEvento(Privilegio evento) {
		this.evento = evento;
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
		if (!(obj instanceof Suscripcion))//lazy issue, comparar con instanceof
			return false;
		Suscripcion other = (Suscripcion) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}