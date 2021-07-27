package ar.com.signals.trading.eventos.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.com.signals.trading.eventos.domain.Suscripcion;
import ar.com.signals.trading.eventos.dto.SuscripcionDTO;
import ar.com.signals.trading.eventos.service.SuscripcionSrv;
import ar.com.signals.trading.eventos.support.MetodoNotificacion;
import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.SeguridadUtil;
import ar.com.signals.trading.util.rest.MyRestGenericSimpleResponse;
import ar.com.signals.trading.util.rest.StatusCodes;
import ar.com.signals.trading.util.web.ControllerUtil;
import ar.com.signals.trading.util.web.JQueryDatatablesPage;
import ar.com.signals.trading.util.web.Mensaje;


@Controller
@RequestMapping("web/EVENTOS/Suscripcion")
public class SuscripcionController {
	@Resource private SuscripcionSrv suscripcionSrv;
	@Resource(name = "messageSource") private MessageSource messageSource;
    
	@RequestMapping( value = "/NUEVO", method = RequestMethod.GET )
    public String nuevo(@AuthenticationPrincipal Usuario usuario, Model model, RedirectAttributes redirectAttributes) throws Exception{
		List<Privilegio> eventos = usuario.getPrivilegiosEvento();
		if(eventos.isEmpty()) {
			Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.WARNING, "La Responsabilidad con la que inicio sesion no tiene Privilegios para Suscribirse a ningun Evento"));
			return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
		}
		SuscripcionDTO entidadDTO = new SuscripcionDTO();
		model.addAttribute("entidadDTO", entidadDTO);
		model.addAttribute("tiposEventosDTO", eventos);
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.EVENTOS, Suscripcion.class, Accion.NUEVO));
	    return ControllerUtil.getTileName(Modulo.EVENTOS, Suscripcion.class, Accion.NUEVO);
    }
	
	@RequestMapping( value = "/NUEVO", method = RequestMethod.POST )
    public String nuevoPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") SuscripcionDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes, HttpServletRequest request){
		if(errors.hasErrors()){
			model.addAttribute("tiposEventosDTO", Arrays.asList(usuario.getPrivilegiosEvento()));
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.EVENTOS, Suscripcion.class, Accion.NUEVO));
			return ControllerUtil.getTileName(Modulo.EVENTOS, Suscripcion.class, Accion.NUEVO);
		}else{
			suscripcionSrv.validateAndPersist(entidadDTO, errors, usuario);
			if(errors.hasErrors()){
				model.addAttribute("tiposEventosDTO", Arrays.asList(usuario.getPrivilegiosEvento()));
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.EVENTOS, Suscripcion.class, Accion.NUEVO));
				return ControllerUtil.getTileName(Modulo.EVENTOS, Suscripcion.class, Accion.NUEVO);
			}else{
				//Redirecciono a Alta de Medicion con mensaje de exito
				Mensaje mensaje = new Mensaje(Mensaje.SUCCESS, "Las Suscripciones a los eventos:");
				for (Privilegio evento : entidadDTO.getEventos()) {
					mensaje.addLine(messageSource.getMessage("menu.EVENTOS."+evento.getClaseMenu()+".INFORMAR", new Object[] {}, LocaleContextHolder.getLocale()));
				}
				mensaje.addLine("se registraron con éxito." + SeguridadUtil.getLinkAcciones(usuario, null, request.getContextPath(), Arrays.asList(Privilegio.SUSCRIPCION_LISTAR)));
				Mensaje.agregarMensajesAlModel(redirectAttributes, mensaje);
				return ControllerUtil.redirectGET(Modulo.EVENTOS, Suscripcion.class, Accion.NUEVO);
			}
		}
    }
	
	@RequestMapping( value = "/ELIMINAR", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody MyRestGenericSimpleResponse<SuscripcionDTO> eliminar(@AuthenticationPrincipal Usuario usuario, 
    		@RequestParam(value = "idParam", required = true) Long id){
		MyRestGenericSimpleResponse<SuscripcionDTO> response = new MyRestGenericSimpleResponse<SuscripcionDTO>();	
		try {
			SuscripcionDTO entidadDTO = suscripcionSrv.eliminar(usuario, id);
			response.setStatusCode(StatusCodes.OK);
			response.setEntidadDTO(entidadDTO);
			response.setObservaciones("La suscripcion " + messageSource.getMessage("menu.EVENTOS."+entidadDTO.getEvento().getClaseMenu()+".INFORMAR", new Object[] {}, LocaleContextHolder.getLocale()) + " del Establecimiento " + entidadDTO.getSucursal_codigo() + " se elimino con exito");//se muetra en un notify en la jsp
		} catch (Exception e) {
			response.setStatusCode(StatusCodes.ERROR_CLIENTE);//Esto indica a la jsp que no se pudo hacer la operacion soliciatada, que revise lo que ingreso
			response.setObservaciones(e.getMessage());
		}
		return response;
    }
	
	@RequestMapping( value = "/LISTAR", method = RequestMethod.GET )
    public String listar(@AuthenticationPrincipal Usuario usuario, Model model){
		model.addAttribute("inicializar_con_datos", true);//para disparar la busqueda		
		//model.addAttribute("ignorar_session_state", true);//si se un redireccion a listar con ciertos filtros especificos, entonces anulo el uso de los filtros guardados de la session! (caso contrario no voy a ver los filtros que quiero, voy a ver filtros que estube usando antes)
		//model.addAttribute("tipos", TipoSuscripcion.values());

		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.EVENTOS, Suscripcion.class, Accion.LISTAR));
        return ControllerUtil.getTileName(Modulo.EVENTOS, Suscripcion.class, Accion.LISTAR);
    }
	
	@RequestMapping( value = "/REST/BUSCAR", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody JQueryDatatablesPage<SuscripcionDTO> buscar(
    		@AuthenticationPrincipal Usuario usuario, 
    	        //@RequestParam int iDisplayStart,
    	        //@RequestParam int iDisplayLength,
    	        //@RequestParam int sEcho, // for datatables draw count
    	        //@RequestParam String sSearch,
    	        @RequestParam Long fltSucursalId
    	        //@RequestParam(required=false) TipoSuscripcion fltTipo
    	        ) throws Exception {		
    	    //int pageNumber = (iDisplayStart + 1) / iDisplayLength;
		SeguridadUtil.validarAutorizacion(usuario, Privilegio.SUSCRIPCION_LISTAR);

		MetodoNotificacion metodo = usuario.getTelegram_id()!=null?MetodoNotificacion.telegram:(StringUtils.isNotEmpty(usuario.getUser_email())?MetodoNotificacion.email:MetodoNotificacion.pantalla);
	    List<SuscripcionDTO> entidades = suscripcionSrv.getSuscripcionesDTO(usuario);
	    for (SuscripcionDTO suscripcionDTO : entidades) {
	    	suscripcionDTO.setMetodoNotificacionPrincipal(metodo);
			suscripcionDTO.setEventoDescripcion(messageSource.getMessage("menu.EVENTOS."+suscripcionDTO.getEvento().getClaseMenu()+".INFORMAR", new Object[] {}, LocaleContextHolder.getLocale()));
		}
	    int iTotalRecords = (int) (int) entidades.size();
	    int iTotalDisplayRecords = entidades.size();
	    JQueryDatatablesPage<SuscripcionDTO> dtPage = new JQueryDatatablesPage<SuscripcionDTO>(
	    		entidades, iTotalRecords, iTotalDisplayRecords, "2");//Integer.toString(sEcho));

	    return dtPage;
    }
}
