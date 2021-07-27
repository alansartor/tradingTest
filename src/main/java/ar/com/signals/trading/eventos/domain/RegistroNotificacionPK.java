package ar.com.signals.trading.eventos.domain;

import java.io.Serializable;

public class RegistroNotificacionPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long registroEvento;
	private Long usuario;
	
	public RegistroNotificacionPK() {
		super();
	}
	public RegistroNotificacionPK(Long registroEvento, Long usuario) {
		super();
		this.registroEvento = registroEvento;
		this.usuario = usuario;
	}

	public Long getRegistroEvento() {
		return registroEvento;
	}
	public void setRegistroEvento(Long registroEvento) {
		this.registroEvento = registroEvento;
	}
	public Long getUsuario() {
		return usuario;
	}
	public void setUsuario(Long usuario) {
		this.usuario = usuario;
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
		if (!(obj instanceof RegistroNotificacionPK))
			return false;
		RegistroNotificacionPK other = (RegistroNotificacionPK) obj;
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
