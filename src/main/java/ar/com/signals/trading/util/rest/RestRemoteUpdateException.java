package ar.com.signals.trading.util.rest;

public class RestRemoteUpdateException extends RuntimeException {
	private final String field;
	public RestRemoteUpdateException(String field, String message) {
		super(message);
		this.field = field;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getField() {
		return field;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
