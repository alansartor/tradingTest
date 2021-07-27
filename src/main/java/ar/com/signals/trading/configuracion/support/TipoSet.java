package ar.com.signals.trading.configuracion.support;

/**
 * Uso de las categorias del juego
 * 
 * Max length=30
 * 
 * @author pepe@hotmail.com
 *
 */
public enum TipoSet {
	agrupador,//No tienen un valor, solo se usan para Agrupar entidades
	caracteristica,//No tienen valor, se usa para asignarle una propiedad a una entidad
	valor, //tiene valor
	equivalencia, //tiene valor
	workflow, //no tiene valor
	id//tien valor, hace referencia a un id de un registro de alguna tabla
}
