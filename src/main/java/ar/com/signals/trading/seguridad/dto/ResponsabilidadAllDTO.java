package ar.com.signals.trading.seguridad.dto;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.AutoPopulatingList;

public class ResponsabilidadAllDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private String id;
	
	private List<PrivilegioDTO> privilegios = new AutoPopulatingList<PrivilegioDTO>(PrivilegioDTO.class);

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PrivilegioDTO> getPrivilegios() {
		return privilegios;
	}

	public void setPrivilegios(List<PrivilegioDTO> privilegios) {
		this.privilegios = privilegios;
	}
}
