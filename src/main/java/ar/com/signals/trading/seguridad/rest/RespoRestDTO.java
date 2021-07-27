package ar.com.signals.trading.seguridad.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ar.com.signals.trading.seguridad.domain.RespoUsuario;
import ar.com.signals.trading.seguridad.support.Privilegio;

@Deprecated
public class RespoRestDTO {
	private Long id;
	private Timestamp creation_date;
	private Timestamp last_update_date;
	
	private List<Privilegio> privilegios = new ArrayList<Privilegio>();
		
	public RespoRestDTO() {}
	
	/**
	 * Usar en entorno Transaccional, debido a Usuario tiene entidades Lazy
	 * @param usuario
	 */
	public RespoRestDTO(RespoUsuario respoUsuario) {
		id = respoUsuario.getRespo().getId();
		creation_date = respoUsuario.getRespo().getCreation_date();
		last_update_date = respoUsuario.getRespo().getLast_update_date();
		
		for (Privilegio entidad : respoUsuario.getRespo().getPrivilegios()) {
			privilegios.add(entidad);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Privilegio> getPrivilegios() {
		return privilegios;
	}

	public void setPrivilegios(List<Privilegio> privilegios) {
		this.privilegios = privilegios;
	}
}
