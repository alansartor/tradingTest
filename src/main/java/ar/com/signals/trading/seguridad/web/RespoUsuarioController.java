package ar.com.signals.trading.seguridad.web;

import java.util.ArrayList;
import java.util.Arrays;
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

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.RespoUsuario;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.RespoUsuarioDTO;
import ar.com.signals.trading.seguridad.service.RespoUsuarioSrv;
import ar.com.signals.trading.util.web.ControllerUtil;
import ar.com.signals.trading.util.web.JQueryDatatablesPage;
import ar.com.signals.trading.util.web.Mensaje;
import ar.com.signals.trading.seguridad.service.RespoSrv;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;

@Controller
@RequestMapping("/web/SEGURIDAD/RespoUsuario")
public class RespoUsuarioController {
	@Resource private RespoUsuarioSrv respoUsuarioSrv;
	@Resource private UsuarioSrv usuarioSrv;
	@Resource private RespoSrv respoSrv;
	
	@RequestMapping( value = "/NUEVO", method = RequestMethod.GET )
    public String nuevo(@AuthenticationPrincipal Usuario usuario, Model model){
		RespoUsuarioDTO entidadDTO = new RespoUsuarioDTO();
		entidadDTO.setActivo(true);
		if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
			//deberia ver todos los usuarios y solo las responsabilidades genericas tipo usaurioMaestro!
			//el usuario maestro le asigna la responsabilidad inicial a un usuario nuevo que representa al campo, y este debera luego generar usuarios y asignarle reponsabilidades comunes
			model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioMaestro, TipoResponsabilidad.UsuarioComun)));
			model.addAttribute("sinSucursal", true);
			//un superusuario solo asigna responsabilidad tipo usuario maestro que no tienen relacion con una sucursal!
			//model.addAttribute("sucursalesDTO", subSeccionSrv.getSubSeccionesDTO());
		}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
			//deberia ver sus usuarios, pero podria hacer referencia a usuario externo, por cuit o alias
			//deberia ver sus responsabilidadess y las genericas
			model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioComun)));
			model.addAttribute("sinSucursal", false);
		}else {
			//el usuario maestro del que depende le dio acceso a esta pantalla, asi que deberia ver lo mismo que el
			//model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO());
		}
		model.addAttribute("entidadDTO", entidadDTO);
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, RespoUsuario.class, Accion.NUEVO));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, RespoUsuario.class, Accion.NUEVO);
    }
	
	@RequestMapping( value = "/NUEVO", method = RequestMethod.POST )
    public String nuevoPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") RespoUsuarioDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes){
		if(errors.hasErrors()){
			if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
				//deberia ver todos los usuarios y solo las responsabilidades genericas tipo usaurioMaestro!
				//el usuario maestro le asigna la responsabilidad inicial a un usuario nuevo que representa al campo, y este debera luego generar usuarios y asignarle reponsabilidades comunes
				model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioMaestro, TipoResponsabilidad.UsuarioComun)));
				model.addAttribute("sinSucursal", true);
				//un superusuario solo asigna responsabilidad tipo usuario maestro que no tienen relacion con una sucursal!
				//model.addAttribute("sucursalesDTO", subSeccionSrv.getSubSeccionesDTO());
			}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
				//deberia ver sus usuarios, pero podria hacer referencia a usuario externo, por cuit o alias
				//deberia ver sus responsabilidadess y las genericas
				model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioComun)));
				model.addAttribute("sinSucursal", false);
				model.addAttribute("sucursales");//cuando el susario maestro inicia session, se le cargan todas sus sucursales!
			}else {
				//el usuario maestro del que depende le dio acceso a esta pantalla, asi que deberia ver lo mismo que el
				//model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO());
			}
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, RespoUsuario.class, Accion.NUEVO));
			return ControllerUtil.getTileName(Modulo.SEGURIDAD, RespoUsuario.class, Accion.NUEVO);
		}else{
			boolean sinSucursal = TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo());
			respoUsuarioSrv.validateAndPersist(entidadDTO, errors, sinSucursal);
			if(errors.hasErrors()){
				if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
					//deberia ver todos los usuarios y solo las responsabilidades genericas tipo usaurioMaestro!
					//el usuario maestro le asigna la responsabilidad inicial a un usuario nuevo que representa al campo, y este debera luego generar usuarios y asignarle reponsabilidades comunes
					model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioMaestro, TipoResponsabilidad.UsuarioComun)));
					model.addAttribute("sinSucursal", true);
					//un superusuario solo asigna responsabilidad tipo usuario maestro que no tienen relacion con una sucursal!
					//model.addAttribute("sucursalesDTO", subSeccionSrv.getSubSeccionesDTO());
				}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
					//deberia ver sus usuarios, pero podria hacer referencia a usuario externo, por cuit o alias
					//deberia ver sus responsabilidadess y las genericas
					model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioComun)));
					model.addAttribute("sinSucursal", false);
				}else {
					//el usuario maestro del que depende le dio acceso a esta pantalla, asi que deberia ver lo mismo que el
					//model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO());
				}
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, RespoUsuario.class, Accion.NUEVO));
				return ControllerUtil.getTileName(Modulo.SEGURIDAD, RespoUsuario.class, Accion.NUEVO);
			}else{
				//Redirecciono a Alta de Medicion con mensaje de exito
				Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "Se Asigno Responsabilidad <strong>" + entidadDTO.getRespo_codigo() + " al Usuario " + entidadDTO.getUsuario_username() + "</strong>"));
				return ControllerUtil.redirectGET(Modulo.SEGURIDAD, RespoUsuario.class, Accion.NUEVO);
			}
		}
    }
	
	@RequestMapping( value = "/EDITAR", method = RequestMethod.GET )
    public String editar(@AuthenticationPrincipal Usuario usuario, Model model, @RequestParam(value = "idParam", required = true) Long id){
		if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
			//deberia ver todos los usuarios y solo las responsabilidades genericas tipo usaurioMaestro!
			//el usuario maestro le asigna la responsabilidad inicial a un usuario nuevo que representa al campo, y este debera luego generar usuarios y asignarle reponsabilidades comunes
			model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioMaestro, TipoResponsabilidad.UsuarioComun)));
			model.addAttribute("sinSucursal", true);
			//un superusuario solo asigna responsabilidad tipo usuario maestro que no tienen relacion con una sucursal!
			//model.addAttribute("sucursalesDTO", subSeccionSrv.getSubSeccionesDTO());
		}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
			//deberia ver sus usuarios, pero podria hacer referencia a usuario externo, por cuit o alias
			//deberia ver sus responsabilidadess y las genericas
			model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioComun)));
			model.addAttribute("sinSucursal", false);
			model.addAttribute("sucursales");//cuando el susario maestro inicia session, se le cargan todas sus sucursales!
		}else {
			//el usuario maestro del que depende le dio acceso a esta pantalla, asi que deberia ver lo mismo que el
			//model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO());
		}
		model.addAttribute("entidadDTO", respoUsuarioSrv.obtenerDTO(id));
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, RespoUsuario.class, Accion.EDITAR, id));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, RespoUsuario.class, Accion.EDITAR);
    }
	
	@RequestMapping( value = "/EDITAR", method = RequestMethod.POST )
    public String editarPost(@AuthenticationPrincipal Usuario usuario, Model model, @Valid @ModelAttribute("entidadDTO") RespoUsuarioDTO entidadDTO, BindingResult errors,
    		RedirectAttributes redirectAttributes){
		if(errors.hasErrors()){
			if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
				//deberia ver todos los usuarios y solo las responsabilidades genericas tipo usaurioMaestro!
				//el usuario maestro le asigna la responsabilidad inicial a un usuario nuevo que representa al campo, y este debera luego generar usuarios y asignarle reponsabilidades comunes
				model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioMaestro, TipoResponsabilidad.UsuarioComun)));
				model.addAttribute("sinSucursal", true);
				//un superusuario solo asigna responsabilidad tipo usuario maestro que no tienen relacion con una sucursal!
				//model.addAttribute("sucursalesDTO", subSeccionSrv.getSubSeccionesDTO());
			}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
				//deberia ver sus usuarios, pero podria hacer referencia a usuario externo, por cuit o alias
				//deberia ver sus responsabilidadess y las genericas
				model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioComun)));
				model.addAttribute("sinSucursal", false);
			}else {
				//el usuario maestro del que depende le dio acceso a esta pantalla, asi que deberia ver lo mismo que el
				//model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO());
			}
			model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, RespoUsuario.class, Accion.EDITAR, entidadDTO.getUsuario_username() + " " + entidadDTO.getRespo_id()));
			return ControllerUtil.getTileName(Modulo.SEGURIDAD, RespoUsuario.class, Accion.EDITAR);
		}else{
			boolean sinSucursal = TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo());
			respoUsuarioSrv.validateAndPersist(entidadDTO, errors, sinSucursal);
			if(errors.hasErrors()){
				if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
					//deberia ver todos los usuarios y solo las responsabilidades genericas tipo usaurioMaestro!
					//el usuario maestro le asigna la responsabilidad inicial a un usuario nuevo que representa al campo, y este debera luego generar usuarios y asignarle reponsabilidades comunes
					model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioMaestro, TipoResponsabilidad.UsuarioComun)));
					model.addAttribute("sinSucursal", true);
					//un superusuario solo asigna responsabilidad tipo usuario maestro que no tienen relacion con una sucursal!
					//model.addAttribute("sucursalesDTO", subSeccionSrv.getSubSeccionesDTO());
				}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
					//deberia ver sus usuarios, pero podria hacer referencia a usuario externo, por cuit o alias
					//deberia ver sus responsabilidadess y las genericas
					model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO(Arrays.asList(TipoResponsabilidad.UsuarioComun)));
					model.addAttribute("sinSucursal", false);
				}else {
					//el usuario maestro del que depende le dio acceso a esta pantalla, asi que deberia ver lo mismo que el
					//model.addAttribute("responsabilidadesDTO", respoSrv.getResponsabilidadesDTO());
				}
				model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, RespoUsuario.class, Accion.EDITAR, entidadDTO.getUsuario_username() + " " + entidadDTO.getRespo_id()));
				return ControllerUtil.getTileName(Modulo.SEGURIDAD, RespoUsuario.class, Accion.EDITAR);
			}else{
				Mensaje.agregarMensajesAlModel(redirectAttributes, new Mensaje(Mensaje.SUCCESS, "Se modifico <strong>Responsabilidad " + entidadDTO.getRespo_codigo() + " del Usuario " + entidadDTO.getUsuario_username() + "</strong>"));
				return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
			}
		}
    }
	
	@RequestMapping( value = "/LISTAR", method = RequestMethod.GET )
    public String listar(Model model, @AuthenticationPrincipal Usuario usuario){
		//model.addAttribute("entidades", RespoUsuarioSrv.getEntidades());
		if(!TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
			String inicializar_con_datos_notificacion = "Se muestran todas las Responsabilidades que dio de alta";
			model.addAttribute("inicializar_con_datos", true);//para disparar la busqueda
			model.addAttribute("inicializar_con_datos_notificacion", StringUtils.remove(inicializar_con_datos_notificacion, "\""));		
		}
		//model.addAttribute("ignorar_session_state", true);//si se un redireccion a listar con ciertos filtros especificos, entonces anulo el uso de los filtros guardados de la session! (caso contrario no voy a ver los filtros que quiero, voy a ver filtros que estube usando antes)
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.SEGURIDAD, RespoUsuario.class, Accion.LISTAR));
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, RespoUsuario.class, Accion.LISTAR);
    }
	
	@RequestMapping( value = "/REST/BUSCAR", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody JQueryDatatablesPage<RespoUsuarioDTO> buscar(
    		@AuthenticationPrincipal Usuario usuario
    	        //@RequestParam int iDisplayStart,
    	        //@RequestParam int iDisplayLength,
    	        //@RequestParam int sEcho, // for datatables draw count
    	        //@RequestParam String sSearch,) throws Exception {			
    	    //int pageNumber = (iDisplayStart + 1) / iDisplayLength;
    		){
		List<RespoUsuarioDTO> entidades = new ArrayList<>();
		if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
			entidades = respoUsuarioSrv.getEntidades();
		}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
			entidades = respoUsuarioSrv.getEntidades();
		}
	    int iTotalRecords = (int) (int) entidades.size();
	    int iTotalDisplayRecords = entidades.size();
	    JQueryDatatablesPage<RespoUsuarioDTO> dtPage = new JQueryDatatablesPage<RespoUsuarioDTO>(
	    		entidades, iTotalRecords, iTotalDisplayRecords, "2");//Integer.toString(sEcho));

	    return dtPage;
    }
}
