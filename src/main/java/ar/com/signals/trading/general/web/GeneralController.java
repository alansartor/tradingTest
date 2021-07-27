package ar.com.signals.trading.general.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.com.signals.trading.configuracion.service.PropiedadCategoriaSrv;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.SeguridadUtil;
import ar.com.signals.trading.util.service.EmailSrv;
import ar.com.signals.trading.util.web.ControllerUtil;
import ar.com.signals.trading.util.web.Mensaje;

@Controller
@RequestMapping("web/GENERAL")
public class GeneralController {
	@Resource private EmailSrv emailSrv;
	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	
	@RequestMapping( value = "/HOME", method = RequestMethod.GET )
    public String home(@AuthenticationPrincipal Usuario usuario, Model model,
    		@RequestParam(value = "cancel", required = false) Boolean cancel){
		if(cancel != null && cancel){
			List<Mensaje> mensajes = new ArrayList<Mensaje>();
			mensajes.add(new Mensaje(Mensaje.INFO, "A cancelado la operacion"));
			Mensaje.agregarMensajesAlModel(model, mensajes);
		}
		return ControllerUtil.getTileName(Modulo.GENERAL, Accion.HOME);
    }
	
	@RequestMapping(value="/REPORTAR_ERROR", method = RequestMethod.POST)
	public String reportarError(@AuthenticationPrincipal Usuario usuario,
			@RequestParam("error") String error, 
			@RequestParam("stackTrace") String stackTrace,
			RedirectAttributes redirectAttributes) throws Exception{
		String propiedadDestinatarios = propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_MAILS_REPORTE_ERRORES);
		String[] destinatarios = propiedadDestinatarios.split(";");
		if(destinatarios != null && destinatarios.length > 0) {
			String body = "Usuario: "+ usuario.getUsername() +" \n";
			body += "Fecha: "+ new Date() +" \n";
			body += "Error: "+ error +" \n";
			body += "stackTrace: "+ stackTrace;
			emailSrv.informar(destinatarios, "Reporte Error", body ,null);
		}else {
			throw new Exception("No se puede informar error por mail, no hay destinarios. Comuniquese con el Administrador para que configure la Propiedad: " + PropiedadCategoriaEnum.PROPIEDAD_MAILS_REPORTE_ERRORES);
		}
		Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "El reporte del error se envio de forma correcta a los destinatarios: " + propiedadDestinatarios));
		return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
	}
}
