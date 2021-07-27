package ar.com.signals.trading.util.rest;

import java.math.BigDecimal;

public class GenericAutocompleteDTO4 implements AutocompleteDTO4{
	private Long id;
	private String value;
	private String descripcion;
	private String descripcion2;
	private BigDecimal value2;
	
	public GenericAutocompleteDTO4() {}
	
	public GenericAutocompleteDTO4(Long id, String value, String descripcion) {
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
	@Override
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Override
	public BigDecimal getValue2() {
		return value2;
	}

	public void setValue2(BigDecimal value2) {
		this.value2 = value2;
	}
	@Override
	public String getDescripcion2() {
		return descripcion2==null?"":descripcion2;
	}

	public void setDescripcion2(String descripcion2) {
		this.descripcion2 = descripcion2;
	}
}
