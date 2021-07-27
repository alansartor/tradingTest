package ar.com.signals.trading.seguridad.support;

public enum TipoPrivilegio {
	PrivilegioMenu, //Son acciones que aparecen en el Menu, requieren seguridad
	PrivilegioAccion, //Son acciones que NO aparecen en el Menu, requieren seguridad
	PrivilegioSoporte, //No son acciones y tampoco figuran en el menu, se usan en conjunto con Accion.ABM para asignar los PRODUCTOS que tiene permiso cierta responsabilidad a realizar alguna accion de ABM
					  //Tambien se usan en un caso particular en que queremos dejar una accion sin seguridad y que no se necesita asignar a la responsabilidad, ya que esa accion se llega desde otra accion, y esa accion redirecciona a una accion que si tiene seguridad
	PrivilegioMenuConfigAndSecurity,//Es lo mismo que PrivilegioMenu, pero en caso de iniciar session con una respo especial (superUsuario o usuarioMaestro) entonces el menu se arma solo con estas opciones!
	PrivilegioEvento//son los eventos que genera el sistema, entonces los usuarios se deben suscribir para recibir esos eventos, pero solo pueden suscribirse a eventos que estan como provilegios en su responsabilidad
}
