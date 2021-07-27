package ar.com.signals.trading.eventos.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.signals.trading.eventos.domain.RegistroEvento;
import ar.com.signals.trading.eventos.domain.RegistroEvento.RegistroEventoTipo;
import ar.com.signals.trading.eventos.domain.RegistroNotificacion;
import ar.com.signals.trading.eventos.dto.RegistroNotificacionDTO;
import ar.com.signals.trading.eventos.repository.RegistroNotificacionDao;
import ar.com.signals.trading.eventos.support.MetodoNotificacion;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.Privilegio;

@Service
@Transactional
public class RegistroNotificacionSrvImpl implements RegistroNotificacionSrv{
	@Resource private RegistroNotificacionDao dao;
	@Resource private UsuarioSrv usuarioSrv;
	private Logger logger = LoggerFactory.getLogger(RegistroNotificacionSrv.class);
	
	@Override
	public void registrarNotificacionPendiente(RegistroEvento registroEvento, Usuario usuario, MetodoNotificacion metodoNotificacion) {
		RegistroNotificacion entidad = new RegistroNotificacion();
		entidad.setRegistroEvento(registroEvento);
		entidad.setUsuario(usuario);
		entidad.setMetodoNotificacion(metodoNotificacion);
		dao.guardar(entidad);
	}

	@Override
	public List<RegistroNotificacion> obtenerPendientesInformar(MetodoNotificacion metodoNotificacion) {
		return dao.obtenerPendientesInformar(metodoNotificacion);
	}

	@Override
	public void marcarInformado(RegistroNotificacion entidad) {
		if(dao.refreshOrEvict(entidad)) {
			entidad.setFechaInforme(new Date());
		}else {
			logger.warn("No se pudo marcar como informado el RegistroNotificacion id:"+entidad.getRegistroEvento().getId()+"_"+entidad.getUsuario().getUsername() + " porque fue eliminado en otro hilo");
		}
	}

	@Override
	public void borrarNotificacionesViejas(Date fechaHasta) {
		dao.borrarNotificacionesViejas(fechaHasta);
	}

	@Override
	public int marcarVistasNotificaciones(Long telegram_id) {
		//buscar las notificaciones pendientes de marcar como vistas del usuario y marcarlas
		//se deben marcar todas las notificaciones hasta la ultima informada
		Usuario usuario = usuarioSrv.getByTelegramId(telegram_id.toString());
		List<RegistroNotificacion> registros = dao.getInformadasSinMarcar(usuario, MetodoNotificacion.telegram);
		for (RegistroNotificacion registroNotificacion : registros) {
			registroNotificacion.setVisto(true);
		}
		return registros.size();
	}

	@Override
	public List<RegistroNotificacion> getPendientes(Privilegio evento, RegistroEventoTipo registroEventoTipo, Long idRelacion) {
		return dao.getPendientes(evento, registroEventoTipo, idRelacion);
	}

	@Override
	public void eliminar(RegistroNotificacion entidad) {
		dao.eliminar(entidad);
	}

	@Override
	public List<RegistroNotificacion> ultimasVistas(Usuario usuario, int top) {
		return dao.ultimasVistas(usuario, true, top);
	}

	@Override
	public List<RegistroNotificacion> obtenerNoVistas(Usuario usuario) {
		return dao.ultimasVistas(usuario, false, null);
	}

	@Override
	public void marcarVistasNotificaciones(Usuario usuario, List<Long> ids) {
		List<RegistroNotificacion> registros = dao.getByIds(usuario, ids);
		for (RegistroNotificacion registroNotificacion : registros) {
			registroNotificacion.setVisto(true);
			if(registroNotificacion.getFechaInforme() == null) {
				registroNotificacion.setFechaInforme(new Date());//si ya la vio por pantalla, no hace falta que se la envie por otro medio
			}
		}
	}

	@Override
	public List<RegistroNotificacionDTO> getRegistrosNotificacionesDTO(Usuario usuario) {
		return dao.getRegistrosNotificacionesDTO(usuario);
	}
}