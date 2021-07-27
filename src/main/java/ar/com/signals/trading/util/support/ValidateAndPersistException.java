package ar.com.signals.trading.util.support;

public class ValidateAndPersistException extends RuntimeException {
	public static final String ERROR_GENERAL_FIELD = "errores"; 
	private final String field;
	
	public ValidateAndPersistException(String field, String message) {
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
