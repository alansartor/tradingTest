package ar.com.signals.trading.util.service;


/**
 * Clase que se va a utilizar para arrojar excepciones en los Servicios @Service
 * Hereda de RuntimeException por lo que genera un rollback
 * @author Administrador
 *
 */
public class ServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3132549288245817691L;

	public ServiceException(String s) {
		super(s);
	}

}
