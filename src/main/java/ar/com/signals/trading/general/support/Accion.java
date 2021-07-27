package ar.com.signals.trading.general.support;

import com.google.common.util.concurrent.Monitor;

import ar.com.signals.trading.seguridad.domain.Usuario;

public enum Accion{
	LOGIN, HOME, NUEVO, ELIMINAR, EDITAR, NUEVO_POPUP, EDITAR_POPUP,
	//NUEVO_2,//se usa para mostrar pantalla alternativa para ingresar uva sin orden de compra
	EDITAR_2,//se usa para configurar la password del usuario
	ABM, //accion especial, no se usa como una opcion de menu o una accion sobre una entidad, sino que se usa para asignar categorias a las Acciones NUEVO, ELIMINAR, EDITAR, LISTAR. Ejemplo para ABM de ResultadoAnalisis, se usa para asignar los PRODUCTOS que tiene permiso cierta responsabilidad a realizar alguna accion de ABM
	COMPLETAR, //se usa para realizar una accion del circuito distinta de la que inicia un circuito. Una accion del circuito que inicia el movimiento se hace con NUEVO
	PASO_UNO, PASO_DOS, PASO_TRES,//se utiliza en los wizard, es decir cuando se deben completar datos en etapas
	VER, VER_POPUP, //ver registro en modo read only
	LISTAR, LISTAR_POPUP,//si una entidad tiene varias opciones para listar, entonces hay que armar otras Acciones: LISTAR_2, LIATAR_3 .. y poner un nombre en mensajes 
	LISTAR_2,
	LISTAR_HISTORICO, //los movimientos se dividen en movimientos vivos e historicos, entonces todas las entidades relacionadas se separan en dos listados, los vivos y los historicos
	//LISTAR_RO, //no se usa mas, usar listar y mostrar boton editar o ver segun si el usuario tiene esos privilegios o no, lo mismo con el boton nueva entidad
	SELECCIONAR, SELECCIONAR_2, SELECCIONAR_3, GENERAR, PRESENTAR, INFORMAR, IMPORTAR, DESCARGAR,
	GRAFICAR, GRAFICAR_2,
	RESETEAR,//se usa para resetear la password del usuario y para resetar telegram
	CONFIGURAR;
	
	public static String getIconForOption(Accion accion) {
		String iconName = getIconName(accion);
		if(iconName != null) {
			return getIconSpan(iconName);
		}else {
			return "<span>"+accion.name()+"</span>";
		}
	}
	
	public static String getIconSpan(String iconName) {
		return "<span class='"+iconName+" my-form-icon'></span>";
	}
	
	private static String getIconName(Accion accion) {
		switch(accion) {
		case NUEVO:
		case PASO_UNO:
		case PASO_DOS:
		case PASO_TRES:
			return "fas fa-plus";
		case EDITAR:
		case EDITAR_2:
			return "glyphicon glyphicon-pencil";
		case ELIMINAR:
			return "glyphicon glyphicon-trash";
		case VER:
			return "fas fa-binoculars";
		case LISTAR:
			return "glyphicon glyphicon-list";
		case DESCARGAR:
			return "glyphicon glyphicon-download-alt";
		case GENERAR:
			return "fas fa-cogs";
		case GRAFICAR:
			return "far fa-chart-bar";
		default:
			return null;
		}
	}
	
	/**
	 * Es similar al anterior, pero si no existe, entonces se devuelve empty String, y ademas se agrega un espacio luego del span
	 * @param accion
	 * @return
	 */
	public static String getIconForMenu(Accion accion) {
		String iconName = getIconName(accion);
		if(iconName != null) {
			return "<span class='"+iconName+" my-form-icon'></span> ";
		}else {
			return "";
		}
	}
	
	public static String getIconForMenuAction(String accion) {
		return getIconForMenu(Accion.valueOf(accion));
	}
	
	public static String getIconForMenu(String iconName) {
		if(iconName != null) {
			return "<span class='"+iconName+"  my-form-icon'></span> ";
		}else {
			return "";
		}
	}

	public static String getIconClaseForForm(String clase) {
		String iconName = getIconClase(clase);
		if(iconName != null) {
			return " <span class='"+iconName+" my-form-icon'></span>";
		}else {
			return "";
		}
	}

	public static String getIconClase(String claseMenu) {
		if(claseMenu.equals(Usuario.class.getSimpleName())) {
			return "fas fa-user";
		}
		return null;
	}
}

