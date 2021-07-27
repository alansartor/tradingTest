package ar.com.signals.trading.util.spring;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ar.com.signals.trading.configuracion.service.PropiedadCategoriaSrv;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.support.ScheduledTasks;
import ar.com.signals.trading.telegram.support.MyTelegramBotSrvImpl;
import ar.com.signals.trading.trading.support.CandleEstadisticasDTO;
import ar.com.signals.trading.trading.support.IEstadisticas;
import ar.com.signals.trading.trading.support.TipoAlertaTrading;
import ar.com.signals.trading.trading.support.TrueFXDivisas;
import ar.com.signals.trading.trading.support.TrueFXSrv;
import ar.com.signals.trading.trading.support.TrueFXSrvImpl;
import ar.com.signals.trading.trading.support.TrueFxPreciosData;
import ar.com.signals.trading.util.web.ChartCandlePointDTO;
import ar.com.signals.trading.util.web.FechaUtil;

/**
 * Starting with Spring 4.2+ you can use the @EventListener annotation to observe the ContextRefreshedEvent
 * Esto se llama una vez que arranco la aplicacion y se cargo todo el context
 * @author pepe@hotmail.com
 *
 */
@Component
public class StartupTrading {
	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	@Resource private TrueFXSrv trueFXSrv;
	@Resource(name = "tradingBotSrvImpl") private MyTelegramBotSrvImpl tradingBotSrvImpl;
	private Logger logger = LoggerFactory.getLogger(StartupTrading.class);
	
	private TelegramBotsApi botsApi = null;
	
	@EventListener(ContextRefreshedEvent.class)
	public void contextRefreshedEvent() {//IMPORTANTE: por lo que vi se llama 2 veces, entonces tener cuidado en las inicializaciones de porner algun control
		if(botsApi == null) {//por las dudas para no generar llamadas duplicadas al server de Telegram
			if(propiedadCategoriaSrv.getBoolean(PropiedadCategoriaEnum.PROPIEDAD_INICIAR_TELEGRAM)) {//por si algun dia hay que parar telegram, esto requiere reiniciar tomcat
				logger.info("Iniciando conexion con Telegram");
				String token = propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_TOKEN_TELEGRAM);
				String botname = propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_BOTNAME_TELEGRAM);
				tradingBotSrvImpl.setBotToken(token);
				tradingBotSrvImpl.setBotUsername(botname);
				
			    //Para inicializar cosas a mano cuando arranca la aplicacion
				//ApiContextInitializer.init();si lo ejecutamos antes que se cree MyTelegramBotSrvImpl, entonces aparece error:No implementation for org.telegram.telegrambots.meta.generics.BotSession was bound.
				//solucion: llamar a ApiContextInitializer.init() antes de crear MyTelegramBotSrvImpl (lo puse en el constructor static de esa clase!)
		        botsApi = new TelegramBotsApi();
		        try {
		            botsApi.registerBot(tradingBotSrvImpl);//esto inicia un hilo infinito que llama a telegram, los uodates se pasan a tradingBotSrvImpl 
		        } catch (TelegramApiException e) {//no pasa nunca
		            e.printStackTrace();
		        }
			}else {
				logger.info("La conexion con Telegram esta deshabilitada, no se inicia");
			}
		}
		//Llamo a la logica para que arranque el hilo de busqueda de datos en la pagina de truefx
		//solo arranco si es dia entre semana: entre el domingo a las 19:00 y el viernes a las 18:00, esos son los horarios que truefx devuelve datos
		Date fechaActual = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaActual);
		int dayOfWeek =  cal.get(Calendar.DAY_OF_WEEK);//number ranges from 1 (Sunday) to 7 (Saturday)
		if(dayOfWeek > 1 && dayOfWeek < 6) {//de lunes a jueves arrancar nomas
			trueFXSrv.arrancarHilo();
		}else if(dayOfWeek == 1) {//si es domingo, entonces solo arranco si ya pasaron las 19:00
			int hourOfDay =  cal.get(Calendar.HOUR_OF_DAY);
			if(hourOfDay >= 19) {
				trueFXSrv.arrancarHilo();
			}
		}else if(dayOfWeek == 6) {//si es viernes, solo arranco si todavia no son las 18:00
			int hourOfDay =  cal.get(Calendar.HOUR_OF_DAY);
			if(hourOfDay < 18) {
				trueFXSrv.arrancarHilo();
			}
		}
	}
}
