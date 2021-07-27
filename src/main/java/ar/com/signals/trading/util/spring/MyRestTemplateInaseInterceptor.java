package ar.com.signals.trading.util.spring;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Interceptor para corregir Content-type a la Response de Inase debido a que
 * devuelve 'Content-Type: Content-Type: text/csv'
 * Este metodo intercepta el response antes que spring busque el MessageConverter, entonces le arreglamos el Content-Type
 * Para que la busqueda del MessageConverter no falle
 * @author computossl
 *
 */
public class MyRestTemplateInaseInterceptor implements ClientHttpRequestInterceptor {
	
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        response.getHeaders().remove("Content-Type");//saco el original que esta mal
        response.getHeaders().add("Content-Type", CsvHttpMessageConverter.CSV_MEDIA_TYPE.toString());//lo agrego correctamente 
        return response;
	}

}