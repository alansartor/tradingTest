package ar.com.signals.trading.util.web;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FormateadorUtil {
	private static final Pattern inv_html_form_pattern_open = Pattern.compile("<form.*?>", Pattern.CASE_INSENSITIVE);//si dejamos el .* entonces el Matcher busca el mayor string que cumpla la condicion, pero si le agregamos el ? despues del * entonces el Matcher busca primero el menor string que cumpla
	private static final Pattern inv_html_form_pattern_close = Pattern.compile("</form>", Pattern.CASE_INSENSITIVE);
	private static final Pattern inv_html_form_textarea_pattern_open = Pattern.compile("<form.*?jsrs_Form.*?><textarea.*?jsrs_Payload.*?>", Pattern.CASE_INSENSITIVE);
	private static final Pattern inv_html_form_textarea_pattern_close = Pattern.compile("</textarea></form>", Pattern.CASE_INSENSITIVE);
	/**
	 * Pasa la primera letra a mayuscula y reemplaza '_' por ' '
	 * @param enumname
	 * @return
	 */
	public static String formatEnum(String enumname){
		if(enumname == null)
			enumname = "";
		enumname = StringUtils.capitalize(enumname);
		return enumname.replace('_', ' ');
	}

	/**
	 * Si es null, blanco o solo espacios, devuelve null
	 * De lo contrario, saca todos los espacios (inicio, fin y del medio) y pasa a mayusculas
	 * @param stringToClean
	 * @return
	 */
	public static String CLEANStringAndDeleteWhitespace(String stringToClean) {
		if(StringUtils.isBlank(stringToClean)) {
			return null;
		}
		return  StringUtils.deleteWhitespace(stringToClean).toUpperCase();
	}
	
	/**
	 * Si es null, blanco o solo espacios, devuelve null
	 * De lo contrario, saca los espacios del inicio y fin y pasa a mayusculas
	 * @param stringToClean
	 * @return
	 */
	public static String CLEANStringAndTrim(String stringToClean) {
		if(StringUtils.isBlank(stringToClean)) {
			return null;
		}
		return  stringToClean.trim().toUpperCase();
	}

	/**
	 * Da formato ppppnnnnnnnn
	 * Se recorta prefijo a 4 caracteres
	 * Se recorta numero a 8 caracteres
	 * @param nroRemitoPrefijo
	 * @param nroRemito
	 * @return
	 */
	public static String formatRemito(String nroRemitoPrefijo, String nroRemito) {
		nroRemitoPrefijo = StringUtils.substring(nroRemitoPrefijo, 0, 4);
		nroRemito = StringUtils.substring(nroRemito, 0, 8);
		return StringUtils.leftPad(nroRemitoPrefijo, 4, '0') + StringUtils.leftPad(nroRemito, 8, '0');
	}
	
	/**
	 * Equivale al metodo encodeURIComponent de JavaScript.
	 * Se usa para pasar datos a la pagina html, que luego son utilizados desde javaScript realizando 
	 * la operacion inversa decodeURIComponent
	 * Se utiliza en parseJsonAndEncodeURIComponent que primero pasa el objeto a Json y luego hace el encode
	 * 
	 * Creo que tiene una falla con el signo +, y se pierde este caracter si existe dentro del objeto original
	 * 
	 * @param component
	 * @return
	 */
	public static String encodeURIJson(String component)   {     
		String result = null;      
		try {       
			result = URLEncoder.encode(component, "UTF-8")   
				   .replaceAll("\\%28", "(")                          
				   .replaceAll("\\%29", ")")   		
				   .replaceAll("\\+", "%20")                          
				   .replaceAll("\\%27", "'")   			   
				   .replaceAll("\\%21", "!")
				   .replaceAll("\\%7E", "~");
		}catch (UnsupportedEncodingException e) {       
			result = component;     
		}      
		
		return result;   
	}
	
	public static String encodeURL(String url)   {     
		String result = null;      
		try {       
			return URLEncoder.encode(url, "UTF-8");
		}catch (UnsupportedEncodingException e) {       
			result = url;     
		}      
		return result;   
	}

	public static String parseJsonAndEncodeURIComponent(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(object);
		return encodeURIJson(jsonInString);
	}

	/**
	 * Devuelve el texto que esta entre los tags <form></form>
	 * @param htmlInv
	 * @return
	 */
	public static String cleanInvRespuesta(String htmlInv) {
        //<html><head></head><body onload="p=document.layers?parentLayer:window.parent;p.jsrsLoaded('');">jsrsPayload:<br><form name="jsrs_Form"><textarea name="jsrs_Payload">NO PUEDE ANULAR ESTE CIU YA QUE NO EXISTE UN ALTA O ALTA FUERA DE TERMINO PRESENTADO POR ESTA BODEGA.!!!</textarea></form></body></html>
        //<html><head></head><body onload="p=document.layers?parentLayer:window.parent;p.jsrsError('','jsrsError%3A+The+selected+function+doesn%5C%27t+exists');">jsrsError: The selected function doesn\'t exists</body></html>
        //<html><head></head><body onload="p=document.layers?parentLayer:window.parent;p.jsrsLoaded('');">jsrsPayload:<br><form name="jsrs_Form"><textarea name="jsrs_Payload">ESTA DECLARANDO COMO ALTA, LA FECHA DEL CIU NO DEBE SER ANTERIOR A LOS ULTIMOS 4 DIAS.-</textarea></form></body></html>
        //<html><head></head><body onload="p=document.layers?parentLayer:window.parent;p.jsrsLoaded('');">jsrsPayload:<br><form name="jsrs_Form"><textarea name="jsrs_Payload">NO Existe el Numero de CIU o el Viñedo, el Numero de CIU NO corresponde al Viñedo o bien el CIU esta dado de baja!!!.</textarea></form></body></html>
		if(StringUtils.isEmpty(htmlInv)) {
			return "Html vacio";
		}
		int from = 0;
		int to = htmlInv.length();
		Matcher matcher = inv_html_form_textarea_pattern_open.matcher(htmlInv);
		if(matcher.find()) {//busca el inicio del form <form...><textarea...>
			from = matcher.end();
			matcher = inv_html_form_textarea_pattern_close.matcher(htmlInv);
			if(matcher.find()) {//busca el fin del form </textarea></form>
				to = matcher.start();
			}
		}else {
			matcher = inv_html_form_pattern_open.matcher(htmlInv);
			if(matcher.find()) {//busca el inicio del form <form...>
				from = matcher.end();
				matcher = inv_html_form_pattern_close.matcher(htmlInv);
				if(matcher.find()) {//busca el fin del form </form>
					to = matcher.start();
				}
			}
		}
		return htmlInv.substring(from, to);
	}
	
	/**
	 * Devuelve el Texto que esta dentro del <textarea name="jsrs_Payload">
	 * @param html
	 * @return
	 * @throws Exception 
	 */
	public static String getInvPayLoad(String htmlInv) throws Exception {
		//Asi lo hace la libreria jsrsClient.js que utiliza el inv:
		//this.container.document.forms['jsrs_Form']['jsrs_Payload'].value;
		
		//<html><head></head><body onload="p=document.layers?parentLayer:window.parent;p.jsrsLoaded('');">jsrsPayload:<br><form name="jsrs_Form"><textarea name="jsrs_Payload">0</textarea></form></body></html>
		if(StringUtils.isNotEmpty(htmlInv)) {
			int from = 0;
			int to = htmlInv.length();
			Matcher matcher = inv_html_form_textarea_pattern_open.matcher(htmlInv);
			if(matcher.find()) {//busca el inicio del form <form...><textarea...>
				from = matcher.end();
				matcher = inv_html_form_textarea_pattern_close.matcher(htmlInv);
				if(matcher.find()) {//busca el fin del form </textarea></form>
					to = matcher.start();
				}
				return htmlInv.substring(from, to);
			}
		}
		throw new Exception("No se pudo extraer el Payload");
	}

	/**
	 * Transforma un numero decimal en formato '0.00'
	 * con '.' como separador decimal
	 * @param valor
	 * @return
	 */
	public static String formatDecimalOracle(BigDecimal valor) {
		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		DecimalFormat format = new DecimalFormat("0.00");
		format.setDecimalFormatSymbols(custom);
		return format.format(valor);
	}

	/**
	 * Devuelve array con el año, el mes y dia [yyyy, mm, dd] de la fecha que se pasa como parametro
	 * @return
	 */
	public static String[] getYearMesDayFolder(Timestamp fecha) {
		String fechaString = DateFormatUtils.format(fecha, "yyyy/MM/dd");
		return fechaString.split("/");
	}

	public static String cleanSpacesAndCharacter(String stringToClean, char characterRemove) {
		if(StringUtils.isBlank(stringToClean)) {
			return null;
		}
		return stringToClean.trim().replace(Character.toString(characterRemove), "");
	}
}
