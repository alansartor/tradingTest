package ar.com.signals.trading.seguridad.rest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ar.com.signals.trading.util.rest.JacksonObjectMapperFactoryBean;

/**
 * @author pepe@hotmail.com
 *
 */
@RestController
@RequestMapping(value = "/api/SEGURIDAD")
public class LoginRestController {
	@Autowired private JacksonObjectMapperFactoryBean jacksonObjectMapper;
	
	/**
	 * Si se hace una consulta desde ajax y la sesion expiro, entonces la clase AjaxAwareAuthenticationEntryPoint redirecciona
	 * aca, donde se devuelve un JSON con mensaje de error
	 * @return
	 */
	@RequestMapping( value = "/REST_LOGIN", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ModelAndView restLogin(HttpServletResponse response){
		return jacksonObjectMapper.getJSonFailModel(response, "Sesion expirada");
	}
}
