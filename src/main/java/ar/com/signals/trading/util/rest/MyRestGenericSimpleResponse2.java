package ar.com.signals.trading.util.rest;


/**
 * 
 * @author Pepe
 *
 */
public class MyRestGenericSimpleResponse2<E, C> {
	protected StatusCodes statusCode;
	protected String observaciones;
	
	protected String field;//se usa en los controller del MSP que terminan llamanda al MSC para actualizar alguna entidad, entonces si hay algun error, el MSC manda el field donde esta el error

	protected E entidadDTO;//en caso de poder realizar la accion requerida, entonces se devuelve el dto original, pero se completan los datos nuevos (ej: id, creation_date, last_update_date)
	
	protected C entidadAdicional;//en caso de haber una entidad relacionada a la principal que puede que no exista del otro lado, entonces la mando para persistirla (ejemplo sucursal.cliente)
	
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
	}
	public E getEntidadDTO() {
		return entidadDTO;
	}
	public void setEntidadDTO(E entidadDTO) {
		this.entidadDTO = entidadDTO;
	}
	public C getEntidadAdicional() {
		return entidadAdicional;
	}
	public void setEntidadAdicional(C entidadAdicional) {
		this.entidadAdicional = entidadAdicional;
	};
}
