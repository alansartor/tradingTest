package ar.com.signals.trading.configuracion.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.support.Privilegio;

@Entity
//@Table(indexes = {@Index(columnList = "usuario_username", name = "respoUsuario_index")})
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"respo_id", "privilegio", "categoria_id"}))//Nombre de las columnas en la base de datos
public class RespoCategoria extends AbstractCategoria{
	@ManyToOne
	private Respo respo;
	@Column(length=60)
	@Enumerated(EnumType.STRING)
	private Privilegio privilegio;
	
	public Respo getRespo() {
		return respo;
	}
	public void setRespo(Respo respo) {
		this.respo = respo;
	}
	public Privilegio getPrivilegio() {
		return privilegio;
	}
	public void setPrivilegio(Privilegio privilegio) {
		this.privilegio = privilegio;
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
		if (!(obj instanceof RespoCategoria))//lazy issue, comparar con instanceof
			return false;
		RespoCategoria other = (RespoCategoria) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}
