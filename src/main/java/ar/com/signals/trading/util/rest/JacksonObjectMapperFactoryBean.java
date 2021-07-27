package ar.com.signals.trading.util.rest;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.signals.trading.util.spring.MyTextView;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;

public class JacksonObjectMapperFactoryBean implements FactoryBean<ObjectMapper>{
    private boolean insensitive;

    public Class<?> getObjectType(){
    	return ObjectMapper.class;
    }
	@Override
	public boolean isSingleton() {
		return true;
	}
	
	public boolean isInsensitive() {
		return insensitive;
	}

	public void setInsensitive(boolean insensitive) {
		this.insensitive = insensitive;
	}

	public ObjectMapper getObject(){
		return new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, insensitive);
	}
	
	/**
	 * 
	 * @param response
	 * @param mensaje
	 * @return
	 */
	public ModelAndView getJSonModel(HttpServletResponse response, int satus, String message, Object data){
		response.reset();
        response.setStatus(satus);
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        Map<String, Object> asd = new HashMap<String, Object>();
        asd.put("observaciones", message);//para que sean iguales que MyRestSimpleResponse, cambio message por observaciones
        if(data != null) {
        	asd.put("data", data);
        }
        view.setAttributesMap(asd);//agregar los atributos luego de llenar el map
        return new ModelAndView(view);
	}
	
	
	/**
	 * 
	 * @param response
	 * @param mensaje
	 * @return
	 */
	public ModelAndView getTextModel(HttpServletResponse response, String message, Object data){
		response.reset();
        response.setStatus(HttpServletResponse.SC_OK);

        MyTextView view = new MyTextView();
        Map<String, Object> asd = new HashMap<String, Object>();
        asd.put("observaciones", message);//para que sean iguales que MyRestSimpleResponse, cambio message por observaciones
        if(data != null) {
        	asd.put("data", data);
        }
        view.setAttributesMap(asd);//agregar los atributos luego de llenar el map
        return new ModelAndView(view);
	}
	
	/**
	 * 
	 * @param response
	 * @param mensaje
	 * @return
	 */
	public ModelAndView getJSonFailModel(HttpServletResponse response, String message){
		return getJSonModel(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, null);
	}
	
	/**
	 * 
	 * @param response
	 * @param mensaje
	 * @return
	 */
	public ModelAndView getJSonModel(HttpServletResponse response, String message, Object data){
		return getJSonModel(response, HttpServletResponse.SC_OK, message, data);
	}
	
	/**
	 * 
	 * @param response
	 * @param mensaje
	 * @return
	 */
	public ModelAndView getJSonModel(HttpServletResponse response, String message){
		return getJSonModel(response, HttpServletResponse.SC_OK, message, null);
	}
	
	/**
	 * Resuelve la respuesta de un popUp cuando es correcta, ya que se debe devolver un json con 
	 * la respuesta, y hay un caso particular que cuando el pedido viene de in Internet Explorer, se debe
	 * devolver texto plano, porque el json es rechazado
	 * @param request
	 * @param response
	 * @param message
	 * @param data
	 * @return
	 */
	public ModelAndView resolvePopUpResponse(HttpServletRequest request, HttpServletResponse response, String message, Object data) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		if(userAgent != null && userAgent.getBrowser() != null && Browser.IE.equals(userAgent.getBrowser().getGroup())){
			//IE issue
			return this.getTextModel(response, message, data);
		}
		return this.getJSonModel(response, message, data);
	}
}