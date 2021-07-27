package ar.com.signals.trading.seguridad.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.domain.RespoUsuario;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.RespoUsuarioDTO;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.seguridad.support.Privilegio;

public interface RespoUsuarioSrv {

	List<ResponsabilidadDTO> getResponsabilidadesDTO(Usuario usuario);

	void persistir(RespoUsuario entidad);

	void validateAndPersist(RespoUsuarioDTO entidadDTO, BindingResult errors, boolean sinSucursal);

	Object obtenerDTO(Long id);

	List<RespoUsuarioDTO> getEntidades();
}
