package ar.com.signals.trading.util.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class Mensaje {
	public static final String SUCCESS = "success";
	public static final String INFO = "info";
	public static final String WARNING = "warning";
	public static final String DANGER = "danger";
	
	private static final String ATRIBUTE_MENSAJES = "flashMensajes";
	private static final String ATRIBUTE_NOTIFICACIONES = "flashNotificaciones";
	
	private String mensaje;
	private String tipo;
	
	public static final String PlacementAlignLeft = "left";
	public static final String PlacementAlignCenter = "center";
	public static final String PlacementAlignRigth = "right";
	private int delay = 7000;//se usa en las notificaciones, esta expresado en milisegundos
	private String placementAlign = PlacementAlignCenter;//se usa en las notificaciones
	
	public Mensaje(String tipo, String mensaje){
		this.tipo = tipo;
		this.mensaje = mensaje;
	}
	
	public Mensaje(String tipo, int delay, String placementAlign, String mensaje){
		this.tipo = tipo;
		this.mensaje = mensaje;
		this.delay = delay;
		this.placementAlign = placementAlign;
	}
	
	/**
	 * Verifica que el model no tenga el Mensaje.ATRIBUTE_MENSAJES
	 * Si lo tiene entonces le agrega el mensaje que se manda como parametro
	 * y si no lo tiene, entonces agrega una nueva lista de mensajes con el mensaje nuevo al model
	 * @param model
	 * @param mensaje
	 */
	public static void agregarMensajesAlModel(Model model, Mensaje mensaje) {
		if(model instanceof RedirectAttributes) {
			if(((RedirectAttributes) model).getFlashAttributes().containsKey(Mensaje.ATRIBUTE_MENSAJES)){//Si existia atributo mensaje, se añaden los mensajes
				List<Mensaje> flashMensajes = (List<Mensaje>) ((RedirectAttributes) model).getFlashAttributes().get(Mensaje.ATRIBUTE_MENSAJES);
				flashMensajes.add(mensaje); 
			}else{
				List<Mensaje> mensajes = new ArrayList<Mensaje>();
				mensajes.add(mensaje); 
				((RedirectAttributes) model).addFlashAttribute(Mensaje.ATRIBUTE_MENSAJES, mensajes);
			}
		}else {
			if(model.containsAttribute(Mensaje.ATRIBUTE_MENSAJES)){//Si existia atributo mensaje, se añaden los mensajes
				List<Mensaje> flashMensajes = (List<Mensaje>) model.asMap().get(Mensaje.ATRIBUTE_MENSAJES);
				flashMensajes.add(mensaje); 
			}else{
				List<Mensaje> mensajes = new ArrayList<Mensaje>();
				mensajes.add(mensaje); 
				model.addAttribute(Mensaje.ATRIBUTE_MENSAJES, mensajes);
			}
		}
	}
	/**
	 * Verifica que el model no tenga el Mensaje.ATRIBUTE_MENSAJES
	 * Si lo tiene entonces le agrega los mensajes que se mandan como parametro
	 * y si no lo tiene, entonces agrega una la lista de mensajes al model
	 * @param model
	 * @param mensajes
	 */
	public static void agregarMensajesAlModel(Model model, List<Mensaje> mensajes) {
		if(mensajes == null || mensajes.isEmpty()) {
			return;
		}
		if(model instanceof RedirectAttributes) {
			if(((RedirectAttributes) model).getFlashAttributes().containsKey(Mensaje.ATRIBUTE_MENSAJES)){//Si existia atributo mensaje, se añaden los mensajes
				List<Mensaje> flashMensajes = (List<Mensaje>) ((RedirectAttributes) model).getFlashAttributes().get(Mensaje.ATRIBUTE_MENSAJES);
				flashMensajes.addAll(mensajes); 
			}else{
				((RedirectAttributes) model).addFlashAttribute(Mensaje.ATRIBUTE_MENSAJES, mensajes);
			}
		}else {
			if(model.containsAttribute(Mensaje.ATRIBUTE_MENSAJES)){//Si existia atributo mensaje, se añaden los mensajes
				List<Mensaje> flashMensajes = (List<Mensaje>) model.asMap().get(Mensaje.ATRIBUTE_MENSAJES);
				flashMensajes.addAll(mensajes); 
			}else{
				model.addAttribute(Mensaje.ATRIBUTE_MENSAJES, mensajes);
			}
		}
	}
	/**
	 * Verifica que el model no tenga el Mensaje.ATRIBUTE_NOTIFICACIONES
	 * Si lo tiene entonces le agrega los mensajes que se mandan como parametro
	 * y si no lo tiene, entonces agrega una la lista de mensajes al model
	 * @param model
	 * @param mensajes
	 */
	public static void agregarNotificacionesAlModel(Model model, List<Mensaje> mensajes) {
		if(mensajes == null || mensajes.isEmpty()) {
			return;
		}
		if(model instanceof RedirectAttributes) {
			if(((RedirectAttributes) model).getFlashAttributes().containsKey(Mensaje.ATRIBUTE_NOTIFICACIONES)){//Si existia atributo mensaje, se añaden los mensajes
				List<Mensaje> flashMensajes = (List<Mensaje>) ((RedirectAttributes) model).getFlashAttributes().get(Mensaje.ATRIBUTE_NOTIFICACIONES);
				flashMensajes.addAll(mensajes); 
			}else{
				((RedirectAttributes) model).addFlashAttribute(Mensaje.ATRIBUTE_NOTIFICACIONES, mensajes);
			}
		}else {
			if(model.containsAttribute(Mensaje.ATRIBUTE_NOTIFICACIONES)){//Si existia atributo mensaje, se añaden los mensajes
				List<Mensaje> flashMensajes = (List<Mensaje>) model.asMap().get(Mensaje.ATRIBUTE_NOTIFICACIONES);
				flashMensajes.addAll(mensajes); 
			}else{
				model.addAttribute(Mensaje.ATRIBUTE_NOTIFICACIONES, mensajes);
			}
		}
	}
	/**
	 * Verifica que el model no tenga el Mensaje.ATRIBUTE_NOTIFICACIONES
	 * Si lo tiene entonces le agrega el mensaje que se manda como parametro
	 * y si no lo tiene, entonces agrega una nueva lista de mensajes con el mensaje nuevo al model
	 * @param model
	 * @param mensaje
	 */
	public static void agregarNotificacionesAlModel(Model model, Mensaje mensaje) {
		if(model instanceof RedirectAttributes) {
			if(((RedirectAttributes) model).getFlashAttributes().containsKey(Mensaje.ATRIBUTE_NOTIFICACIONES)){//Si existia atributo mensaje, se añaden los mensajes
				List<Mensaje> flashMensajes = (List<Mensaje>) ((RedirectAttributes) model).getFlashAttributes().get(Mensaje.ATRIBUTE_NOTIFICACIONES);
				flashMensajes.add(mensaje); 
			}else{
				List<Mensaje> mensajes = new ArrayList<Mensaje>();
				mensajes.add(mensaje); 
				((RedirectAttributes) model).addFlashAttribute(Mensaje.ATRIBUTE_NOTIFICACIONES, mensajes);
			}
		}else {
			if(model.containsAttribute(Mensaje.ATRIBUTE_NOTIFICACIONES)){//Si existia atributo mensaje, se añaden los mensajes
				List<Mensaje> flashMensajes = (List<Mensaje>) model.asMap().get(Mensaje.ATRIBUTE_NOTIFICACIONES);
				flashMensajes.add(mensaje); 
			}else{
				List<Mensaje> mensajes = new ArrayList<Mensaje>();
				mensajes.add(mensaje); 
				model.addAttribute(Mensaje.ATRIBUTE_NOTIFICACIONES, mensajes);
			}
		}
	}

	public void addLine(String linea) {
		if(mensaje == null) {
			mensaje = linea;
		}else {
			mensaje += "<br />" + linea;
		}
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public String getPlacementAlign() {
		return placementAlign;
	}

	public void setPlacementAlign(String placementAlign) {
		this.placementAlign = placementAlign;
	}
}
