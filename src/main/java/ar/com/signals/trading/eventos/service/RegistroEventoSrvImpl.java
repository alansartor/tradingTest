package ar.com.signals.trading.eventos.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import ar.com.signals.trading.eventos.domain.RegistroEvento;
import ar.com.signals.trading.eventos.domain.RegistroEvento.RegistroEventoTipo;
import ar.com.signals.trading.eventos.domain.RegistroNotificacion;
import ar.com.signals.trading.eventos.domain.SuscripcionConfig;
import ar.com.signals.trading.eventos.repository.RegistroEventoDao;
import ar.com.signals.trading.eventos.support.MetodoNotificacion;
import ar.com.signals.trading.eventos.support.Patron;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.telegram.support.MyTelegramBotSrvImpl;
import ar.com.signals.trading.telegram.support.TelegramUtil;
import ar.com.signals.trading.trading.support.TrueFXDivisas;
import ar.com.signals.trading.util.web.FechaUtil;

@Service
@Transactional
public class RegistroEventoSrvImpl implements RegistroEventoSrv{
	@Resource private RegistroEventoDao dao;
	@Resource private SuscripcionSrv suscripcionSrv;
	@Resource private SuscripcionConfigSrv suscripcionConfigSrv;
	@Resource private RegistroNotificacionSrv registroNotificacionSrv;
	@Resource(name = "tradingBotSrvImpl") private MyTelegramBotSrvImpl tradingBotSrvImpl;
	private Logger logger = LoggerFactory.getLogger(RegistroEventoSrv.class);
	
	@Override
	public void registrarEvento(Privilegio evento, RegistroEventoTipo registroEventoTipo, Long idRelacion, String observaciones) {
		if(idRelacion != null) {
			//Si ya existe el registro, entonces no hago nada
			if(dao.existeRegistroEvento(evento, idRelacion, registroEventoTipo)) {
				logger.warn("Se ignora evento: " + evento + ", tipo: "+ registroEventoTipo + " y idRelacion: "+idRelacion + " debido a que ya existe en la base");
				return;
			}
		}
		RegistroEvento entidad = new RegistroEvento();
		entidad.setFecha(new Timestamp(new Date().getTime()));
		entidad.setEvento(evento);
		entidad.setRegistroEventoTipo(registroEventoTipo);
		entidad.setIdRelacion(idRelacion);
		entidad.setObservaciones(StringUtils.right(observaciones, 600));
		dao.guardar(entidad);
		
		if(RegistroEventoTipo.registrar_and_informar.equals(registroEventoTipo)) {
			//por cada usuario suscripto al evento, genero un registro de pendiente de aviso
			List<Usuario> usuarios = suscripcionSrv.getUsuariosSuscriptos(evento);
			for (Usuario usuario : usuarios) {
				if(usuario.getTelegram_id() != null) {
					registroNotificacionSrv.registrarNotificacionPendiente(entidad, usuario, MetodoNotificacion.telegram);
				}else if(StringUtils.isNotEmpty(usuario.getUser_email())) {
					registroNotificacionSrv.registrarNotificacionPendiente(entidad, usuario, MetodoNotificacion.email);
				}else {
					registroNotificacionSrv.registrarNotificacionPendiente(entidad, usuario, MetodoNotificacion.pantalla);
				}
			}
		}else if(RegistroEventoTipo.registrar_and_cancelar_informe.equals(registroEventoTipo)) {
			//por cada usuario suscripto al evento, genero un registro de pendiente de aviso
			List<Usuario> usuarios = suscripcionSrv.getUsuariosSuscriptos(evento);
			Set<Usuario> usuariosNoInformar = new HashSet<>();
			if(idRelacion != null) {
				//Verifico si el Evento relacionado ya fue enviado, caso positivo entonces informo ques se cancelo, caso negativo elimino la notificacion
				List<RegistroNotificacion> pendientesInformar = registroNotificacionSrv.getPendientes(evento, RegistroEventoTipo.registrar_and_informar, idRelacion);
				for (RegistroNotificacion registroNotificacion : pendientesInformar) {
					usuariosNoInformar.add(registroNotificacion.getUsuario());
					registroNotificacionSrv.eliminar(registroNotificacion);
				}
			}
			for (Usuario usuario : usuarios) {
				if(!usuariosNoInformar.contains(usuario)) {
					if(usuario.getTelegram_id() != null) {
						registroNotificacionSrv.registrarNotificacionPendiente(entidad, usuario, MetodoNotificacion.telegram);
					}else if(StringUtils.isNotEmpty(usuario.getUser_email())) {
						registroNotificacionSrv.registrarNotificacionPendiente(entidad, usuario, MetodoNotificacion.email);
					}else {
						registroNotificacionSrv.registrarNotificacionPendiente(entidad, usuario, MetodoNotificacion.pantalla);
					}
				}
			}
		}
	}

	@Async
	@Override
	public void notificacionInmediata(Privilegio evento, String observaciones) {
		List<Usuario> usuarios = suscripcionSrv.getUsuariosSuscriptos(evento);
		try {
			for (Usuario usuario : usuarios) {
				if(usuario.getTelegram_id() != null) {
					SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
			                .setChatId(usuario.getTelegram_id())
			                .setParseMode(ParseMode.HTML)//IMPORTANTE: ojo con lo que queremos enviar, porque telegram va a tirar error si ponemos un tag que no reconoce!
			                .disableWebPagePreview()//por ahora lo deshabilito
			                .setText(TelegramUtil.limpiarHtmlText(observaciones));
					tradingBotSrvImpl.execute(message); // Call method to send the message
				}
			}
		} catch (Exception e) {
			logger.error("Fallo notificacion inmediata telegram evento " + evento + ". Error: " + e.getMessage());
		}

	}
	
	@Async
	@Override
	public void tradingAlertInmediata(TrueFXDivisas trueFXDivisas, Map<Patron, String> patronObservaciones, Set<Patron> patrones) {
		logger.info("Alerta en " + trueFXDivisas.name() + ", se dispara las notificaciones");
		Date fechaActual = new Date();
		Date fechaInicio = DateUtils.truncate(fechaActual, Calendar.DATE);
		long minutos = FechaUtil.diferencia(fechaActual, fechaInicio, Calendar.MINUTE);
		
		List<SuscripcionConfig> suscripciones =  suscripcionConfigSrv.getTradingAlertSuscript(minutos);
		//List<Usuario> usuarios = suscripcionSrv.getUsuariosSuscriptos(evento);
		try {
			for (SuscripcionConfig suscripcionConfig : suscripciones) {
				if(suscripcionConfig.getUsuario().getTelegram_id() != null && suscripcionConfig.divisaSeleccionada(trueFXDivisas) && patrones.contains(suscripcionConfig.getPatron())) {
					SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
			                .setChatId(suscripcionConfig.getUsuario().getTelegram_id())
			                .setParseMode(ParseMode.HTML)//IMPORTANTE: ojo con lo que queremos enviar, porque telegram va a tirar error si ponemos un tag que no reconoce!
			                .disableWebPagePreview()//por ahora lo deshabilito
			                .setText(TelegramUtil.limpiarHtmlText(patronObservaciones.get(suscripcionConfig.getPatron())));
					tradingBotSrvImpl.execute(message); // Call method to send the message
				}
			}
		} catch (Exception e) {
			logger.error("Fallo notificacion inmediata telegram evento " + Privilegio.EVENTO_TRADING_ALERT + " de la divisa " + trueFXDivisas + ". Error: " + e.getMessage());
		}

	}
}