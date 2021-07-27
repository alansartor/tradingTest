package ar.com.signals.trading.util.spring;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.bind.annotation.RequestMethod;

import ar.com.signals.trading.util.web.FormateadorUtil;

public class WebAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	public static final String TarjetUrlAttribute = "user_target_url";
	
	@Autowired private ServletContext servletContext;
	
    public WebAuthenticationEntryPoint(String loginUrl) {
        super(loginUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    	if(RequestMethod.GET.name().equalsIgnoreCase(request.getMethod()) 
    			&& !request.getRequestURI().equals(this.getLoginFormUrl())) { 
    		String appName = servletContext.getContextPath();
    		if(!request.getRequestURI().startsWith(appName+"/web/SEGURIDAD/SELECCIONAR")){//si en vez de ir a la pagina de login, fue por error a la pagina de seleccion de respo, entonces se va a redireccionar al login, pero despues del login y de la seleccionde respo, no debo redireccionar a seleccionar de nuevo!
        		if(request.getRequestURI().startsWith(appName)) {
        			request.setAttribute(TarjetUrlAttribute, FormateadorUtil.encodeURL(request.getRequestURI().substring(appName.length()) + (StringUtils.isNotBlank(request.getQueryString())?"?" + request.getQueryString():"")));
        		}else {
        			request.setAttribute(TarjetUrlAttribute, FormateadorUtil.encodeURL(request.getRequestURI() + (StringUtils.isNotBlank(request.getQueryString())?"?" + request.getQueryString():"")));
        		}
    		}
    	}
    	super.commence(request, response, authException);
    }
 
    @Override
	protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
    	Object tarjetUrl = (Object) request.getAttribute(WebAuthenticationEntryPoint.TarjetUrlAttribute);
		String redirectUrl = super.determineUrlToUseForThisRequest(request, response, exception);
		if(tarjetUrl!=null) {
			if(redirectUrl.contains("?")) {
				redirectUrl += "&"+TarjetUrlAttribute+"="+tarjetUrl;
			}else {
				redirectUrl += "?"+TarjetUrlAttribute+"="+tarjetUrl;
			}
		}
		return redirectUrl;
	}
}