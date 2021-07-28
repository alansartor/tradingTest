package ar.com.signals.trading.support;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.domain.RespoUsuario;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.service.RespoSrv;
import ar.com.signals.trading.seguridad.service.RespoUsuarioSrv;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.SeguridadUtil;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
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
	@Resource private RespoSrv respoSrv;
	@Resource private RespoUsuarioSrv respoUsuarioSrv;
	@Resource private TrueFXSrv trueFXSrv;
	@Resource private RegistroNotificacionSrv registroNotificacionSrv;
	@Resource(name = "tradingBotSrvImpl") private MyTelegramBotSrvImpl tradingBotSrvImpl;
	@Resource private EmailSrv emailSrv;
	private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
    //fixedRate : makes Spring run the task on periodic intervals even if the last invocation may still be running.
    //fixedDelay : specifically controls the next execution time when the last execution finishes.
	
	//private volatile long last_update_id = -1;//se usa en las llamadas a telegram, cada vez que recibimos updates de telegram, la proxima llamada tenemos que hacerla con un offset = last_update_id +1 para que telegram marque los updates como vistos y no los manda mas

	@Scheduled(initialDelay = 5000, fixedDelay=Long.MAX_VALUE)//al tener tanto tiempo nunca se va a volver a ejecutar
	public void onStartUp() {
		//creo un usuario admin de prueba
		//comentario de gitHub y eclipse (merge)
		Usuario usuario = new Usuario();
		usuario.setUsername("admin");
		usuario.setDescripcion("par apruebas");
		//usuario.setId(sujeto.getId());esto causa error, hace que hibernate haga un update en vez d eun insert del usuario
		usuario.setUser_enabled(true);
		usuario.setUser_email("pepe@hotmail.com");
		usuario.setCreation_date(new Timestamp(new Date().getTime()));
		usuario.setLast_update_date(usuario.getCreation_date());
		usuarioSrv.persistir(usuario, "admin");
		Respo respo = new Respo();
		//respo.setId(-1l);esto causa error, hace que hibernate haga un update en vez d eun insert del usuario
		respo.setCodigo("superUsuario");
		respo.setDescripcion("Super usuario que tiene acceso a la administracion general de Responsabilidades y Usuarios");
		respo.setTipo(TipoResponsabilidad.SuperUsuario);
		respo.setCreation_date(usuario.getCreation_date());
		respo.setLast_update_date(usuario.getCreation_date());
		respo.setActivo(true);
		List<Privilegio> privilegios = new ArrayList<>();
		privilegios.add(Privilegio.USUARIO_NUEVO);
		privilegios.add(Privilegio.USUARIO_EDITAR);
		privilegios.add(Privilegio.USUARIO_LISTAR);
		privilegios.add(Privilegio.RESPONSABILIDAD_NUEVO);
		privilegios.add(Privilegio.RESPONSABILIDAD_EDITAR);
		privilegios.add(Privilegio.RESPONSABILIDAD_LISTAR);
		privilegios.add(Privilegio.RESPONSABILIDAD_VER);
		privilegios.add(Privilegio.RESPOUSUARIO_NUEVO);
		privilegios.add(Privilegio.RESPOUSUARIO_EDITAR);
		privilegios.add(Privilegio.RESPOUSUARIO_LISTAR);
		privilegios.add(Privilegio.TRADING_GRAFICAR);
		privilegios.add(Privilegio.TRADING_GRAFICAR_2);
		respo.setPrivilegios(privilegios);//Arrays.asList(Privilegio.values()));//le doy todos los privilegios que existen!
		respoSrv.persistir(respo);
		RespoUsuario ru = new RespoUsuario();
		ru.setRespo(respo);
		//ru.setSujeto(sujeto);
		ru.setUsuario(usuario);
		ru.setCreation_date(usuario.getCreation_date());
		ru.setLast_update_date(usuario.getCreation_date());
		ru.setActivo(true);
		respoUsuarioSrv.persistir(ru);
	}
	
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
