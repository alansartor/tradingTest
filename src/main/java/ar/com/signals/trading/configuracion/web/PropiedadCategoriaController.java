package ar.com.signals.trading.configuracion.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

import ar.com.signals.trading.configuracion.domain.PropiedadCategoria;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaDTO;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaDTO2;
import ar.com.signals.trading.configuracion.service.PropiedadCategoriaSrv;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.SeguridadUtil;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.util.rest.MyRestGenericSimpleResponse;
import ar.com.signals.trading.util.rest.StatusCodes;
import ar.com.signals.trading.util.web.ControllerUtil;
import ar.com.signals.trading.util.web.JQueryDatatablesPage;
import ar.com.signals.trading.util.web.Mensaje;
import ar.com.signals.trading.util.web.ServidorUtil;

@Controller
@RequestMapping("/web/CONFIGURACION/PropiedadCategoria")
public class PropiedadCategoriaController {
	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	@Resource private ServidorUtil servidorUtil;
	
	@RequestMapping( value = "/NUEVO", method = RequestMethod.GET )
    public String nuevo(Model model){
		PropiedadCategoriaDTO entidadDTO = new PropiedadCategoriaDTO();
		entidadDTO.setActivo(true);
		model.addAttribute("entidadDTO", entidadDTO);
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.NUEVO));
        return ControllerUtil.getTileName(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.NUEVO);
    }
	
	@RequestMapping( value = "/NUEVO", method = RequestMethod.POST )
    public String nuevoPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") PropiedadCategoriaDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes, HttpServletRequest request){
		if(errors.hasErrors()){
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.NUEVO));
			return ControllerUtil.getTileName(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.NUEVO);
		}else{
			propiedadCategoriaSrv.validateAndPersist(entidadDTO, errors, usuario);
			if(errors.hasErrors()){
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.NUEVO));
				return ControllerUtil.getTileName(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.NUEVO);
			}else{
				Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "PropiedadCategoria <strong>" + entidadDTO.toString() + "</strong> Registrado con éxito"+ SeguridadUtil.getLinkAcciones(usuario, entidadDTO.getId(), request.getContextPath(), Arrays.asList(Privilegio.PROPIEDADCATEGORIA_EDITAR, Privilegio.PROPIEDADCATEGORIA_LISTAR))));
				return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
			}
		}
    }
	
	@RequestMapping( value = "/EDITAR", method = RequestMethod.GET )
    public String editar(Model model, @RequestParam(value = "idParam", required = true) Long id){
		model.addAttribute("entidadDTO", propiedadCategoriaSrv.obtenerDTO(id));
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.EDITAR, id));
        return ControllerUtil.getTileName(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.EDITAR);
    }
	
	@RequestMapping( value = "/EDITAR", method = RequestMethod.POST )
    public String editarPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") PropiedadCategoriaDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes, HttpServletRequest request){
		if(errors.hasErrors()){
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.EDITAR, entidadDTO.getCodigo()));
			return ControllerUtil.getTileName(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.EDITAR);
		}else{
			propiedadCategoriaSrv.validateAndPersist(entidadDTO, errors, usuario);
			if(errors.hasErrors()){
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.EDITAR, entidadDTO.getCodigo()));
				return ControllerUtil.getTileName(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.EDITAR);
			}else{
				Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "PropiedadCategoria <strong>" + entidadDTO.toString() + "</strong> Modificado con éxito"+ SeguridadUtil.getLinkAcciones(usuario, entidadDTO.getId(), request.getContextPath(), Arrays.asList(Privilegio.PROPIEDADCATEGORIA_EDITAR, Privilegio.PROPIEDADCATEGORIA_LISTAR))));
				return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
			}
		}
    }
	
	@RequestMapping( value = "/VER", method = RequestMethod.GET )
    public String ver(Model model, @RequestParam(value = "idParam", required = true) Long id){
		model.addAttribute("entidadDTO", propiedadCategoriaSrv.obtenerDTO(id));
		//model.addAttribute("empresasDTOs", empresaSrv.getEmpresasDTO());
		//model.addAttribute("plantasDTOs", sucursalSrv.getSucursalesDTO(null, null));
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.VER, id));
        return ControllerUtil.getTileName(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.VER);
    }
	
	@RequestMapping( value = "/LISTAR", method = RequestMethod.GET )
    public String listarRO(@AuthenticationPrincipal Usuario usuario, Model model){
		if(!TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {//solo es super usuario puede ver todo
			model.addAttribute("inicializar_con_datos", true);//para disparar la busqueda
			String inicializar_con_datos_notificacion = "Se muestran todas las propiedades";
			//model.addAttribute("ignorar_session_state", true);//si se un redireccion a listar con ciertos filtros especificos, entonces anulo el uso de los filtros guardados de la session! (caso contrario no voy a ver los filtros que quiero, voy a ver filtros que estube usando antes)
			model.addAttribute("inicializar_con_datos_notificacion", StringUtils.remove(inicializar_con_datos_notificacion, "\""));
		}
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.LISTAR));
		return ControllerUtil.getTileName(Modulo.CONFIGURACION, PropiedadCategoria.class, Accion.LISTAR);
    }
	
	@RequestMapping( value = "/REST/BUSCAR", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody JQueryDatatablesPage<PropiedadCategoriaDTO2> buscar(
    		@AuthenticationPrincipal Usuario usuario,
	        //@RequestParam int iDisplayStart,
	        //@RequestParam int iDisplayLength,
	        //@RequestParam int sEcho, for datatables draw count
	        //@RequestParam String sSearch
			@RequestParam Long fltSujetoId,
			@RequestParam(required=false) Long fltSucursalId) {
		List<PropiedadCategoriaDTO2> entidades = null;
		SeguridadUtil.validarAutorizacion(usuario, Privilegio.PROPIEDADCATEGORIA_LISTAR);
		if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {//solo el super usuario puede ver todo
			 entidades = propiedadCategoriaSrv.getPropiedadesDTO(true);
		}else {
			//usuario comunes solo pueden ver propiedades de su sujueto maestro, sucrsal o unidadProductiva
			SeguridadUtil.validarAutorizacion(usuario, Privilegio.PROPIEDADCATEGORIA_LISTAR);
			entidades = propiedadCategoriaSrv.getPropiedadesDTO(false);
		}
	   
	    int iTotalRecords = (int) (int) entidades.size();
	    int iTotalDisplayRecords = entidades.size();
	    JQueryDatatablesPage<PropiedadCategoriaDTO2> dtPage = new JQueryDatatablesPage<PropiedadCategoriaDTO2>(
	    		entidades, iTotalRecords, iTotalDisplayRecords, "2");

	    return dtPage;
    }	
}