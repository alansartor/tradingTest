package ar.com.signals.trading.seguridad.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;

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

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.seguridad.service.RespoSrv;
import ar.com.signals.trading.seguridad.service.RespoUsuarioSrv;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.util.web.ControllerUtil;
import ar.com.signals.trading.util.web.JQueryDatatablesPage;
import ar.com.signals.trading.util.web.Mensaje;

@Controller
@RequestMapping("/web/SEGURIDAD/Respo")
public class ResponsabilidadController {
	@Resource private UsuarioSrv usuarioSrv;
	@Resource private RespoSrv responsabilidadSrv;
	@Resource private RespoUsuarioSrv respoUsuarioSrv;
	
	@RequestMapping( value = "/NUEVO", method = RequestMethod.GET )
    public String nuevo(@AuthenticationPrincipal Usuario usuario, Model model){
		ResponsabilidadDTO entidadDTO = new ResponsabilidadDTO();
		model.addAttribute("entidadDTO", entidadDTO);
		if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {//solo es super usuario crea responsabilidades para usaurio maestro
			model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(null, null));//ve todos los privilegios
			model.addAttribute("tipos", Arrays.asList(TipoResponsabilidad.UsuarioComun, TipoResponsabilidad.UsuarioMaestro));
		}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
			model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(usuario.getResponsabilidad().getId(), null));//privilegios limitados a su responsabilidad
			model.addAttribute("tipos", Arrays.asList(TipoResponsabilidad.UsuarioComun));
		}else {
			
		}
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Respo.class, Accion.NUEVO));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Respo.class, Accion.NUEVO);
    }
	
	@RequestMapping( value = "/NUEVO", method = RequestMethod.POST )
    public String nuevoPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") ResponsabilidadDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes){
		if(errors.hasErrors()){
			if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {//solo es super usuario crea responsabilidades para usaurio maestro
				model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(null, null));//ve todos los privilegios
				model.addAttribute("tipos", Arrays.asList(TipoResponsabilidad.UsuarioComun, TipoResponsabilidad.UsuarioMaestro));
			}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
				model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(usuario.getResponsabilidad().getId(), null));//privilegios limitados a su responsabilidad
				model.addAttribute("tipos", Arrays.asList(TipoResponsabilidad.UsuarioComun));
			}else {
				
			}
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Usuario.class, Accion.NUEVO));
			return ControllerUtil.getTileName(Modulo.SEGURIDAD, Respo.class, Accion.NUEVO);
		}else{
			responsabilidadSrv.validateAndPersist(entidadDTO, errors, usuario);
			if(errors.hasErrors()){
				if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {//solo es super usuario crea responsabilidades para usaurio maestro
					model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(null, null));//ve todos los privilegios
					model.addAttribute("tipos", Arrays.asList(TipoResponsabilidad.UsuarioComun, TipoResponsabilidad.UsuarioMaestro));
				}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
					model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(usuario.getResponsabilidad().getId(), null));//privilegios limitados a su responsabilidad
					model.addAttribute("tipos", Arrays.asList(TipoResponsabilidad.UsuarioComun));
				}else {
					
				}
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Respo.class, Accion.NUEVO));
				return ControllerUtil.getTileName(Modulo.SEGURIDAD, Respo.class, Accion.NUEVO);
			}else{
				Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "Responsabilidad <strong>" + entidadDTO.getCodigo() + "</strong> Registrada con éxito"));
				return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
			}
		}
    }
	
	@RequestMapping( value = "/EDITAR", method = RequestMethod.GET )
    public String editar(@AuthenticationPrincipal Usuario usuario, Model model, @RequestParam(value = "idParam", required = true) Long id){
		model.addAttribute("entidadDTO", responsabilidadSrv.obtenerDTO(id));
		if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {//solo es super usuario crea responsabilidades para usaurio maestro
			model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(null, id));//ve todos los privilegios
		}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
			model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(usuario.getResponsabilidad().getId(), id));//privilegios limitados a su responsabilidad
		}else {
			
		}
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Respo.class, Accion.EDITAR, id));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Respo.class, Accion.EDITAR);
    }
	
	@RequestMapping( value = "/EDITAR", method = RequestMethod.POST )
    public String editarPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") ResponsabilidadDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes){
		if(errors.hasErrors()){
			if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {//solo es super usuario crea responsabilidades para usaurio maestro
				model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(null, entidadDTO.getId()));//ve todos los privilegios
			}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
				model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(usuario.getResponsabilidad().getId(), entidadDTO.getId()));//privilegios limitados a su responsabilidad
			}else {
				
			}
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Respo.class, Accion.EDITAR, entidadDTO.getId()));
			return ControllerUtil.getTileName(Modulo.SEGURIDAD, Respo.class, Accion.EDITAR);
		}else{
			responsabilidadSrv.validateAndPersist(entidadDTO, errors, usuario);
			if(errors.hasErrors()){
				if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {//solo es super usuario crea responsabilidades para usaurio maestro
					model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(null, entidadDTO.getId()));//ve todos los privilegios
				}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
					model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(usuario.getResponsabilidad().getId(), entidadDTO.getId()));//privilegios limitados a su responsabilidad
				}else {
					
				}
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Respo.class, Accion.EDITAR, entidadDTO.getId()));
				return ControllerUtil.getTileName(Modulo.SEGURIDAD, Respo.class, Accion.EDITAR);
			}else{
				Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "Responsabilidad <strong>" + entidadDTO.getCodigo() + "</strong> Modificada con éxito"));
				return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
			}
		}
    }
	
	@RequestMapping( value = "/LISTAR", method = RequestMethod.GET )
    public String listar(Model model){
		//model.addAttribute("entidades", usuarioSrv.getEntidades());
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Respo.class, Accion.LISTAR));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Respo.class, Accion.LISTAR);
    }
	
	@RequestMapping( value = "/REST/BUSCAR", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody JQueryDatatablesPage<ResponsabilidadDTO> buscar(
    		@AuthenticationPrincipal Usuario usuario
    	        //@RequestParam int iDisplayStart,
    	        //@RequestParam int iDisplayLength,
    	        //@RequestParam int sEcho, for datatables draw count
    	        //@RequestParam String sSearch
    		) {

    	    //int pageNumber = (iDisplayStart + 1) / iDisplayLength;
		List<ResponsabilidadDTO> entidades = new ArrayList<>();
		if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
			entidades = responsabilidadSrv.getResponsabilidadesDTO(null);
		}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
			//el usuario maestro ve las respo del owner de su respo, que seria el mismo
			entidades = responsabilidadSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioComun));
		}else {
			
		}			
	    int iTotalRecords = (int) (int) entidades.size();
	    int iTotalDisplayRecords = entidades.size();
	    JQueryDatatablesPage<ResponsabilidadDTO> dtPage = new JQueryDatatablesPage<ResponsabilidadDTO>(
	    		entidades, iTotalRecords, iTotalDisplayRecords, "2");

	    return dtPage;
    }
	
	@RequestMapping( value = "/VER", method = RequestMethod.GET )
    public String ver(@AuthenticationPrincipal Usuario usuario, Model model, @RequestParam(value = "idParam", required = true) Long id){
		model.addAttribute("entidadDTO", responsabilidadSrv.obtenerDTO(id));
		if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {//solo es super usuario crea responsabilidades para usaurio maestro
			model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(null, id));//ve todos los privilegios
		}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
			model.addAttribute("privilegiosDTOs", responsabilidadSrv.obtenerPrivilegiosDTO(usuario.getResponsabilidad().getId(), id));//privilegios limitados a su responsabilidad
		}else {
			
		}
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, Respo.class, Accion.VER, id));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Respo.class, Accion.VER);
    }
}
