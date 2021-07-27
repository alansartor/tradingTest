package ar.com.signals.trading.util.support;

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.util.web.Mensaje;

public class RedireccionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Mensaje mensaje;
	private Modulo modulo;
	private Class<?> clase;
	private Accion accion;
	private String requestParams;
	
	public RedireccionException(Modulo modulo, Class<?> clase, Accion accion, String requestParams, Mensaje mensaje) {
		super("Se deberia redireccionar la pantalla automaticamente al Modulo:" + modulo +", Clase:"+clase.getSimpleName()+", Accion:"+accion + (requestParams!=null?" y requestParams:"+requestParams:"")+ ", en caso de no ser asi, comuniquece con el administrador");
		this.modulo = modulo;
		this.clase = clase;
		this.accion = accion;
		this.requestParams = requestParams;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public Class<?> getClase() {
		return clase;
	}

	public void setClase(Class<?> clase) {
		this.clase = clase;
	}
}
