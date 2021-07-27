package ar.com.signals.trading.general.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ibm.icu.util.Calendar;

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.SeguridadUtil;
import ar.com.signals.trading.trading.support.TrueFXDivisas;
import ar.com.signals.trading.trading.support.TrueFXSrv;
import ar.com.signals.trading.util.web.ChartCandlePointDTO;
import ar.com.signals.trading.util.web.ChartLineDataDTO;
import ar.com.signals.trading.util.web.ControllerUtil;
import ar.com.signals.trading.util.web.JQueryChart;
import ar.com.signals.trading.util.web.Mensaje;

@Controller
@RequestMapping("web/GENERAL/Trading")
public class TradingController {
	@Resource private TrueFXSrv trueFXSrv;
	
	@RequestMapping( value = "/GRAFICAR", method = RequestMethod.GET )
    public String graficar(@AuthenticationPrincipal Usuario usuario, Model model,
    		@RequestParam(required=false) TrueFXDivisas fltTrueFXDivisas) throws Exception{
		model.addAttribute("inicializar_con_datos", true);//para disparar la busqueda
		String inicializar_con_datos_notificacion = "Se grafican las Cotizaciones";
		if(fltTrueFXDivisas != null) {
			inicializar_con_datos_notificacion = "Se grafica la divisa " + fltTrueFXDivisas.getCodigo();
			model.addAttribute("ignorar_session_state", true);//si se un redireccion a listar con ciertos filtros especificos, entonces anulo el uso de los filtros guardados de la session! (caso contrario no voy a ver los filtros que quiero, voy a ver filtros que estube usando antes)	
		}else {
			inicializar_con_datos_notificacion = "Se grafica la divisa " + TrueFXDivisas.EUR_USD.getCodigo();
			fltTrueFXDivisas = TrueFXDivisas.EUR_USD;
		}
		model.addAttribute("trueFXDivisas", TrueFXDivisas.values());
		model.addAttribute("fltTrueFXDivisasValue", fltTrueFXDivisas);
		
		model.addAttribute("inicializar_con_datos_notificacion", StringUtils.remove(inicializar_con_datos_notificacion, "\""));
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.GENERAL, "Trading", Accion.GRAFICAR));
        return ControllerUtil.getTileName(Modulo.GENERAL, "Trading", Accion.GRAFICAR);
    }
	
	@RequestMapping( value = "/REST/BUSCAR", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody JQueryChart<ChartLineDataDTO<ChartCandlePointDTO>> buscar(
    		@AuthenticationPrincipal Usuario usuario, 
	        @RequestParam(required=true) TrueFXDivisas fltTrueFXDivisas) throws Exception {			
		List<Mensaje> mensajes = new ArrayList<>();
		//Collection<ChartLineDataDTO<ChartCandlePointDTO>> entidades = trueFXSrv.getRandomData(DateUtils.truncate(new Date(), Calendar.MINUTE), 30);
		Collection<ChartLineDataDTO<ChartCandlePointDTO>> entidades = trueFXSrv.getDatos(fltTrueFXDivisas, 60, mensajes);
	    JQueryChart<ChartLineDataDTO<ChartCandlePointDTO>> chartData = new JQueryChart<ChartLineDataDTO<ChartCandlePointDTO>>(entidades, mensajes);
	    return chartData;
    }
	
	@RequestMapping( value = "/GRAFICAR_2", method = RequestMethod.GET )
    public String graficar2(@AuthenticationPrincipal Usuario usuario, Model model) throws Exception{
		model.addAttribute("inicializar_con_datos", true);//para disparar la busqueda
		model.addAttribute("hora_inicial_index", 0);
		model.addAttribute("breadcrumbs", ControllerUtil.getBreadcrumb(Modulo.GENERAL, "Trading", Accion.GRAFICAR_2));
        return ControllerUtil.getTileName(Modulo.GENERAL, "Trading", Accion.GRAFICAR_2);
    }
	
	@RequestMapping( value = "/REST/BUSCAR_2", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody JQueryChart<ChartLineDataDTO<ChartCandlePointDTO>> buscar2(
    		@AuthenticationPrincipal Usuario usuario,
    		@RequestParam(required=true) Integer fltHoraIndex) throws Exception {					
		Collection<ChartLineDataDTO<ChartCandlePointDTO>> entidades = trueFXSrv.getDatosHistoricos(fltHoraIndex);
	    JQueryChart<ChartLineDataDTO<ChartCandlePointDTO>> chartData = new JQueryChart<ChartLineDataDTO<ChartCandlePointDTO>>(entidades);
	    return chartData;
    }
	
	/**
	 * Simulacion de busqueda de puntos de interes con los datos que estan en el archivo
	 * /HOME/TOMCAT/Campos/EURUSD.csv
	 * @param usuario
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( value = "/NUEVO", method = RequestMethod.GET )
    public String simular(@AuthenticationPrincipal Usuario usuario, Model model, RedirectAttributes redirectAttributes) throws Exception{
		List<Mensaje> mensajes = trueFXSrv.simularControlDatos();
		Mensaje.agregarMensajesAlModel(redirectAttributes, mensajes);
		return ControllerUtil.redirectGET(Modulo.GENERAL, Accion.HOME);
    }
}
