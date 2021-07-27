package ar.com.signals.trading.seguridad.dto;

public class SubSeccionDTO{
	private Long id;
	private String subSeccionDescripcion;
	private String seccionDescripcion;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubSeccionDescripcion() {
		return subSeccionDescripcion;
	}
	public void setSubSeccionDescripcion(String subSeccionDescripcion) {
		this.subSeccionDescripcion = subSeccionDescripcion;
	}
	public String getSeccionDescripcion() {
		return seccionDescripcion;
	}
	public void setSeccionDescripcion(String seccionDescripcion) {
		this.seccionDescripcion = seccionDescripcion;
	}
	@Override
	public String toString() {
		return seccionDescripcion + " - " + subSeccionDescripcion;
	}
}
