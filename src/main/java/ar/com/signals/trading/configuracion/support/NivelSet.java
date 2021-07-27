package ar.com.signals.trading.configuracion.support;

/**
 * alcance de las categorias del juego
 * 
 * Max length=30
 * 
 * @author pepe@hotmail.com
 *
 */
public enum NivelSet {
	global, //se aplica a todos
	sujeto,//se aplica solo a una empresa (quiere decir que en la configuracion tiene que haber una referencia a la Empresa)
	sucursal,//se aplica solo a una planta (quiere decir que en la configuracion tiene que haber una referencia a Planta)
	unidadProductiva
}
