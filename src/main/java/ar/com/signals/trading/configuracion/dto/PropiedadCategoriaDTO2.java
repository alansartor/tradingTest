package ar.com.signals.trading.configuracion.dto;

public class PropiedadCategoriaDTO2 {
	private Long id;
	private String codigo;
	private String sujetoDescripcion;
	private String sucursalCodigo;
	private String unidadProductivaCodigo;
	private String valor;
	protected boolean activo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getSujetoDescripcion() {
		return sujetoDescripcion;
	}
	public void setSujetoDescripcion(String sujetoDescripcion) {
		this.sujetoDescripcion = sujetoDescripcion;
	}
	public String getSucursalCodigo() {
		return sucursalCodigo;
	}
	public void setSucursalCodigo(String sucursalCodigo) {
		this.sucursalCodigo = sucursalCodigo;
	}
	public String getUnidadProductivaCodigo() {
		return unidadProductivaCodigo;
	}
	public void setUnidadProductivaCodigo(String unidadProductivaCodigo) {
		this.unidadProductivaCodigo = unidadProductivaCodigo;
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
}