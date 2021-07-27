package ar.com.signals.trading.util.support;

/**
 * Arrojar cuando detectamos una incosistencia de datos en la base.
 * El ejemplo mas comun es cuando vamos a buscar un dato que deberia existir uno solo
 * y no encontramos con varios!
 * @author Pepe
 *
 */
public class InconsistenciaDeDatosException extends RuntimeException {

	public InconsistenciaDeDatosException(String message) {
		super(message);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
