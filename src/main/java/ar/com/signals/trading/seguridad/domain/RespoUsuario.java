package ar.com.signals.trading.seguridad.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author pepe@hotmail.com
 *
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"usuario_id", "respo_id"}))//Nombre de las columnas en la base de datos
public class RespoUsuario {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//no usar mas  GenerationType.AUTO con hibernate 5
	private Long id;
	//no se puede tener composite id con nulls values, entonces hacer un id, y luego un unique indice con las columnas!
	@ManyToOne
	private Usuario usuario;
	@ManyToOne
	private Respo respo;

	//no se necesita para mysql@Column(columnDefinition = "number(1) default 0")
	private boolean activo;
	private Timestamp creation_date;
	@Column(nullable = false)//todas las entidades que se sincronizan deben tener este dato obligatoriamente
	private Timestamp last_update_date;
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Respo getRespo() {
		return respo;
	}
	public void setRespo(Respo respo) {
		this.respo = respo;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
		if (!(obj instanceof RespoUsuario))//lazy issue, comparar con instanceof
			return false;
		RespoUsuario other = (RespoUsuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
