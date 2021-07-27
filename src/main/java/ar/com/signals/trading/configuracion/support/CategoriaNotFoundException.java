package ar.com.signals.trading.configuracion.support;

public class CategoriaNotFoundException extends RuntimeException {

	public CategoriaNotFoundException(PropiedadCategoriaEnum propiedadCategoriaEnum) {
		super("Es necesario que cree o asigne un valor valido para la PropiedadCategoria: " + propiedadCategoriaEnum);
	}

	public CategoriaNotFoundException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
