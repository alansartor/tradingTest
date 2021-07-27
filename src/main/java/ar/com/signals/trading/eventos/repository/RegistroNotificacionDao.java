package ar.com.signals.trading.eventos.repository;

import java.util.Date;
import java.util.List;

import ar.com.signals.trading.eventos.domain.RegistroEvento;
import ar.com.signals.trading.eventos.domain.RegistroEvento.RegistroEventoTipo;
import ar.com.signals.trading.eventos.domain.RegistroNotificacion;
import ar.com.signals.trading.eventos.dto.RegistroNotificacionDTO;
import ar.com.signals.trading.eventos.support.MetodoNotificacion;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.util.repository.GenericDao;

public interface RegistroNotificacionDao extends GenericDao<RegistroNotificacion> {

	List<RegistroNotificacion> obtenerPendientesInformar(MetodoNotificacion metodoNotificacion);

	void borrarNotificacionesViejas(Date fechaHasta);

	List<RegistroNotificacion> getInformadasSinMarcar(Usuario usuario, MetodoNotificacion metodoNotificacion);

	List<RegistroNotificacion> getPendientes(Privilegio evento, RegistroEventoTipo registroEventoTipo, Long idRelacion);

	List<RegistroNotificacion> ultimasVistas(Usuario usuario, boolean vistas, Integer top);

	List<RegistroNotificacion> getByIds(Usuario usuario, List<Long> ids);

	List<RegistroNotificacionDTO> getRegistrosNotificacionesDTO(Usuario usuario);

}