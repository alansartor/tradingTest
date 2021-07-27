package ar.com.signals.trading.util.support;

import java.util.Arrays;
import java.util.List;

import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;

public class SeguridadException extends RuntimeException {
	private Usuario usuario;
	private Respo responsabilidad;
	private List<Privilegio> privilegios;
	
/*	public SeguridadException(String message) {
		super(message);
	}
	
	public SeguridadException(String message, Exception e) {
		super(message, e);
	}

	public SeguridadException(String message, Throwable e) {
		super(message, e);
	}*/
	
	public SeguridadException(Usuario usuario, Respo responsabilidad, Privilegio privilegio, String mensaje) {
		super("El Usuario " + usuario +" con Responsabilidad " + responsabilidad + " " + mensaje);
		this.usuario = usuario;
		this.responsabilidad = responsabilidad;
		this.privilegios = Arrays.asList(privilegio);
	}
	
	public SeguridadException(Usuario usuario, Respo responsabilidad, List<Privilegio> privilegios) {
		super("El Usuario " + usuario +" con Responsabilidad " + responsabilidad + " " + getTexto(privilegios));
		this.usuario = usuario;
		this.responsabilidad = responsabilidad;
		this.privilegios = privilegios;
	}
	
	private static String getTexto(List<Privilegio> privilegios) {
		if(privilegios == null || privilegios.isEmpty()) {
			return "";
		}
		String rta = "";
		for (Privilegio privilegio : privilegios) {
			rta += privilegio + " o ";
		}
		return rta.substring(0, rta.length() - 3);//saco ' o '
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Respo getResponsabilidad() {
		return responsabilidad;
	}

	public void setResponsabilidad(Respo responsabilidad) {
		this.responsabilidad = responsabilidad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Privilegio> getPrivilegios() {
		return privilegios;
	}

	public void setPrivilegios(List<Privilegio> privilegios) {
		this.privilegios = privilegios;
	}
	
}
