package ar.com.signals.trading.eventos.service;

import java.util.Date;
import java.util.List;

import ar.com.signals.trading.eventos.domain.RegistroEvento;
import ar.com.signals.trading.eventos.domain.RegistroEvento.RegistroEventoTipo;
import ar.com.signals.trading.eventos.domain.RegistroNotificacion;
import ar.com.signals.trading.eventos.dto.RegistroNotificacionDTO;
import ar.com.signals.trading.eventos.support.MetodoNotificacion;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;

public interface RegistroNotificacionSrv {

	void registrarNotificacionPendiente(RegistroEvento registroEvento, Usuario usuario, MetodoNotificacion metodoNotificacion);

	List<RegistroNotificacion> obtenerPendientesInformar(MetodoNotificacion metodoNotificacion);

	void marcarInformado(RegistroNotificacion entidad);

	void borrarNotificacionesViejas(Date addMonths);

	/**
	 * Marcar vista hasta la ultima notificacion de telegram informada
	 * @param telegram_id
	 * @return
	 */
	int marcarVistasNotificaciones(Long telegram_id);

	/**
	 * Devuelve las Notificacion que no fueron informadas que coincidan con los filtros, todos son obligatorios y idRelacion no puede ser null
	 * @param evento
	 * @param registroEventoTipo
	 * @param idRelacion
	 * @return
	 */
	List<RegistroNotificacion> getPendientes(Privilegio evento, RegistroEventoTipo registroEventoTipo, Long idRelacion);

	void eliminar(RegistroNotificacion entidad);

	List<RegistroNotificacion> ultimasVistas(Usuario usuario, int top);

	/**
	 * Devuelve las notificaciones que no estan marcadas como vistas
	 * @param usuario
	 * @return
	 */
	List<RegistroNotificacion> obtenerNoVistas(Usuario usuario);

	/**
	 * Marcar como vistas las notificaciones con esos ids
	 * @param usuario
	 * @param ids
	 */
	void marcarVistasNotificaciones(Usuario usuario, List<Long> ids);

	List<RegistroNotificacionDTO> getRegistrosNotificacionesDTO(Usuario usuario);	

}
