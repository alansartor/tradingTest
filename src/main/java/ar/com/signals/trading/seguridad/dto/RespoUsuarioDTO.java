package ar.com.signals.trading.seguridad.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import ar.com.signals.trading.seguridad.domain.RespoUsuario;

public class RespoUsuarioDTO {
	private Long id;
	

	private Long usuario_id;//es un autocomplete, pero que se puede poner un valor que no este listado, entonces pongo obligatorio que ponga algo, depsues verifico que sea un usuario valido
	@NotBlank
	private String usuario_username;//obligo a poner algo 
	
	@NotNull
	private Long respo_id;
	private String respo_codigo;
	
	private boolean activo;
	
	//Para listar
	private Timestamp creation_date;
	private Timestamp last_update_date;
	private String usuario_descripcion;
	
	public RespoUsuarioDTO() {}
	
	public RespoUsuarioDTO(RespoUsuario entidad) {
		id = entidad.getId();
		usuario_id = entidad.getUsuario().getId();
		usuario_username = entidad.getUsuario().getUsername();
		respo_id = entidad.getRespo().getId();
		respo_codigo = entidad.getRespo().toString();

		activo = entidad.isActivo();
	}
	
	public String getUsuario_username() {
		return usuario_username;
	}
	public void setUsuario_username(String usuario_username) {
		this.usuario_username = usuario_username;
	}
	public Long getRespo_id() {
		return respo_id;
	}
	public void setRespo_id(Long respo_id) {
		this.respo_id = respo_id;
	}

	public String getRespo_codigo() {
		return respo_codigo;
	}

	public void setRespo_codigo(String respo_codigo) {
		this.respo_codigo = respo_codigo;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}

	public String getUsuario_descripcion() {
		return usuario_descripcion;
	}

	public void setUsuario_descripcion(String usuario_descripcion) {
		this.usuario_descripcion = usuario_descripcion;
	}
}
