package ar.com.signals.trading.util.support;

public class LogicaNoImplementadaException extends RuntimeException {

	public LogicaNoImplementadaException(String message) {
		super(message);
	}
	
	public LogicaNoImplementadaException(String message, Exception e) {
		super(message, e);
	}

	public LogicaNoImplementadaException(String message, Throwable e) {
		super(message, e);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
