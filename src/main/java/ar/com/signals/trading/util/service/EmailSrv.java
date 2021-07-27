package ar.com.signals.trading.util.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import ar.com.signals.trading.eventos.domain.RegistroEvento;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;

public interface EmailSrv {
	public void informarEvento(Privilegio evento, List<String> destinatarios, String asunto, String cuerpo, Object adjuntos, RegistroEvento registroEvento, Usuario usuarioGenerador) throws Exception;
	
	/**
	 * Envia un mail a los destinatarios, con los datos de los argumentos
	 * El metodo no se ejecuta asyncronicamente, ante cualquier error lo arroja
	 * @param destinatarios
	 * @param asunto
	 * @param texto
	 * @param adjuntos
	 */
	public void informarNoCatch(String from, String[] destinatarios, String[] destinatariosCC, String asunto, String texto, Object adjuntos) throws Exception;
		
	/**
	 * Metodo Asyncronico, NO ejecutar en bucles!
	 * Envia un mail a los destinatarios, con los datos de los argumentos
	 * @param destinatarios
	 * @param asunto
	 * @param texto
	 * @param adjuntos
	 */
	public void informar(String[] destinatarios, String asunto, String texto, List<String> adjuntos);

	/**
	 * Envia un mail a los destinatarios, y a los con copia, con los datos de los argumentos
	 * El metodo se ejecuta Asyncronicamente
	 * @param destinatarios
	 * @param destinatariosCC
	 * @param asunto
	 * @param texto
	 * @param adjuntos
	 */
	public void informar(String[] destinatarios, String[] destinatariosCC, String asunto, String texto, List<String> adjuntos);
	
	/**
	 * Envia un mail a los destinatarios, y a los con copia, con los datos de los argumentos
	 * El metodo se ejecuta Asyncronicamente
	 * @param destinatarios
	 * @param asunto
	 * @param texto
	 * @param adjuntos
	 */
	public void informarMap(String[] destinatarios, String asunto, String texto, Map<String, File> adjuntos);
	
	/**
	 * Envia un mail a los destinatarios, y a los con copia, con los datos de los argumentos
	 * El metodo se ejecuta Asyncronicamente
	 * @param destinatarios
	 * @param destinatariosCC
	 * @param asunto
	 * @param texto
	 * @param adjuntos
	 */
	public void informarMap(String[] destinatarios, String[] destinatariosCC, String asunto, String texto, Map<String, File> adjuntos);
	
	
	/**
	 * Envia un mail a los destinatarios, y a los con copia, con los datos de los argumentos, el correo sale de From
	 * El metodo se ejecuta Asyncronicamente
	 * @param destinatarios
	 * @param destinatariosCC
	 * @param asunto
	 * @param texto
	 * @param adjuntos
	 */
	public void informar(String from, String[] destinatarios, String[] destinatariosCC, String asunto, String texto, List<String> adjuntos);
	
}
