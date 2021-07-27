package ar.com.signals.trading.util.rest;

public class GenericAutocompleteDTO implements AutocompleteDTO{
	private Long id;
	private String value;
	
	public GenericAutocompleteDTO() {}
	
	public GenericAutocompleteDTO(Long id, String value) {
		super();
		this.id = id;
		this.value = value;
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
}
