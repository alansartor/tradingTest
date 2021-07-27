package ar.com.signals.trading.eventos.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import ar.com.signals.trading.seguridad.support.Privilegio;

public class RegistroNotificacionDTO {
	private Long evento_id;
	private Timestamp evento_fecha;
	private Privilegio evento;
	
	private Long sujeto_id;
	private String sujeto_descripcion;
	private Long sucursal_id;
	private String sucursal_codigo;
	
	private Long unidadProductiva_id;
	private String unidadProductiva_codigo;
	
	private String observaciones;

	public Long getEvento_id() {
		return evento_id;
	}

	public void setEvento_id(Long evento_id) {
		this.evento_id = evento_id;
	}

	public Timestamp getEvento_fecha() {
		return evento_fecha;
	}

	public void setEvento_fecha(Timestamp evento_fecha) {
		this.evento_fecha = evento_fecha;
	}

	public Privilegio getEvento() {
		return evento;
	}

	public void setEvento(Privilegio evento) {
		this.evento = evento;
	}

	public Long getSujeto_id() {
		return sujeto_id;
	}

	public void setSujeto_id(Long sujeto_id) {
		this.sujeto_id = sujeto_id;
	}

	public String getSujeto_descripcion() {
		return sujeto_descripcion;
	}

	public void setSujeto_descripcion(String sujeto_descripcion) {
		this.sujeto_descripcion = sujeto_descripcion;
	}

	public Long getSucursal_id() {
		return sucursal_id;
	}

	public void setSucursal_id(Long sucursal_id) {
		this.sucursal_id = sucursal_id;
	}

	public String getSucursal_codigo() {
		return sucursal_codigo;
	}

	public void setSucursal_codigo(String sucursal_codigo) {
		this.sucursal_codigo = sucursal_codigo;
	}

	public Long getUnidadProductiva_id() {
		return unidadProductiva_id;
	}

	public void setUnidadProductiva_id(Long unidadProductiva_id) {
		this.unidadProductiva_id = unidadProductiva_id;
	}

	public String getUnidadProductiva_codigo() {
		return unidadProductiva_codigo;
	}

	public void setUnidadProductiva_codigo(String unidadProductiva_codigo) {
		this.unidadProductiva_codigo = unidadProductiva_codigo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
