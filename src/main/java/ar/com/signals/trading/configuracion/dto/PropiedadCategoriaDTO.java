package ar.com.signals.trading.configuracion.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;

public class PropiedadCategoriaDTO {
	private Long id;
	@NotNull
	private PropiedadCategoriaEnum codigo;
	private Long sujeto_id;
	private String sujeto_descripcion;
	private Long sucursal_id;
	private String sucursal_codigo;
	private Long unidadProductiva_id;
	private String unidadProductiva_codigo;
	private String valor;
	private boolean activo;
	
	//solo se usan en la jsp ver
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Timestamp creation_date;
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Timestamp last_update_date;
	
	@Override
	public String toString() {
		return codigo.name();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public PropiedadCategoriaEnum getCodigo() {
		return codigo;
	}
	public void setCodigo(PropiedadCategoriaEnum codigo) {
		this.codigo = codigo;
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
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public Timestamp getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Timestamp creation_date) {
		this.creation_date = creation_date;
	}
	public Timestamp getLast_update_date() {
		return last_update_date;
	}
	public void setLast_update_date(Timestamp last_update_date) {
		this.last_update_date = last_update_date;
	}
	
}