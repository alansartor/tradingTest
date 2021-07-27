package ar.com.signals.trading.eventos.repository;

import java.sql.Timestamp;
import java.util.List;

import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.util.repository.GenericDao;
import ar.com.signals.trading.eventos.domain.Suscripcion;
import ar.com.signals.trading.eventos.dto.SuscripcionDTO;

public interface SuscripcionDao extends GenericDao<Suscripcion> {

	Suscripcion getBySucursPepedTipoEvento(Usuario usuario, Privilegio evento);

	List<SuscripcionDTO> getSuscripcionesDTO(Usuario usuario);

	List<Usuario> getUsuariosSuscriptos(Privilegio evento);
}