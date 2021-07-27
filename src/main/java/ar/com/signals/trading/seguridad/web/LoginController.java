package ar.com.signals.trading.seguridad.web;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.LoginSelectDTO;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.seguridad.service.RespoUsuarioSrv;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.util.spring.MyWebAuthenticationDetails;
import ar.com.signals.trading.util.spring.WebAuthenticationEntryPoint;
import ar.com.signals.trading.util.support.BusinessGenericException;
import ar.com.signals.trading.util.web.ControllerUtil;
import ar.com.signals.trading.util.web.Mensaje;

@Controller
@RequestMapping("/web/SEGURIDAD")
public class LoginController {
	@Resource private UsuarioSrv usuarioSrv;
	@Resource private RespoUsuarioSrv respoUsuarioSrv;
	
	//private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping( value = "/LOGIN", method = RequestMethod.GET )
    public String login(HttpSession session, Model model, HttpServletRequest request, 
    		@RequestParam(required=false, name="user_target_url") String user_target_url){
		//Cuando spring redirecciona a esta url, por alguna razon no hay usaurio en el @AuthenticationPrincipal, entonces
		//cualquiera de las paginas que se redireccionan desde aqui puede que haya que buscar el usuario de forma manual
		
		SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
		if(securityContext != null){//Si ya estaba logueado entonces redirecciono a seleccion de responsabilidad/planta o al home
			Authentication auth = securityContext.getAuthentication(); 
			if(!(auth instanceof AnonymousAuthenticationToken)){
				if(SecurityContextHolder.getContext().getAuthentication() == null) {
					//Por alguna razon, cuando se vuelve a entrar a esta pagina pero la session del usuario todavia es valida, 
					//el SecurityContextHolder blanquea el usuario, por eso se lo pone de nuevo para que no fallen las paginas a donde se redirecciona
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
				Usuario user = (Usuario) auth.getPrincipal();
				if(user.getResponsabilidad() == null){
					return ControllerUtil.redirectGET(Modulo.SEGURIDAD, Accion.SELECCIONAR);
				}//else if(user.getSubSeccion() == null){
					//return ControllerUtil.redirectGET(Modulo.SEGURIDAD, SubSeccion.class, Accion.SELECCIONAR);
				//}
				return ControllerUtil.forwardGET(Modulo.GENERAL, Accion.HOME, "showMapa=true");/*The user is logged in :)*/ 
			}
		}
		model.addAttribute(WebAuthenticationEntryPoint.TarjetUrlAttribute, user_target_url);
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Accion.LOGIN);
    }
	
	@RequestMapping( value = "/SELECCIONAR", method = RequestMethod.GET )
    public String seleccionar(HttpSession session,
    		Model model) throws Exception{
		SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");		
		Usuario usuario = (Usuario)securityContext.getAuthentication().getPrincipal();
		//session.setAttribute("login_user", usuario);//esto no es necesario
		
		//limpiar todo lo referente a responsabilidad, sujetoResponsabilidad y sucursales, ya que cuando cambia de respo hay que limpiarlo si o si para que se complete con la nueva seleccion hay respo que no ahcen referencia a sucursales
		usuarioSrv.cleanRespoSujetoMenu(usuario);
		List<ResponsabilidadDTO> responsabilidades = respoUsuarioSrv.getResponsabilidadesDTO(usuario);
		if(responsabilidades.isEmpty()){
			throw new Exception("El usuario " + usuario.getUsername() + " no tiene ninguna Responsabilidad asignada");
		}
		LoginSelectDTO dto = new LoginSelectDTO();
		dto.setResponsabilidadId(responsabilidades.get(0).getId());
		//NEW 09/09/2020 aca deberia estar el user_target_url que dejamos en login page
		MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) securityContext.getAuthentication().getDetails();
		if(details != null) {
			dto.setUser_target_url(details.getUser_target_url());
		}
		
		model.addAttribute("entidadDTO", dto);
		if(responsabilidades.size() > 1) {
			Collections.sort(responsabilidades, new Comparator<ResponsabilidadDTO>() {
				public int compare(ResponsabilidadDTO r1, ResponsabilidadDTO r2) {
					return r1.getTipo().ordinal() - r2.getTipo().ordinal();//el orden en el enum dan el orden en que se muestran
				}
			});
			model.addAttribute("entidades", responsabilidades);
		}else {
			usuarioSrv.setResponsabilidadAndMakeMenu(usuario, dto.getResponsabilidadId());
			if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
				//Super Usuario, voy directamente al home, estos usuario son para dar de alta otros usuarios y asignar responsabilidades (el super usuario es para dar de alta usuarios maestros (aunque podrian dar de alta usuarios comunes tambien pero no podrian darle acceso a alguna sucursal, eso lo hace el maestro de la sucursal), y los usuario maestron dan de alta usaurios comunes)			
				//usuario.setSujetoResponsabilidad(usuario.getSujeto());//el superUsuario no hace referencia a ningun sujeto! Esto lo necesito para cuando doy de alta Responsabilidades, se que si este sujeto es null, entonces significa que puede editar responsabilidades sin owner, pero si es un usuarioMaestro entonces solo puede editar sus responsabilidades es decir la que tengan owner y coincida con este valor
				if(StringUtils.isNotEmpty(dto.getUser_target_url())) {
					return "redirect:"+dto.getUser_target_url();
				}
				return ControllerUtil.forwardGET(Modulo.GENERAL, Accion.HOME);
			}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())) {
				//Usuario Maestro, voy directamente al home, estos usuario son para dar de alta otros usuarios y asignar responsabilidades (el super usuario es para dar de alta usuarios maestros (aunque podrian dar de alta usuarios comunes tambien pero no podrian darle acceso a alguna sucursal, eso lo hace el maestro de la sucursal), y los usuario maestron dan de alta usaurios comunes)			
				if(StringUtils.isNotEmpty(dto.getUser_target_url())) {
					return "redirect:"+dto.getUser_target_url();
				}
				return ControllerUtil.forwardGET(Modulo.GENERAL, Accion.HOME);
			}else {
				return ControllerUtil.redirectGET(Modulo.SEGURIDAD, Accion.SELECCIONAR_2);//hago un redirect para que en la url quede apuntando correctamente
			}
		}
        return ControllerUtil.getTileName(Modulo.SEGURIDAD, Accion.SELECCIONAR);
    }
	
	@RequestMapping( value = "/SELECCIONAR", method = RequestMethod.POST )
    public String seleccionarPost(@AuthenticationPrincipal Usuario usuario, 
    		@ModelAttribute("entidadDTO") LoginSelectDTO dto, Model model, RedirectAttributes redirectAttributes){
		usuarioSrv.setResponsabilidadAndMakeMenu(usuario, dto.getResponsabilidadId());
		if(TipoResponsabilidad.SuperUsuario.equals(usuario.getResponsabilidad().getTipo())) {
			//Super Usuario, voy directamente al home, estos usuario son para dar de alta otros usuarios y asignar responsabilidades (el super usuario es para dar de alta usuarios maestros (aunque podrian dar de alta usuarios comunes tambien pero no podrian darle acceso a alguna sucursal, eso lo hace el maestro de la sucursal), y los usuario maestron dan de alta usaurios comunes)			
			//usuario.setSujetoResponsabilidad(usuario.getSujeto());//el superUsuario no hace referencia a ningun sujeto! Esto lo necesito para cuando doy de alta Responsabilidades, se que si este sujeto es null, entonces significa que puede editar responsabilidades sin owner, pero si es un usuarioMaestro entonces solo puede editar sus responsabilidades es decir la que tengan owner y coincida con este valor
			if(StringUtils.isNotEmpty(dto.getUser_target_url())) {
				return "redirect:"+dto.getUser_target_url();
			}
			return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);//forwardGET solo se puede usar en RequestMethod.GET
		}else if(TipoResponsabilidad.UsuarioMaestro.equals(usuario.getResponsabilidad().getTipo())){
			//Usuario Maestro, voy directamente al home, estos usuario son para dar de alta otros usuarios y asignar responsabilidades (el super usuario es para dar de alta usuarios maestros (aunque podrian dar de alta usuarios comunes tambien pero no podrian darle acceso a alguna sucursal, eso lo hace el maestro de la sucursal), y los usuario maestron dan de alta usaurios comunes)			
			//usuario.setSujetoResponsabilidad(usuario.getSujeto());//le repito el sujeto en el sujetoResponsabilidad, lo necesito para abm de responsabilidades/usuarios y asignacion de responsabilidades y establecimiento inicial		
			//usuarioSrv.setSujetoAndSucursales(usuario, usuario.getSujeto().getId());
			if(StringUtils.isNotEmpty(dto.getUser_target_url())) {
				return "redirect:"+dto.getUser_target_url();
			}
			return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);//forwardGET solo se puede usar en RequestMethod.GET
		}else {
			redirectAttributes.addFlashAttribute(WebAuthenticationEntryPoint.TarjetUrlAttribute, dto.getUser_target_url());
			return ControllerUtil.redirectGET(Modulo.SEGURIDAD, Accion.SELECCIONAR_2);//forward solo se puede usar en RequestMethod.GET
		}
    }
	
	@RequestMapping( value = "/SELECCIONAR_2", method = RequestMethod.GET )
    public String seleccionar2(@AuthenticationPrincipal Usuario usuario, 
    		Model model) throws Exception{
		LoginSelectDTO dto = new LoginSelectDTO();
		dto.setResponsabilidadId(usuario.getResponsabilidad().getId());
		if(model.containsAttribute(WebAuthenticationEntryPoint.TarjetUrlAttribute)){
			dto.setUser_target_url((String) model.asMap().get(WebAuthenticationEntryPoint.TarjetUrlAttribute));
			model.asMap().remove(WebAuthenticationEntryPoint.TarjetUrlAttribute);//para que no figure en el redirect!
		}
		model.addAttribute("entidadDTO", dto);
		//List<SujetoDTO> entidadesDto = respoUsuarioSrv.getSujetosDTO(usuario, usuario.getResponsabilidad());
		//if(entidadesDto.size() == 1){//paso a seleccion de Sucursal	
			//usuarioSrv.setSujetoAndSucursales(usuario, entidadesDto.get(0).getId());
			return ControllerUtil.redirectGET(Modulo.SEGURIDAD, Accion.SELECCIONAR_3);//hago un redirect para que en la url quede apuntando correctamente
		//}
/*		Collections.sort(entidadesDto, new Comparator<SujetoDTO>() {
			public int compare(SujetoDTO s1, SujetoDTO s2) {
				return s1.getDescripcion().compareTo(s2.getDescripcion());//el orden de la razon social dan el orden
			}
		});*/
		//model.addAttribute("entidades", entidadesDto);
		//return ControllerUtil.getTileName(Modulo.SEGURIDAD, Accion.SELECCIONAR_2);
    }
	
	@RequestMapping( value = "/SELECCIONAR_2", method = RequestMethod.POST )
    public String seleccionar2Post(@AuthenticationPrincipal Usuario usuario, 
    		@ModelAttribute("entidadDTO") LoginSelectDTO dto, Model model, RedirectAttributes redirectAttributes){
		//usuarioSrv.setSujetoAndSucursales(usuario, dto.getFirmaId());
		redirectAttributes.addFlashAttribute(WebAuthenticationEntryPoint.TarjetUrlAttribute, dto.getUser_target_url());	
		return ControllerUtil.redirectGET(Modulo.SEGURIDAD, Accion.SELECCIONAR_3);//forward solo se puede usar en RequestMethod.GET
    }
	
	@RequestMapping( value = "/SELECCIONAR_3", method = RequestMethod.GET )
    public String seleccionar3(@AuthenticationPrincipal Usuario usuario, 
    		Model model) throws Exception{
		LoginSelectDTO dto = new LoginSelectDTO();
		//dto.setFirmaId(usuario.getSujetoResponsabilidad().getId());
		if(model.containsAttribute(WebAuthenticationEntryPoint.TarjetUrlAttribute)){
			dto.setUser_target_url((String) model.asMap().get(WebAuthenticationEntryPoint.TarjetUrlAttribute));
			model.asMap().remove(WebAuthenticationEntryPoint.TarjetUrlAttribute);//para que no figure en el redirect!
		}
		model.addAttribute("entidadDTO", dto);
		//List<SucursalDTO> entidadesDto = respoUsuarioSrv.getSucursalesDTO(usuario, usuario.getResponsabilidad(), usuario.getSujetoResponsabilidad());
		//if(entidadesDto.size() == 1){//paso a home ya que tiene una sola sucursal
			//usuario.setSucursal(sucursalSrv.obtener(entidadesDto.get(0).getId()));
			if(StringUtils.isNotEmpty(dto.getUser_target_url())) {
				return "redirect:"+dto.getUser_target_url();
			}
			return ControllerUtil.forwardGET(Modulo.GENERAL, Accion.HOME, "showMapa=true");
		//}
/*		Collections.sort(entidadesDto, new Comparator<SucursalDTO>() {
			public int compare(SucursalDTO s1, SucursalDTO s2) {
				return s1.getCodigo().compareTo(s2.getCodigo());//el orden de la razon social dan el orden
			}
		});
		model.addAttribute("entidades", entidadesDto);*/
		//return ControllerUtil.getTileName(Modulo.SEGURIDAD, Accion.SELECCIONAR_3);
    }
	
	@RequestMapping( value = "/SELECCIONAR_3", method = RequestMethod.POST )
    public String seleccionar3Post(@AuthenticationPrincipal Usuario usuario, 
    		@ModelAttribute("entidadDTO") LoginSelectDTO dto, Model model){
		//usuarioSrv.setSucursal(usuario, dto.getSucursalId());
		if(StringUtils.isNotEmpty(dto.getUser_target_url())) {
			return "redirect:"+dto.getUser_target_url();
		}
		return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);//forward solo se puede usar en RequestMethod.GET
    }
}
