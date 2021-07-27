package ar.com.signals.trading.util.spring;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import ar.com.signals.trading.util.rest.JacksonObjectMapperFactoryBean;

/**
 * Con esta clase resuelvo las excepciones NoHandlerFoundException que el dispatcher arroja cada vez que 
 * un usuario quiere acceder a un recurso que no existe (el dispatcher no encuentra un Handler)
 * Se devuelve el error en formato JSON (si el reques era para un /rest) o la pantalla de error si el request era para una /web
 * 
 * IMPORTANTE: para que el dispatcher arroje la excepcion NoHandlerFoundException se debe configurar en web.xml throwExceptionIfNoHandlerFound=true
 * @author Pepe
 *
 */
@ControllerAdvice
public class MyNoHandlerFoundControllerAdvice {
	@Autowired private JacksonObjectMapperFactoryBean jacksonObjectMapper;
	
	@ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException exception) throws NoHandlerFoundException {
		if(AjaxAwareAuthenticationEntryPoint.isAjaxRequest(request)){
			return jacksonObjectMapper.getJSonFailModel(response, "El recurso al que esta queriendo acceder no existe (" + exception.getMessage()+")");
		}
		//TODO ver si hacer log del error y ver como pasar el printStackTrace a la view
		ModelAndView modelAndView = new ModelAndView("GENERAL.error.EXCEPTION");//super.doResolveException(request, response, null, exception);
		modelAndView.addObject("exception", exception);
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    exception.printStackTrace(pw);
		modelAndView.addObject("stackTrace", sw.toString());//Se modifico la jsp para que haga un escape de los caracteres del stackTrace, enotnces no habria que hacer esto: .replaceAll("\r", "<br />"));
		return modelAndView;
    }
}
