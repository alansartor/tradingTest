package ar.com.signals.trading.eventos.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.com.signals.trading.eventos.domain.RegistroEvento.RegistroEventoTipo;
import ar.com.signals.trading.eventos.support.Patron;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.trading.support.TrueFXDivisas;

public interface RegistroEventoSrv {	
	void registrarEvento(Privilegio privilegio, RegistroEventoTipo registroEventoTipo, Long idRelacionado, String observacion);

	/**
	 * Metodo Asyncronico (se dispara en un hilo aparte de donde se llamo)
	 * Envia las notificaciones por telegram, no registra nada en la base de datos
	 * Si falla telegram, no se hace nada, no se vuelve a informar
	 * @param eventoTradingAlert
	 * @param observaciones
	 */
	void notificacionInmediata(Privilegio eventoTradingAlert, String observaciones);


	/**
	 * Metodo Asyncronico (se dispara en un hilo aparte de donde se llamo)
	 * Envia las notificaciones por telegram, no registra nada en la base de datos
	 * Si falla telegram, no se hace nada, no se vuelve a informar
	 * @param patronObservaciones
	 * @param patrones 
	 */
	void tradingAlertInmediata(TrueFXDivisas trueFXDivisas, Map<Patron, String> patronObservaciones, Set<Patron> patrones);
}
