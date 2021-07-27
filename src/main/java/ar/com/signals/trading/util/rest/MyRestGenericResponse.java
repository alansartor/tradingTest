package ar.com.signals.trading.util.rest;


/**
 * 
 * @author Pepe
 *
 */
public abstract class MyRestGenericResponse<T> extends MyRestSimpleResponse{	
	public abstract T getData();
	
	public void setData(T data) {}
}
