package ar.com.signals.trading.util.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class MyWebAuthenticationDetails extends WebAuthenticationDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String user_target_url;

    public MyWebAuthenticationDetails(HttpServletRequest request) {
    	super(request);
    	user_target_url = request.getParameter("user_target_url");
    }
	   
	public String getUser_target_url() {
		return user_target_url;
	}
}
