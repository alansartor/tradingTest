package ar.com.signals.trading.ayuda.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ar.com.signals.trading.ayuda.suport.AyudaEnum;
import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.service.RespoSrv;
import ar.com.signals.trading.util.web.ControllerUtil;


/**
 * Para mostrar el Explorador de Ayudas y las paginas de Ayuda
 * @author Pepe
 *
 */
@Controller
@RequestMapping("web/AYUDA/Ayuda")
public class AyudaController {
	@Resource private RespoSrv responsabilidadSrv;
	
	/**
	 * Explorador de ayudas
	 * @param model
	 * @param ayuda
	 * @param popUpMode
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/LISTAR", method = RequestMethod.GET )
    public String listar(Model model, @RequestParam(value = "ayuda", required = false) AyudaEnum ayuda, @RequestParam(value = "popUpMode", required = false) Boolean popUpMode, HttpServletResponse response){        
		//model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.GENERAL, Sucursal.class, Accion.NUEVO));
		model.addAttribute("ayudasDTOs", AyudaEnum.obtenerAyudasDTO());
		model.addAttribute("ayuda", ayuda);//para inicializar con esa ayuda
		
		if(popUpMode != null && popUpMode) {
			response.setHeader("X-Frame-Options", "SAMEORIGIN");// policy="SAMEORIGIN"
		}
        return ControllerUtil.getTileName(Modulo.AYUDA, "Ayuda", popUpMode != null && popUpMode?Accion.LISTAR_POPUP:Accion.LISTAR);
    }
	
	/**
	 * Paginas de Ayuda
	 * @param model
	 * @param ayuda
	 * @param popUpMode
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/VER", method = RequestMethod.GET )
    public String ver(Model model, @RequestParam(value = "ayuda", required = true) AyudaEnum ayuda, HttpServletResponse response){        
        return ControllerUtil.getTileName(Modulo.AYUDA, ayuda.name(), Accion.VER);
    }
	
/*	@RequestMapping( value = "/VER", method = RequestMethod.GET )
    public String ver(Model model, @RequestParam(value = "ayuda", required = true) AyudaEnum ayuda, @RequestParam(value = "popUpMode", required = false) Boolean popUpMode, HttpServletResponse response){        
		//model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.GENERAL, Sucursal.class, Accion.NUEVO));
		if(popUpMode != null && popUpMode) {
			response.setHeader("X-Frame-Options", "SAMEORIGIN");// policy="SAMEORIGIN"
		}
        return ControllerUtil.getTileName(Modulo.AYUDA, ayuda.name(), popUpMode != null && popUpMode?Accion.VER_POPUP:Accion.VER);
    }*/
}
