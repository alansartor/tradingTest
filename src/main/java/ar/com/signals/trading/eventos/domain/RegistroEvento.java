package ar.com.signals.trading.eventos.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ar.com.signals.trading.seguridad.support.Privilegio;

/**
 * Tabla donde se van registrando los eventos
 *
 */
@Entity
public class RegistroEvento {
	public enum RegistroEventoTipo {
		registrar_and_informar, //se registra el evento y se arman los registros para informar a los usuarios suscriptos
		registrar_and_cancelar_informe, //se usa cuando editaron una entidad que anularia el informe anterior, por ejemplo hicieron un monitoreo y pusieron que hay que aplicar productos, luego editaron y pusieron que no, entonces hay que cancelar los informes, o informar de dicha anulacion!
		registrar_sin_informar//por si hay registros que se deben guardar pero que no disparan informes
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//no usar mas  GenerationType.AUTO con hibernate 5
	private Long id;
	private Timestamp fecha;
	@Column(length=60, nullable=false)
	@Enumerated(EnumType.STRING)
	private Privilegio evento;
	@Column(nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private RegistroEventoTipo registroEventoTipo;

	@Column(nullable=true)
	private Long idRelacion;//dependiendo del evento, va a depender si es obligatorio el id relacionado y a que entidad se refiere
	
	@Column(length=600)
	private String observaciones;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getFecha() {
		return fecha;
	}
	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}
	public Privilegio getEvento() {
		return evento;
	}
	public void setEvento(Privilegio evento) {
		this.evento = evento;
	}

	public Long getIdRelacion() {
		return idRelacion;
	}
	public void setIdRelacion(Long idRelacion) {
		this.idRelacion = idRelacion;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public RegistroEventoTipo getRegistroEventoTipo() {
		return registroEventoTipo;
	}
	public void setRegistroEventoTipo(RegistroEventoTipo registroEventoTipo) {
		this.registroEventoTipo = registroEventoTipo;
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
		if (!(obj instanceof RegistroEvento))//lazy issue, comparar con instanceof
			return false;
		RegistroEvento other = (RegistroEvento) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}
