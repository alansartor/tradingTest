package ar.com.signals.trading.util.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.signals.trading.configuracion.domain.PropiedadCategoria;
import ar.com.signals.trading.configuracion.service.PropiedadCategoriaSrv;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;

/**
 * Instructivo para poder usar OAuth2 autentification con Gmail!
 * https://chariotsolutions.com/blog/post/sending-mail-via-gmail-javamail/
 * 
 * Ver en EmailSrvImpl el paso a paso
 * 
 * Si en algun momento falla, puede ser porque el PROPIEDAD_MAILS_OAUTH2_REFRESH_TOKEN no se uso por 6 meses entonces Gmail lo revoco
 * En ese caso habria que generar uno nuevo, ver el archivo python de la pagina para generar un REFRESH_TOKEN nuevo
 * 
 * @author pepe@hotmail.com
 *
 */
@Service
@Transactional
public class OAuth2SrvImpl implements OAuth2Srv{
	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	private Logger logger = LoggerFactory.getLogger(OAuth2Srv.class);
	
	/**
	 * Devuelve el token actual, o gestiona uno nuevo si ya expiro
	 * @return
	 */
	public String getOAuth2Token() {
		//primero recupero el ultimo token y su fecha de vencimiento
		String lastToken = propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_MAILS_OAUTH2_TOKEN);
		Long tokenExpirationMiliseconds = propiedadCategoriaSrv.getLong(PropiedadCategoriaEnum.PROPIEDAD_MAILS_OAUTH2_TOKEN_EXPIRATION);
		Date expiration = new Date(tokenExpirationMiliseconds);
		if(expiration.before(new Date())) {//expiro, se debe gestionar un nuevo token
			String oauthClientId = propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_MAILS_OAUTH2_ID_CLIENT);
			String oauthSecret = propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_MAILS_OAUTH2_CLIENT_SECRET);
			String refreshToken = propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_MAILS_OAUTH2_REFRESH_TOKEN);
			String TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
			try {
	            String request = "client_id="+URLEncoder.encode(oauthClientId, "UTF-8")
	                    +"&client_secret="+URLEncoder.encode(oauthSecret, "UTF-8")
	                    +"&refresh_token="+URLEncoder.encode(refreshToken, "UTF-8")
	                    +"&grant_type=refresh_token";
	            HttpURLConnection conn = (HttpURLConnection) new URL(TOKEN_URL).openConnection();
	            conn.setDoOutput(true);
	            conn.setRequestMethod("POST");
	            PrintWriter out = new PrintWriter(conn.getOutputStream());
	            out.print(request); // note: println causes error
	            out.flush();
	            out.close();
	            conn.connect();
	            try {
	                HashMap<String,Object> result;
	                result = new ObjectMapper().readValue(conn.getInputStream(), new TypeReference<HashMap<String,Object>>() {});
	                lastToken = (String) result.get("access_token");
	                tokenExpirationMiliseconds = System.currentTimeMillis()+(((Number)result.get("expires_in")).intValue()*1000);
	                PropiedadCategoria propiedadCategoriaExpiration = propiedadCategoriaSrv.getPropiedad(PropiedadCategoriaEnum.PROPIEDAD_MAILS_OAUTH2_TOKEN_EXPIRATION);
	                propiedadCategoriaExpiration.setValor(tokenExpirationMiliseconds.toString());
	                Timestamp fechaActual = new Timestamp(new Date().getTime());
	                propiedadCategoriaExpiration.setLast_update_date(fechaActual);
	                PropiedadCategoria propiedadCategoriaToken = propiedadCategoriaSrv.getPropiedad(PropiedadCategoriaEnum.PROPIEDAD_MAILS_OAUTH2_TOKEN);
	                propiedadCategoriaToken.setValor(lastToken);
	                propiedadCategoriaToken.setLast_update_date(fechaActual);
	            } catch (IOException e) {
	                String line;
	                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	                String mensaje = "";
	                while((line = in.readLine()) != null) {
	                	mensaje += line;
	                }
	                logger.error("Fallo la gestion del token para poder enviar mails por gmail. Detalle: " + mensaje, e);
	            }
	        } catch (Exception e) {
	        	logger.error("Fallo la gestion del token para poder enviar mails por gmail. Detalle: " + e.getMessage(), e);
	        }
		}
		return lastToken;
	}
}
