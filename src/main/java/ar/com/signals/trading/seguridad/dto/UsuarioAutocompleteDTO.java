package ar.com.signals.trading.seguridad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ar.com.signals.trading.util.rest.AutocompleteDTO2;

@JsonIgnoreProperties({ "username", "user_nombre", "user_apellido" , "user_legajo"})
public class UsuarioAutocompleteDTO implements AutocompleteDTO2{
	private String username;
	private String user_nombre;
	private String user_apellido;
	private String user_legajo;
	
	@Override
	public String getValue() {
		return user_legajo + " - " + (user_apellido!= null?user_apellido + " ":"") + (user_nombre!= null?user_nombre:"");
	}
	@Override
	public String getId() {
		return username;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUser_nombre() {
		return user_nombre;
	}
	public void setUser_nombre(String user_nombre) {
		this.user_nombre = user_nombre;
	}
	public String getUser_apellido() {
		return user_apellido;
	}
	public void setUser_apellido(String user_apellido) {
		this.user_apellido = user_apellido;
	}
	public String getUser_legajo() {
		return user_legajo;
	}
	public void setUser_legajo(String user_legajo) {
		this.user_legajo = user_legajo;
	}
}
