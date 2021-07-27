package ar.com.signals.trading.util.spring;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import ar.com.signals.trading.util.rest.JacksonObjectMapperFactoryBean;

public class MyMappingExceptionResolver extends SimpleMappingExceptionResolver{
	@Autowired private JacksonObjectMapperFactoryBean jacksonObjectMapper;
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
		
		if(AjaxAwareAuthenticationEntryPoint.isAjaxRequest(request)){
			logger.error("AjaxRequest: " + exception.getMessage(), exception);
			return jacksonObjectMapper.getJSonFailModel(response, exception.getMessage());
		}
		//TODO ver si hacer log del error y ver como pasar el printStackTrace a la view
		ModelAndView modelAndView = super.doResolveException(request, response, handler, exception);
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    exception.printStackTrace(pw);
	    modelAndView.addObject("stackTrace", sw.toString());//Se modifico la jsp para que haga un escape de los caracteres del stackTrace, enotnces no habria que hacer esto: .replaceAll("\r", "<br />"));
		return modelAndView;
	}
}
