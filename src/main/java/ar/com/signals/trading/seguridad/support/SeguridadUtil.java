package ar.com.signals.trading.seguridad.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.GrantedAuthority;

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.TokenTemporalDTO;
import ar.com.signals.trading.util.support.SeguridadException;

public class SeguridadUtil {
	/**
	 * Map donde se registran los token temporales
	 * Por ahora solo se usan para asociar usuario con cuenta de telegram (mandan el token al bot del sistema de telegram)
	 * Hay un crontab que limpia los token vencidos una vez por dia
	 */
	private static Map<Long, TokenTemporalDTO> mapToken = new HashMap<Long, TokenTemporalDTO>();
	
	/**
	 * Verifica si el usuario tiene ese privilegio asignado. 
	 * Solo se verifica sobre los privilegios de la responsabilidad actual que selecciono
	 * @param usuario
	 * @param privilegio
	 * @return
	 */
	public static boolean tieneAutorizacion(Usuario usuario, List<Privilegio> privilegios) {
		Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();
		if (authorities == null) {
			return false;
		}
		for (Privilegio privilegio : privilegios) {
			if(authorities.contains(privilegio)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean tieneAutorizacion(Usuario usuario, Privilegio privilegio) {
		return tieneAutorizacion(usuario, Arrays.asList(privilegio));
	}
	


	
	/**
	 * Verifica si el usuario tiene ese privilegio asignado. 
	 * Solo se verifica sobre los privilegios de la responsabilidad actual que selecciono
	 * Si no tiene autorizacion, entonces se arroja una SeguridadException
	 * @param usuario
	 * @param privilegio
	 * @return
	 */
	public static void validarAutorizacion(Usuario usuario, List<Privilegio> privilegios) throws SeguridadException{
		if(!tieneAutorizacion(usuario, privilegios)) {
			throw new SeguridadException(usuario, usuario.getResponsabilidad(), privilegios);
		}
	}
	
	public static void validarAutorizacion(Usuario usuario, Privilegio privilegio) throws SeguridadException{
		validarAutorizacion(usuario, Arrays.asList(privilegio));
	}


	

	

	
	/**
	 * Esta validacion no controla si la responsabilidad del usuario es de tipo UsuarioMaestro, ya que no puede comparar el sujeto del usuario con el sujeto de la sucursal!
	 * @param usuario
	 * @param sucursal_id
	 * @param entidad_id
	 * @param contextPath
	 * @param acciones
	 * @return
	 */
	public static String getLinkAcciones(Usuario usuario, Long entidad_id, String contextPath, List<Privilegio> acciones) {
		String links = "";
		for (Privilegio p : acciones) {
			if(tieneAutorizacion(usuario, p) ) {
				links += " <a href='" + contextPath + p.getLink()+(Accion.EDITAR == p.getAccion() || Accion.ELIMINAR == p.getAccion() || Accion.VER == p.getAccion()?"?idParam="+ entidad_id:"") + "'>"+Accion.getIconForOption(p.getAccion()) +"</a>";
			}
		}
		return links;
	}
	
	
	
	

	public static String getLinkAccionesGeneral(Usuario usuario, Long entidad_id, String contextPath, List<Privilegio> acciones) {
		String links = "";
		for (Privilegio p : acciones) {
			if(tieneAutorizacion(usuario, Arrays.asList(p))) {
				links += " <a href='" + contextPath + p.getLink()+(Accion.EDITAR == p.getAccion() || Accion.ELIMINAR == p.getAccion() || Accion.VER == p.getAccion()?"?idParam="+ entidad_id:"") + "'>"+Accion.getIconForOption(p.getAccion()) +"</a>";
			}
		}
		return links;
	}
	
	/**
	 * Ojo con esta funcion! Se esta armando un link sin saber a el destinatario final, por
	 * lo que se debe chequear que la pantalla que se esta dando acceso, tenga validacion de usuario y privilegio.
	 * Ejemplo: se mando link para ver un monitoreo a los usuarios suscriptos, entonces en la pantalla Ver Monitoreo, antes de devolver la pantalla, valido que ese usuario tenga privilegio de Ver ese Monitoreo!
	 * 
	 * Se arma con un link completo, para poder acceder desde afuera del sistema (Mail, Telegram, ...) y no con un Path relativo como se usa dentro de las pantallas del sistema
	 * Como es para ver afuera, entonces no se puede mandar el icono! Lo mando igual pero con texto tambien, cosa que se pueda usar en cualquier lado (ver si para telegram se reemplaza el icono por otro que se pueda ver?)
	 * @param entidad_id
	 * @param contextPath
	 * @param p
	 * @param otherAtributes
	 * @return
	 */
	public static String getLinkAccionWithoutSecurity(Long entidad_id, String sistemUrl, Privilegio p, String linkText, String otherAtributes) {
		return "<a href='" + sistemUrl + p.getLink()+(Accion.EDITAR == p.getAccion() || Accion.ELIMINAR == p.getAccion() || Accion.VER == p.getAccion()?"?idParam="+ entidad_id + (otherAtributes != null?"&"+otherAtributes:""):(otherAtributes != null?"?"+otherAtributes:"")) + "'>"+Accion.getIconForOption(p.getAccion()) + linkText +"</a>";
	}
	
	/**
	 * Esta opcion la uso para enviar un button en un spring error, la funcionalidad se la doy con jquery en la jsp, ya que la primera vez que la use fue para armar un edit popup!
	 * @param usuario
	 * @param entidad_id
	 * @param contextPath
	 * @param accion
	 * @return
	 */
	public static String getButtonAccionGeneral(Usuario usuario, Long entidad_id, String contextPath, Privilegio accion) {
		if(tieneAutorizacion(usuario, Arrays.asList(accion))) {
			String butonid = "buttonBusiness"+accion.getClase()+accion.getAccion();
			return "<button type='button' id='"+butonid+"' name='"+butonid+"' data-entity_id='"+entidad_id+"' class='btn btn-default'>"+Accion.getIconForOption(accion.getAccion())+"</button>";
		}
		return "";
	}

	/**
	 * Develve el token del usuario, o genera uno nuevo si el anterior estaba vencido
	 * @param usuario
	 * @return
	 */
	public static TokenTemporalDTO getTokenTemporal(Usuario usuario) {
		synchronized (mapToken) {
			TokenTemporalDTO token = mapToken.get(usuario.getId());
			if(token == null || token.getExpirationTime().before(new Date())) {
				token = new TokenTemporalDTO(DateUtils.addMinutes(new Date(), 15), RandomStringUtils.random(5, false, true));
				mapToken.put(usuario.getId(), token);
			}
			return token;
		}
	}
	
	/**
	 * Devuelve datos relacionados con el token
	 * @param token
	 * @return
	 */
	public static Entry<Long, TokenTemporalDTO> getTokenTemporal(String token) {
		synchronized (mapToken) {
			for (Entry<Long, TokenTemporalDTO> entry : mapToken.entrySet()) {
				if(entry.getValue().getToken().equals(token)) {
					return entry;
				}
			}
			return null;
		}
	}

	public static void limpiarTokens() {
		synchronized (mapToken) {
			Date fechaActual = new Date();
			for(Iterator<Map.Entry<Long, TokenTemporalDTO>> it = mapToken.entrySet().iterator(); it.hasNext(); ) {
			    Map.Entry<Long, TokenTemporalDTO> entry = it.next();
			    if(entry.getValue().getExpirationTime().before(fechaActual)) {//borro los vencidos
			        it.remove();
			    }
			}
		}
	}

	public static void removeToken(Long key) {
		synchronized (mapToken) {
			mapToken.remove(key);
		}
	}
}
