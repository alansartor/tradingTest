package ar.com.signals.trading.eventos.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import ar.com.signals.trading.eventos.domain.Suscripcion;
import ar.com.signals.trading.eventos.domain.SuscripcionConfig;
import ar.com.signals.trading.eventos.dto.SuscripcionDTO;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;

public interface SuscripcionSrv {

	void validateAndPersist(SuscripcionDTO entidadDTO, BindingResult errors, Usuario usuario);

	SuscripcionDTO eliminar(Usuario usuario, Long id);

	List<SuscripcionDTO> getSuscripcionesDTO(Usuario usuario);

	List<Usuario> getUsuariosSuscriptos(Privilegio evento);

	void eliminar(Usuario usuario, Privilegio evento);

	void guardar(Suscripcion entidad);
	
	Suscripcion getBySucursPepedTipoEvento(Usuario usuario, Privilegio evento);
}
