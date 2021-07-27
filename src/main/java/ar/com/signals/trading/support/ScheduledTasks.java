package ar.com.signals.trading.support;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import ar.com.signals.trading.configuracion.service.PropiedadCategoriaSrv;
import ar.com.signals.trading.eventos.domain.RegistroNotificacion;
import ar.com.signals.trading.eventos.service.RegistroNotificacionSrv;
import ar.com.signals.trading.eventos.support.MetodoNotificacion;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.SeguridadUtil;
import ar.com.signals.trading.telegram.support.MyTelegramBotSrvImpl;
import ar.com.signals.trading.telegram.support.TelegramUtil;
import ar.com.signals.trading.trading.support.TrueFXSrv;
import ar.com.signals.trading.util.service.EmailSrv;

/**
 * Tareas que se deben ejecutar periodicamente
 * @author Pepe
 *
 */
@Component
public class ScheduledTasks {
	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	
	@Resource private UsuarioSrv usuarioSrv;
	@Resource private TrueFXSrv trueFXSrv;
	@Resource private RegistroNotificacionSrv registroNotificacionSrv;
	@Resource(name = "tradingBotSrvImpl") private MyTelegramBotSrvImpl tradingBotSrvImpl;
	@Resource private EmailSrv emailSrv;
	private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
    //fixedRate : makes Spring run the task on periodic intervals even if the last invocation may still be running.
    //fixedDelay : specifically controls the next execution time when the last execution finishes.
	
	//private volatile long last_update_id = -1;//se usa en las llamadas a telegram, cada vez que recibimos updates de telegram, la proxima llamada tenemos que hacerla con un offset = last_update_id +1 para que telegram marque los updates como vistos y no los manda mas

	crear usuario!
	
	@Scheduled(cron = "0 0 3 * * *")//todos los dias a las 3:00 am
	public void doTheCleaning() {
		logger.info("Inicio Limpieza");
		SeguridadUtil.limpiarTokens();//se borran los token vencidos
		registroNotificacionSrv.borrarNotificacionesViejas(DateUtils.addMonths(new Date(), -6));
		logger.info("Fin Limpieza");
	}
	
	@Scheduled(cron = "0 0 19 * * SUN")//todos los domingos a las 19:00 (from 5 p.m. EST on Sunday)
	public void arrancarHilo() {
		logger.info("Arranca Jornada Divisas, arranco el hilo");
		trueFXSrv.arrancarHilo();
	}
	
	@Scheduled(cron = "0 0 18 * * FRI")//todos los viernes a las 18:00 (until 4 p.m. EST on Friday)
	public void pararHilo() {
		logger.info("Finaliza Jornada, detengo el hilo");
		trueFXSrv.detenerHilo();
	}
	
	/**
	 * Metodo que envia reportes, por ahora solo se informa por telegram, ver si informar via mails en algun momento
	 */
	@Scheduled(initialDelay=120000, fixedDelay=120000)
	public void notificacionesInformar() {
		logger.info("Inicio Notificaciones");
		//Telegram
		List<RegistroNotificacion> notificacionesPendientes = registroNotificacionSrv.obtenerPendientesInformar(MetodoNotificacion.telegram);
		long contador = 0;
		try {
			for (RegistroNotificacion registroNotificacion : notificacionesPendientes) {
				if(registroNotificacion.getUsuario().getTelegram_id()!=null) {
					String fecha = "Fecha: " + DateFormatUtils.format(registroNotificacion.getRegistroEvento().getFecha(), "dd/MM/yyyy HH:mm") + "<br>";
					SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
				                .setChatId(registroNotificacion.getUsuario().getTelegram_id())
				                .setParseMode(ParseMode.HTML)//IMPORTANTE: ojo con lo que queremos enviar, porque telegram va a tirar error si ponemos un tag que no reconoce!
				                .disableWebPagePreview()//por ahora lo deshabilito
				                .setText(TelegramUtil.limpiarHtmlText(fecha + registroNotificacion.getRegistroEvento().getObservaciones() + "<br>(Para marcar mensaje como visto envie: /marcar_visto)"));
					tradingBotSrvImpl.execute(message); // Call method to send the message
					 registroNotificacionSrv.marcarInformado(registroNotificacion);
					 ++contador;
				}else {
					//creo que no se deberia dar, pero si se da, marco como informado el mensaje (se podria dar si el usuario borra su telegram siendo que habia notificaciones pendientes)
					registroNotificacionSrv.marcarInformado(registroNotificacion);
				}
			}
		} catch (TelegramApiRequestException te) {
			logger.error("Fallaron los informes de notificacion por telegram, se alcanzaron a enviar " + contador + " de "+ notificacionesPendientes.size() + ". " + te.toString());
		} catch (Exception e) {
			logger.error("Fallaron los informes de notificacion por telegram, se alcanzaron a enviar " + contador + " de "+ notificacionesPendientes.size() + ". " + e.getMessage());
		}
		//Mail
		notificacionesPendientes = registroNotificacionSrv.obtenerPendientesInformar(MetodoNotificacion.email);
		contador = 0;
		try {
			for (RegistroNotificacion registroNotificacion : notificacionesPendientes) {
				if(registroNotificacion.getUsuario().getUser_email()!=null) {
					String cuerpo = registroNotificacion.getRegistroEvento().getObservaciones();
					emailSrv.informarEvento(registroNotificacion.getRegistroEvento().getEvento(), Arrays.asList(registroNotificacion.getUsuario().getUser_email()), null, cuerpo, null, registroNotificacion.getRegistroEvento(), null);
					registroNotificacionSrv.marcarInformado(registroNotificacion);
					//Marco como visto? Doy alguna forma de marcar como visto?
					++contador;
				}else {
					//creo que no se deberia dar, pero si se da, marco como informado el mensaje (se podria dar si el usuario borra su mail siendo que habia notificaciones pendientes)
					registroNotificacionSrv.marcarInformado(registroNotificacion);
				}
			}
		} catch (Exception e) {
			logger.error("Fallaron los informes de notificacion por email, se alcanzaron a enviar " + contador + " de "+ notificacionesPendientes.size() + ". " + e.getMessage());
		}
	}
}
