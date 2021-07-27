package ar.com.signals.trading.seguridad.support;

/**
 * Max length=30
 * @author pepe@hotmail.com
 *
 */
public enum TipoResponsabilidad {
	//dejarlas en este orden, que es como se las muetra en la seleccion de responsabilidad
	UsuarioComun,//Usuario Comun: acceso a las pantallas de la responsabilidad asignada
	UsuarioMaestro,//Usuario Maestro: usuario que puede dar de alta usuario y asignar responsabilidades de nivel usuario comun
	SuperUsuario; //Super Usuario: puede crear responsabilidades
}