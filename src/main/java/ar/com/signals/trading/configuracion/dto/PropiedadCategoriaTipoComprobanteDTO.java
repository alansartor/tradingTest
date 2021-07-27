package ar.com.signals.trading.configuracion.dto;

import javax.validation.constraints.NotNull;

import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;

public class PropiedadCategoriaTipoComprobanteDTO{
	private Long id;
	@NotNull
	private PropiedadCategoriaEnum codigo;
	@NotNull
	private Integer tipoComprobante_id;
	
	//para listar
	private String propiedad_codigo;
	private String tipoComprobante_descripcion;
	
	
	public String getDescripcionCompleta() {
		return propiedad_codigo + " " + tipoComprobante_descripcion;
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
	public Integer getTipoComprobante_id() {
		return tipoComprobante_id;
	}
	public void setTipoComprobante_id(Integer tipoComprobante_id) {
		this.tipoComprobante_id = tipoComprobante_id;
	}
	public String getPropiedad_codigo() {
		return propiedad_codigo;
	}
	public void setPropiedad_codigo(String propiedad_codigo) {
		this.propiedad_codigo = propiedad_codigo;
	}
	public String getTipoComprobante_descripcion() {
		return tipoComprobante_descripcion;
	}
	public void setTipoComprobante_descripcion(String tipoComprobante_descripcion) {
		this.tipoComprobante_descripcion = tipoComprobante_descripcion;
	}
	
}