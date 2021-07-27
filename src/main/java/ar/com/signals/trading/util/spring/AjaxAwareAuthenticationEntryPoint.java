package ar.com.signals.trading.util.spring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    public AjaxAwareAuthenticationEntryPoint(String loginUrl) {
        super(loginUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //Siemrpe va a entrar por ajax porque este Custom EntryPoint solo se usa para los REST Request
    	//if (isAjaxRequest(request)) {
			//response.sendError(HttpServletResponse.SC_FORBIDDEN, "SESSION_TIMED_OUT");
        //} else {
            request.setAttribute("targetUrl", request.getRequestURL());
            super.commence(request, response, authException);
       //}
    }
 
    @Override
	protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
    	if (isAjaxRequest(request)) {//Si se llama desde ajax, entonces redirecciono a un request que devuelve un JSON
			return "/api/SEGURIDAD/REST_LOGIN";
		}
		return super.determineUrlToUseForThisRequest(request, response, exception);
	}

	protected static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}