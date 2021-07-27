package ar.com.signals.trading.seguridad.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

import ar.com.signals.trading.configuracion.service.PropiedadCategoriaSrv;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.TokenTemporalDTO;
import ar.com.signals.trading.seguridad.dto.UsuarioDTO;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.SeguridadUtil;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.util.web.ControllerUtil;
import ar.com.signals.trading.util.web.JQueryDatatablesPage;
import ar.com.signals.trading.util.web.Mensaje;

@Controller
@RequestMapping("/web/SEGURIDAD/Usuario")
public class UsuarioController {
	@Resource private UsuarioSrv usuarioSrv;
	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	
	@RequestMapping( value = "/NUEVO", method = RequestMethod.GET )
    public String nuevo(Model model){
		UsuarioDTO entidadDTO = new UsuarioDTO();
		entidadDTO.setUser_enabled(true);
		entidadDTO.setPassword("1234");
		model.addAttribute("entidadDTO", entidadDTO);
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.NUEVO));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Usuario.class, Accion.NUEVO);
    }
	
	@RequestMapping( value = "/NUEVO", method = RequestMethod.POST )
    public String nuevoPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") UsuarioDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes){
		if(errors.hasErrors()){
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.NUEVO));
			return ControllerUtil.getTileName(Modulo.SEGURIDAD, Usuario.class, Accion.NUEVO);
		}else{
			List<Mensaje> mensajes = usuarioSrv.validateAndPersist(entidadDTO, errors, usuario);
			Mensaje.agregarMensajesAlModel(model, mensajes);
			if(errors.hasErrors()){
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.NUEVO));
				return ControllerUtil.getTileName(Modulo.SEGURIDAD, Usuario.class, Accion.NUEVO);
			}else{
				//Redirecciono a Alta de Medicion con mensaje de exito
				Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "Usuario <strong>" + entidadDTO.getUsername() + "</strong> Registrado con éxito"));
				return ControllerUtil.redirectGET(Modulo.SEGURIDAD, Usuario.class, Accion.NUEVO);
			}
		}
    }
	
	@RequestMapping( value = "/EDITAR", method = RequestMethod.GET )
    public String editar(Model model, @RequestParam(value = "idParam", required = true) Long id){
		model.addAttribute("entidadDTO", usuarioSrv.obtenerDTO(id));
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR, id));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR);
    }
	
	@RequestMapping( value = "/EDITAR", method = RequestMethod.POST )
    public String editarPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") UsuarioDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes){
		if(errors.hasErrors()){
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR, entidadDTO.getUsername()));
			return ControllerUtil.getTileName(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR);
		}else{
			List<Mensaje> mensajes = usuarioSrv.validateAndPersist(entidadDTO, errors, usuario);
			Mensaje.agregarMensajesAlModel(model, mensajes);
			if(errors.hasErrors()){
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR, entidadDTO.getUsername()));
				return ControllerUtil.getTileName(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR);
			}else{
				Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "Usuario <strong>" + entidadDTO.getUsername() + "</strong> Modificado con éxito"));
				return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
			}
		}
    }

	@RequestMapping( value = "/RESETEAR", method = RequestMethod.GET )
    public String resetear(@AuthenticationPrincipal Usuario usuario, Model model, @RequestParam(value = "idParam", required = true) Long id, RedirectAttributes redirectAttributes){
		UsuarioDTO entidadDTO = usuarioSrv.resetPassword(id, usuario);
		Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "La Password del Usuario <strong>" + entidadDTO.getUsername() + "</strong> se Reseteo con Exito. Password por defecto 1234, aguarde unos minutos hasta que se reflejen los cambios en el Servidor MSP"));
		return ControllerUtil.redirectGET(Modulo.SEGURIDAD, Usuario.class, Accion.LISTAR);
    }
	
	@RequestMapping( value = "/RESETEAR/Telegram", method = RequestMethod.GET )
    public String resetearTelegram(@AuthenticationPrincipal Usuario usuario, Model model, RedirectAttributes redirectAttributes){
		usuarioSrv.resetTelegram(usuario);
		Mensaje.agregarNotificacionesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, 15000, Mensaje.PlacementAlignRigth, "La desvinculacion del Sistema con la cuenta de Telegram se realizo con éxito, puede volver a realizar l vinculacion cuando quiera"));
		return ControllerUtil.redirectGET(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR_2);
    }
	
	@RequestMapping( value = "/LISTAR", method = RequestMethod.GET )
    public String listar(Model model, @AuthenticationPrincipal Usuario usuario){
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.LISTAR));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Usuario.class, Accion.LISTAR);
    }
	
	@RequestMapping( value = "/REST/BUSCAR", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody JQueryDatatablesPage<UsuarioDTO> buscar(
       		@AuthenticationPrincipal Usuario usuario,
    	        //@RequestParam int iDisplayStart,
    	        //@RequestParam int iDisplayLength,
    	        //@RequestParam int sEcho, // for datatables draw count
    	        //@RequestParam String sSearch,
    	        @RequestParam String fltUsername,
    	        @RequestParam String fltDescripcion) throws Exception {			
    	    //int pageNumber = (iDisplayStart + 1) / iDisplayLength;
		List<UsuarioDTO> entidades = usuarioSrv.getEntidades(fltUsername, fltDescripcion);
	    int iTotalRecords = (int) (int) entidades.size();
	    int iTotalDisplayRecords = entidades.size();
	    JQueryDatatablesPage<UsuarioDTO> dtPage = new JQueryDatatablesPage<UsuarioDTO>(
	    		entidades, iTotalRecords, iTotalDisplayRecords, "2");//Integer.toString(sEcho));

	    return dtPage;
    }

	@RequestMapping( value = "/EDITAR_2", method = RequestMethod.GET )
    public String editar2(@AuthenticationPrincipal Usuario usuario, Model model){
		//El metodo para guardar los cambios del usuario estan en UsuarioExtendedController de cada modulo, ya que difieren
		UsuarioDTO entidadDTO = new UsuarioDTO();
		entidadDTO.setId(usuario.getId());
		entidadDTO.setUsername(usuario.getUsername());
		entidadDTO.setDescripcion(usuario.getDescripcion());
		entidadDTO.setUser_email(usuario.getUser_email());
		if(usuario.getTelegram_id() == null) {
			TokenTemporalDTO token = SeguridadUtil.getTokenTemporal(usuario);
			entidadDTO.setTokenTelegram(token);
		}
		model.addAttribute("telegramBotName", propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_BOTNAME_TELEGRAM));
		model.addAttribute("entidadDTO", entidadDTO);
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR_2, usuario.getUsername()));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR_2);
    }
	
	@RequestMapping( value = "/EDITAR_2", method = RequestMethod.POST )
    public String editar2Post(@AuthenticationPrincipal Usuario usuario, Model model, @ModelAttribute("entidadDTO") UsuarioDTO entidadDTO, BindingResult errors, RedirectAttributes redirectAttributes){
		usuarioSrv.validateAndPersistPass(entidadDTO, errors);
		if(errors.hasErrors()){
			model.addAttribute("telegramBotName", propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_BOTNAME_TELEGRAM));
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR_2, entidadDTO.getUsername()));
			return ControllerUtil.getTileName(Modulo.SEGURIDAD, Usuario.class, Accion.EDITAR_2);
		}else{
			//actualizo los datos del usuario de la session
			//usuario.setNombre(entidadDTO.getDescripcion());
			usuario.setUser_email(entidadDTO.getUser_email());
			Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "Usuario <strong>" + entidadDTO.getUsername() + "</strong> Modificado con Exito"));
			return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
		}
    }
}
