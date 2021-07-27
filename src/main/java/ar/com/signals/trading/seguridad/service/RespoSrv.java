package ar.com.signals.trading.seguridad.service;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;

import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.MenuOption;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;

public interface RespoSrv {
	
	/**
	 * Devuelve todas las responsabilidades
	 * @return
	 */
	List<Respo> getResponsabilidades();

	List<ResponsabilidadDTO> getResponsabilidadesDTO(List<TipoResponsabilidad> tipos);

	void validateAndPersist(ResponsabilidadDTO entidadDTO, BindingResult errors, Usuario usuario);

	ResponsabilidadDTO obtenerDTO(Long id);

	/**
	 * Si idRespoPadre != null, entonces se limitan los privilegios a los de la responsabilidad padre 
	 * Si es una edicion, entonces se pasa el idRespoEdicion de la respo para buscar los niveles de edicion que tiene habilitada la responsabilidad
	 * @param id
	 * @return
	 */
	Map<Modulo, MenuOption> obtenerPrivilegiosDTO(Long idRespoPadre, Long idRespoEdicion);

	/**
	 * Se usa en la sincronizacion
	 * @param respo
	 */
	void actualizar(Respo entidad);

	Respo obtenerWithJoins(Long id);

	void persistir(Respo entidad);
}
