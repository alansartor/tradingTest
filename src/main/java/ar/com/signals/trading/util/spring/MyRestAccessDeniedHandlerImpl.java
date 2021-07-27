package ar.com.signals.trading.util.spring;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Clase que resuelve un error de seguridad cuando un Usuario consume un REST Service que no
 * tiene acceso. Por ahora devuelve un objeto JSON con mensaje de error, ver si detectar el tipo de request para 
 * ver que tipo de objeto devolver
 * @author Administrador
 *
 */
public class MyRestAccessDeniedHandlerImpl implements AccessDeniedHandler{
	private Logger logger = LoggerFactory.getLogger(MyRestAccessDeniedHandlerImpl.class);
	
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.warn("Acceso Denegado a :" + request.getRequestURL() + ". Error: " + accessDeniedException.getMessage());
		String jsonObject = "{"
				+ "\"status\": \"" + "RECURSO DENEGADO" + "\","
				+ "\"observaciones\":\"" + "No tiene privilegios para acceder a este recurso" + "\""//para que sean iguales que MyRestSimpleResponse, cambio message por observaciones
				+ "}";
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		PrintWriter out = response.getWriter();
		out.print(jsonObject);
		out.flush();
		out.close();
	}

}
