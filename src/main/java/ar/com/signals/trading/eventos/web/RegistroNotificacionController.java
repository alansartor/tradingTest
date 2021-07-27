package ar.com.signals.trading.eventos.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import ar.com.signals.trading.eventos.domain.RegistroNotificacion;
import ar.com.signals.trading.eventos.domain.Suscripcion;
import ar.com.signals.trading.eventos.dto.RegistroNotificacionDTO;
import ar.com.signals.trading.eventos.dto.SuscripcionDTO;
import ar.com.signals.trading.eventos.service.RegistroNotificacionSrv;
import ar.com.signals.trading.eventos.service.SuscripcionSrv;
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
@RequestMapping("web/EVENTOS/RegistroNotificacion")
public class RegistroNotificacionController {
	@Resource private RegistroNotificacionSrv registroNotificacionSrv;
	@Resource(name = "messageSource") private MessageSource messageSource;
	
	@RequestMapping( value = "/LISTAR", method = RequestMethod.GET )
    public String listar(@AuthenticationPrincipal Usuario usuario, Model model){	
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.EVENTOS, RegistroNotificacion.class, Accion.LISTAR));
        return ControllerUtil.getTileName(Modulo.EVENTOS, RegistroNotificacion.class, Accion.LISTAR);
    }
	
	@RequestMapping( value = "/REST/BUSCAR", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody JQueryDatatablesPage<RegistroNotificacionDTO> buscar(
    		@AuthenticationPrincipal Usuario usuario 
    	        //@RequestParam int iDisplayStart,
    	        //@RequestParam int iDisplayLength,
    	        //@RequestParam int sEcho, // for datatables draw count
    	        //@RequestParam String sSearch,
    	        //@RequestParam Long fltSucursalId
    	        //@RequestParam(required=false) TipoSuscripcion fltTipo
    	        ) throws Exception {		
    	    //int pageNumber = (iDisplayStart + 1) / iDisplayLength;
		SeguridadUtil.validarAutorizacion(usuario, Privilegio.REGISTRONOTIFICACION_LISTAR);	
	    List<RegistroNotificacionDTO> entidades = registroNotificacionSrv.getRegistrosNotificacionesDTO(usuario);
	    int iTotalRecords = (int) (int) entidades.size();
	    int iTotalDisplayRecords = entidades.size();
	    JQueryDatatablesPage<RegistroNotificacionDTO> dtPage = new JQueryDatatablesPage<RegistroNotificacionDTO>(
	    		entidades, iTotalRecords, iTotalDisplayRecords, "2");//Integer.toString(sEcho));

	    return dtPage;
    }
}
