package ar.com.signals.trading.eventos.repository;

import java.util.List;

import ar.com.signals.trading.eventos.domain.RegistroEvento;
import ar.com.signals.trading.eventos.domain.RegistroEvento.RegistroEventoTipo;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.util.repository.GenericDao;

public interface RegistroEventoDao extends GenericDao<RegistroEvento> {

	List<RegistroEvento> getPorMovimientoAndTipoEvento(String movimiento_id);

	boolean existeRegistroEvento(Privilegio evento, Long idRelacion, RegistroEventoTipo registroEventoTipo);
}