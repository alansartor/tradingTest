package ar.com.signals.trading.util.rest;

public class GenericAutocompleteDTO3 implements AutocompleteDTO3{
	private Long id;
	private String value;
	private String descripcion;
	private String descripcion2;
	
	public GenericAutocompleteDTO3() {}
	
	public GenericAutocompleteDTO3(Long id, String value, String descripcion) {
		this.id = id;
		this.value = value;
		this.descripcion = descripcion;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescripcion() {
		return descripcion!=null?descripcion:"";
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion2() {
		return descripcion2!=null?descripcion2:"";
	}

	public void setDescripcion2(String descripcion2) {
		this.descripcion2 = descripcion2;
	}
}
