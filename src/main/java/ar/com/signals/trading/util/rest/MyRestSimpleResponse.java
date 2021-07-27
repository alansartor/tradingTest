package ar.com.signals.trading.util.rest;


/**
 * 
 * @author Pepe
 *
 */
public class MyRestSimpleResponse {
	protected StatusCodes statusCode;
	protected String observaciones;
	
	protected String field;//se usa en los controller del MSP que terminan llamanda al MSC para actualizar alguna entidad, entonces si hay algun error, el MSC manda el field donde esta el error

	public StatusCodes getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCodes statusCode) {
		this.statusCode = statusCode;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	};
	
	
}
