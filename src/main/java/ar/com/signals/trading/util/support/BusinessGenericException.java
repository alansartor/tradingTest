package ar.com.signals.trading.util.support;

public class BusinessGenericException extends RuntimeException {

	public BusinessGenericException(String message) {
		super(message);
	}
	
	public BusinessGenericException(String message, Exception e) {
		super(message, e);
	}

	public BusinessGenericException(String message, Throwable e) {
		super(message, e);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
