package ar.com.signals.trading.seguridad.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import ar.com.signals.trading.seguridad.support.Privilegio;

@Entity
public class Favorito {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//no usar mas  GenerationType.AUTO con hibernate 5
	private Long id;
	@ManyToOne
	private Respo respo;
	@Enumerated(EnumType.STRING)
	private Privilegio privilegio;
	private String argumentos;
	@Column(length=60)
	private String nombreExtendido;
	//no se necesita para mysql@Column(columnDefinition = "number(1) default 0")
	private boolean activo;
	private Timestamp creation_date;
	@Column(nullable = false)//todas las entidades que se sincronizan deben tener este dato obligatoriamente
	private Timestamp last_update_date;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Privilegio getPrivilegio() {
		return privilegio;
	}
	public void setPrivilegio(Privilegio privilegio) {
		this.privilegio = privilegio;
	}
	public String getArgumentos() {
		return argumentos;
	}
	public void setArgumentos(String argumentos) {
		this.argumentos = argumentos;
	}
	public String getNombreExtendido() {
		return nombreExtendido;
	}
	public void setNombreExtendido(String nombreExtendido) {
		this.nombreExtendido = nombreExtendido;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Usuario))//lazy issue, comparar con instanceof
			return false;
		Favorito other = (Favorito) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}