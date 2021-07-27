package ar.com.signals.trading.seguridad.repository;

import java.util.List;

import ar.com.signals.trading.seguridad.domain.RespoUsuario;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.RespoUsuarioDTO;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.util.repository.GenericDao;

public interface RespoUsuarioDao extends GenericDao<RespoUsuario> {

	/**
	 * Devuelve la responsabilidad de ese usuario, forza el join con plantas y con responsabilidad.privilegios
	 * @param usuario
	 * @param responsabilidadId
	 * @return
	 */
	//RespoUsuario obtener(Usuario usuario, String responsabilidadId);

	List<ResponsabilidadDTO> getResponsabilidadesDTO(Usuario usuario);

	boolean existe(Long usuario_id, Long respo_id, Long id);

	List<RespoUsuarioDTO> getEntidades();
}