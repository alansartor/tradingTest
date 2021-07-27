package ar.com.signals.trading.eventos.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ar.com.signals.trading.eventos.support.MetodoNotificacion;
import ar.com.signals.trading.seguridad.domain.Usuario;

/**
 * Registro adicional por cada usuario suscripto a evento, que se crea cada vez que se dispara un evento
 * Estas tablas se deben limpiar periodicamente!(cada un mes), la tabla es solo para saber si el usuario vio el evento, entonces una vez que lo ve, se elimina el registro
 * @author pepe@hotmail.com
 *
 */
@Entity
@IdClass(RegistroNotificacionPK.class)
public class RegistroNotificacion {	
	@Id
	@ManyToOne(optional=false)
	private RegistroEvento registroEvento;
	@Id
	@ManyToOne(optional=false)
	private Usuario usuario;
	
	@Column(nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private MetodoNotificacion metodoNotificacion;//si se informa por telegram, pero no lo marca como leido, entonces tambien lo informo como novedad en pantalla
	
	//hay que registrar si ya se trato de informar
	//y cuando el usuario informe como marcar visto, entonces lo eliminamos, o se elimina solo al mes
	//en telegram, cada vez que se informa algo, se manda opcion de enviar comando /marcar_visto, lo mismo que cuando se muestra por pantalla una notificacion, se agrega boton arcar visto
	@Column(nullable=true, columnDefinition = "DATETIME DEFAULT NULL")//IMPORTANTE: mysql no admite Timestamp nullable, entonces uso Date con esta configuracion
	//@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInforme;//esto indica la fecha que se informo por el metodo de notficacion del registro!

	private boolean visto;//una ves marcado como vistos, quedan disponibles para ser eliminados (puede que falle la notificacion por telegram, entonces queda sin informar por el metodo primario, pero el usuario ve el mensaje por pantalla y lo marca como visto, entonces queda marcado para eliminar y no se intenta enviar mas por telegram)

	public RegistroEvento getRegistroEvento() {
		return registroEvento;
	}
	public void setRegistroEvento(RegistroEvento registroEvento) {
		this.registroEvento = registroEvento;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public MetodoNotificacion getMetodoNotificacion() {
		return metodoNotificacion;
	}
	public void setMetodoNotificacion(MetodoNotificacion metodoNotificacion) {
		this.metodoNotificacion = metodoNotificacion;
	}
	public Date getFechaInforme() {
		return fechaInforme;
	}
	public void setFechaInforme(Date fechaInforme) {
		this.fechaInforme = fechaInforme;
	}
	public boolean isVisto() {
		return visto;
	}
	public void setVisto(boolean visto) {
		this.visto = visto;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((registroEvento == null) ? 0 : registroEvento.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RegistroNotificacion))
			return false;
		RegistroNotificacion other = (RegistroNotificacion) obj;
		if (registroEvento == null) {
			if (other.getRegistroEvento() != null)
				return false;
		} else if (!registroEvento.equals(other.getRegistroEvento()))
			return false;
		if (usuario == null) {
			if (other.getUsuario() != null)
				return false;
		} else if (!usuario.equals(other.getUsuario()))
			return false;
		return true;
	}
}
