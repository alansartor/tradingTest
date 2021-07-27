package ar.com.signals.trading.support.rest;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.signals.trading.eventos.domain.RegistroNotificacion;
import ar.com.signals.trading.eventos.service.RegistroNotificacionSrv;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.util.rest.AutocompleteDTO;
import ar.com.signals.trading.util.rest.MyRestGenericSimpleResponse;
import ar.com.signals.trading.util.rest.StatusCodes;

/**
 * EndPoints para dar soporte a las jsp
 * Se utilizaria para reemplazar a DWR
 * Tienen Seguridad!
 * 
 * @author pepe@hotmail.com
 *
 */
@RestController
@RequestMapping(value = "/web/support/REST")
public class SupportRestController {
	@Resource private UsuarioSrv usuarioSrv;
	@Resource private RegistroNotificacionSrv registroNotificacionSrv;
	
	private static Logger logger = LoggerFactory.getLogger(SupportRestController.class);
	
	@RequestMapping(value = "/autocompletar/usuarioAsignable", method = RequestMethod.GET)
	public List<? extends AutocompleteDTO> autocompletarUsuarioAsignable(@AuthenticationPrincipal Usuario usuario, @RequestParam(value="term", required=true) String term) {
		return usuarioSrv.getAutocompleteDTO(term);
	}
	
	@RequestMapping(value = {"/registroNotificacion/ultimos"}, method = RequestMethod.GET)
	public List<RegistroNotificacion> registroNotificacionUltimos(@AuthenticationPrincipal Usuario usuario) {
		//buscar notificaciones del usuario sin marcar como vistas y si no hay, o son pocas, mostrar las ultimas notificaciones leidas
		List<RegistroNotificacion> registros = registroNotificacionSrv.obtenerNoVistas(usuario);
		if(registros.size() < 5) {//completo hasta 5 notificaciones
			registros.addAll(registroNotificacionSrv.ultimasVistas(usuario, 5-registros.size()));
		}
		return registros;
	}
	
	@RequestMapping(value = "/registroNotificacion/marcarVistos/ids/{ids}", method = RequestMethod.PUT)
	public MyRestGenericSimpleResponse<Boolean> registroNotificacionMarcarVistos(@AuthenticationPrincipal Usuario usuario, @PathVariable(value = "ids") List<Long> ids){
		MyRestGenericSimpleResponse<Boolean> response = new MyRestGenericSimpleResponse<Boolean>();
		try {
			registroNotificacionSrv.marcarVistasNotificaciones(usuario, ids);
			response.setStatusCode(StatusCodes.OK);
			response.setEntidadDTO(true);
			response.setObservaciones("Las notificaciones se marcaron como vistas");
		}catch (Exception e) {
			response.setStatusCode(StatusCodes.ERROR_CLIENTE);//Esto indica a la jsp que no se pudo hacer la operacion soliciatada, que revise lo que ingreso
			response.setObservaciones(e.getMessage());
		}
		return response;
	}
}
