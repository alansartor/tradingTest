package ar.com.signals.trading.seguridad.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;


public class UsuarioDTO {
	private Long id;
	@NotBlank
	private String username;
	private String password;
	@NotBlank
	private String descripcion;
	@Email
	private String user_email;
	private boolean user_enabled;
	
	//Para modificacion de password
	private String passwordNew;
	private String passwordNew2;
	
	private TokenTemporalDTO tokenTelegram;//se usa para suscribirse al bot del sistema en telegram
	
	//Para listar
	private Timestamp creation_date;
	private Timestamp last_update_date;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public boolean isUser_enabled() {
		return user_enabled;
	}
	public void setUser_enabled(boolean user_enabled) {
		this.user_enabled = user_enabled;
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
	public String getPasswordNew() {
		return passwordNew;
	}
	public void setPasswordNew(String passwordNew) {
		this.passwordNew = passwordNew;
	}
	public String getPasswordNew2() {
		return passwordNew2;
	}
	public void setPasswordNew2(String passwordNew2) {
		this.passwordNew2 = passwordNew2;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public TokenTemporalDTO getTokenTelegram() {
		return tokenTelegram;
	}
	public void setTokenTelegram(TokenTemporalDTO tokenTelegram) {
		this.tokenTelegram = tokenTelegram;
	}
}
