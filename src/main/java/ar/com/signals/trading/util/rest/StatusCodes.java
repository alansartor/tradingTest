package ar.com.signals.trading.util.rest;

/**
 * 
 * @author pepe@hotmail.com
 *
 */
public enum StatusCodes {
	OK(200, "Respuesta exitosa"),
	ERROR_CLIENTE(400, "Error en el request realizado"),
	ERROR_SERVIDOR(500, "Error interno del servidor");
	
	private int code;
	private String mensaje;
	
	private StatusCodes(int code, String mensaje) {
		this.code = code;
		this.mensaje = mensaje;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}	
}
