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
import ar.com.signals.trading.eventos.domain.SuscripcionConfig;
import ar.com.signals.trading.eventos.dto.SuscripcionConfigDTO;
import ar.com.signals.trading.eventos.dto.SuscripcionDTO;
import ar.com.signals.trading.eventos.service.SuscripcionConfigSrv;
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
@RequestMapping("web/EVENTOS/SuscripcionConfig")
public class SuscripcionConfigController {
	@Resource private SuscripcionConfigSrv suscripcionConfigSrv;
	@Resource(name = "messageSource") private MessageSource messageSource;
    
	@RequestMapping( value = "/CONFIGURAR", method = RequestMethod.GET )
    public String editar(@AuthenticationPrincipal Usuario usuario, Model model, RedirectAttributes redirectAttributes) throws Exception{
		SuscripcionConfigDTO entidadDTO = suscripcionConfigSrv.getSuscripcionConfigDTO(usuario);
		model.addAttribute("entidadDTO", entidadDTO);
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.EVENTOS, SuscripcionConfig.class, Accion.CONFIGURAR));
	    return ControllerUtil.getTileName(Modulo.EVENTOS, SuscripcionConfig.class, Accion.CONFIGURAR);
    }
	
	@RequestMapping( value = "/CONFIGURAR", method = RequestMethod.POST )
    public String editarPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") SuscripcionConfigDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes, HttpServletRequest request){
		if(errors.hasErrors()){
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.EVENTOS, SuscripcionConfig.class, Accion.CONFIGURAR));
			return ControllerUtil.getTileName(Modulo.EVENTOS, SuscripcionConfig.class, Accion.CONFIGURAR);
		}else{
			suscripcionConfigSrv.validateAndPersist(entidadDTO, errors, usuario);
			if(errors.hasErrors()){
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.EVENTOS, SuscripcionConfig.class, Accion.CONFIGURAR));
				return ControllerUtil.getTileName(Modulo.EVENTOS, SuscripcionConfig.class, Accion.CONFIGURAR);
			}else{
				//Redirecciono a Alta de Medicion con mensaje de exito
				Mensaje mensaje = new Mensaje(Mensaje.SUCCESS, "Las configuracion de las Suscripciones se registraron con éxito");
				Mensaje.agregarMensajesAlModel(redirectAttributes, mensaje);
				if(usuario.getTelegram_id()==null && entidadDTO.isEventoTradingAlert()) {
					Mensaje.agregarMensajesAlModel(redirectAttributes,  new Mensaje(Mensaje.WARNING, "Todavia no se asocio al Bot de Telegram del Sistema, por lo que no va a Recibir las Alertas. Vaya a "+SeguridadUtil.getLinkAccionWithoutSecurity(null, request.getContextPath(), Privilegio.USUARIO_EDITAR_2, messageSource.getMessage(Privilegio.USUARIO_EDITAR_2.getMenuMessageSourceCode(), new Object[] {"",""}, LocaleContextHolder.getLocale()),null)+" y siga las intrucciones para la vinculacion con Telegram"));
				}
				return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
			}
		}
    }
}
