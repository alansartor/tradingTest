package ar.com.signals.trading.util.support;

public class ErrorEsperadoException extends RuntimeException {

	public ErrorEsperadoException(String message) {
		super(message);
	}
	
	public ErrorEsperadoException(String message, Exception e) {
		super(message, e);
	}

	public ErrorEsperadoException(String message, Throwable e) {
		super(message, e);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
