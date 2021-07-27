package ar.com.signals.trading.util.spring;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import ar.com.signals.trading.util.rest.MyRestGenericResponse;
import ar.com.signals.trading.util.rest.StatusCodes;

/**
 * Esta clase maneja todas las excepciones de los RestController
 * @author pepe@hotmail.com
 *
 */
@ControllerAdvice(annotations = RestController.class)//Target all Controllers annotated with @RestController
public class RestResponseEntityExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
	
	private static final Set<String> binaryContentType;
	static {
		binaryContentType = new HashSet<>();
		binaryContentType.add(MediaType.APPLICATION_PDF_VALUE);
		binaryContentType.add(MediaType.IMAGE_GIF_VALUE);
		binaryContentType.add(MediaType.IMAGE_JPEG_VALUE);
		binaryContentType.add(MediaType.IMAGE_PNG_VALUE);
		binaryContentType.add(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		binaryContentType.add("application/vnd.ms-excel");
		binaryContentType.add("application/zip");
	}
	
    
	/**
	 * ERROR: HttpMediaTypeNotAcceptableException: Could not find acceptable representation in exceptionhandler
	 * Esto se da cuando en el request se solicita por ejemplo un image/jpeg, 
	 * pero yo estoy queriendo devolver un application/json, entonces esa incompatibilad produce el error!
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
    protected ResponseEntity<MyRestGenericResponse<String>> handleException(Exception ex, HttpServletRequest request, HttpServletResponse response){
		if(request.getRequestURI()!=null) {
			logger.error("ExceptionHandler on " + request.getRequestURI());//para ver en que Rest se esta causando el error!
		}	
		BodyBuilder bb = null;
		if(binaryContentType.contains(request.getContentType())) {
			//si el cliente solicita un archivo binary y se produce una excepcion, entonces se le devuleve un json con el error!
			bb = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);//para que el cliente sepa que hubo un error! mas que nada para que jquery.binarytransport.js sepa que hubo un error y la respuesta es un json, no los que esperaba
		}else {
			//Devuelvo respuesta 200, pero dentro del json devuelto indico que hubo un error;
			bb = ResponseEntity.status(HttpStatus.OK);//el cliente sabe que hay un error porque mira el responseObject.statusCode
		}
		MyRestGenericResponse<String> responseObject = new MyRestGenericResponse<String>() {
			@Override
			public String getData() {
				return null;
			}
		};
		responseObject.setStatusCode(StatusCodes.ERROR_CLIENTE);
		responseObject.setObservaciones(ex.getMessage() + " - ["+ex.getClass()+"]");
		return bb.body(responseObject);
    }
}
