package ar.com.signals.trading.configuracion.support;

/**
 * Max length=40
 * @author pepe@hotmail.com
 *
 */
public enum TipoDato {
	String, Integer, Long, Double, BigDecimal, Array, Array_Contenedor, Enum, Boolean; //NEW 03/09/2020 vuelvo a agregar boolean, ya que si se necesita para propiedades comunes! Ver bien el uso de activo, ya que no queda claro para que sirve, y no esta bien la logica, quiza si se usa para productoCategoria, pero no para propiedadCategoria //BOOLEAN lo saco, ya que si tiene la categoria asignada y activa entonces es true, sino es false
}
