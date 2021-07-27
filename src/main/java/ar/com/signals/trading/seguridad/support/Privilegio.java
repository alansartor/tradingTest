package ar.com.signals.trading.seguridad.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.util.web.ControllerUtil;
/**
 * Max lengh 60
 * 
 * @author pepe@hotmail.com
 *
 */
public enum Privilegio implements GrantedAuthority{	
	USUARIO_NUEVO(Modulo.SEGURIDAD, "Usuario", Accion.NUEVO, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),
	USUARIO_EDITAR(Modulo.SEGURIDAD, "Usuario", Accion.EDITAR, TipoPrivilegio.PrivilegioAccion),
	/*USUARIO_ELIMINAR(Modulo.SEGURIDAD, "Usuario", Accion.ELIMINAR, TipoPrivilegio.PrivilegioAccion, Servidor.MSC),*/
	USUARIO_LISTAR(Modulo.SEGURIDAD, "Usuario", Accion.LISTAR, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),
	USUARIO_RESETEAR(Modulo.SEGURIDAD, "Usuario", Accion.RESETEAR, TipoPrivilegio.PrivilegioAccion),
	USUARIO_EDITAR_2(Modulo.SEGURIDAD, "Usuario", Accion.EDITAR_2, TipoPrivilegio.PrivilegioAccion),//configuracion de usuario
	
	RESPONSABILIDAD_NUEVO(Modulo.SEGURIDAD, "Respo", Accion.NUEVO, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),
	RESPONSABILIDAD_EDITAR(Modulo.SEGURIDAD, "Respo", Accion.EDITAR, TipoPrivilegio.PrivilegioAccion),
	RESPONSABILIDAD_LISTAR(Modulo.SEGURIDAD, "Respo", Accion.LISTAR, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),
	RESPONSABILIDAD_VER(Modulo.SEGURIDAD, "Respo", Accion.VER, TipoPrivilegio.PrivilegioAccion),
	
	RESPOUSUARIO_NUEVO(Modulo.SEGURIDAD, "RespoUsuario", Accion.NUEVO, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),
	RESPOUSUARIO_EDITAR(Modulo.SEGURIDAD, "RespoUsuario", Accion.EDITAR, TipoPrivilegio.PrivilegioAccion),
	RESPOUSUARIO_LISTAR(Modulo.SEGURIDAD, "RespoUsuario", Accion.LISTAR, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),
		
	PROPIEDADCATEGORIA_NUEVO(Modulo.CONFIGURACION, "PropiedadCategoria", Accion.NUEVO, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),
	PROPIEDADCATEGORIA_EDITAR(Modulo.CONFIGURACION, "PropiedadCategoria", Accion.EDITAR, TipoPrivilegio.PrivilegioAccion),
	PROPIEDADCATEGORIA_LISTAR(Modulo.CONFIGURACION, "PropiedadCategoria", Accion.LISTAR, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),
	PROPIEDADCATEGORIA_VER(Modulo.CONFIGURACION, "PropiedadCategoria", Accion.VER, TipoPrivilegio.PrivilegioAccion),
	
	//no las muestro en el menu comun, la muestro dentro de configuracion (junto con configuracion de usuario)
	//un usuario solo puede crear o eliminar sus propias suscripciones! (no puse una forma de limitar a que evento se puede suscribir un usuario, por si un dueño no quiere q tales usuarios vean tal evento)
	SUSCRIPCION_NUEVO(Modulo.EVENTOS, "Suscripcion", Accion.NUEVO, TipoPrivilegio.PrivilegioAccion),
	//SUSCRIPCION_EDITAR(Modulo.SEGURIDAD, "Suscripcion", Accion.EDITAR, TipoPrivilegio.PrivilegioAccion),
	SUSCRIPCION_LISTAR(Modulo.EVENTOS, "Suscripcion", Accion.LISTAR, TipoPrivilegio.PrivilegioAccion),
	SUSCRIPCION_ELIMINAR(Modulo.EVENTOS, "Suscripcion", Accion.ELIMINAR, TipoPrivilegio.PrivilegioAccion),
	
	//Pantalla para configurar cuando recibir alertas de de que, esta todo unificado en una sola pantalla
	SUSCRIPCIONCONFIG_EDITAR(Modulo.EVENTOS, "SuscripcionConfig", Accion.CONFIGURAR, TipoPrivilegio.PrivilegioAccion),
	
	REGISTRONOTIFICACION_LISTAR(Modulo.EVENTOS, "RegistroNotificacion", Accion.LISTAR, TipoPrivilegio.PrivilegioAccion),//se muestra en la campana de ver ultimas notificaciones
	
	TRADING_GRAFICAR(Modulo.GENERAL, "Trading", Accion.GRAFICAR, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),//TODO temporal, para hacer unas pruebas de traiding
	TRADING_GRAFICAR_2(Modulo.GENERAL, "Trading", Accion.GRAFICAR_2, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),//TODO temporal, para hacer unas pruebas de traiding
	TRADING_SIMULAR(Modulo.GENERAL, "Trading", Accion.NUEVO, TipoPrivilegio.PrivilegioMenuConfigAndSecurity),//TODO temporal, para hacer unas pruebas de traiding
	
	//son eventos a los que se pueden suscribir los usuarios, se manejan de otra forma
	EVENTO_TRADING_ALERT(Modulo.EVENTOS, "TradingAlert", Accion.INFORMAR, TipoPrivilegio.PrivilegioEvento);//TODO temporal, para hacer pruebas de trading
	
	private final Modulo modulo;
	private final String clase;
	private String subClase;
	private final Accion accion;
	private final TipoPrivilegio tipoPrivilegio;
	//private final Servidor servidor;//puede ser null, significa que el TipoPrivilegio.Privilegio se usa tanto en el server central, como en el de planta
	//private String descripcion;
	
	//propiedades opcionales, se usan para mostrar algunas opciones en otros menu en vez del por defecto!
	private Modulo moduloMenu;
	private String claseMenu;
	
	private String icon;
	
	private Privilegio(Modulo modulo, String clase, String subClase, Accion accion, TipoPrivilegio tipoPrivilegio, Modulo moduloMenu, String claseMenu, String icon) {
		this.modulo = modulo;
		this.clase = clase;
		this.subClase = subClase;
		this.accion = accion;
		this.tipoPrivilegio = tipoPrivilegio;
		this.moduloMenu = moduloMenu;
		this.claseMenu = claseMenu;
		this.icon = icon;
	}
	
	private Privilegio(Modulo modulo, String clase, String subClase, Accion accion, TipoPrivilegio tipoPrivilegio, Modulo moduloMenu, String claseMenu) {
		this(modulo, clase, subClase, accion, tipoPrivilegio, moduloMenu, claseMenu, null);
	}
	
	private Privilegio(Modulo modulo, String clase, String subClase, Accion accion, TipoPrivilegio tipoPrivilegio, Modulo moduloMenu) {
		this(modulo, clase, subClase, accion, tipoPrivilegio, moduloMenu, null, null);
	}
	
	private Privilegio(Modulo modulo, String clase, Accion accion, TipoPrivilegio tipoPrivilegio, Modulo moduloMenu) {
		this(modulo, clase, null, accion, tipoPrivilegio, moduloMenu, null, null);
	}
	
	private Privilegio(Modulo modulo, String clase, String subClase, Accion accion, TipoPrivilegio tipoPrivilegio, String icon) {
		this(modulo, clase, subClase, accion, tipoPrivilegio, null, null, icon);
	}
	
	private Privilegio(Modulo modulo, String clase, String subClase, Accion accion, TipoPrivilegio tipoPrivilegio) {
		this(modulo, clase, subClase, accion, tipoPrivilegio, null, null, null);
	}
		
	private Privilegio(Modulo modulo, String clase, Accion accion, TipoPrivilegio tipoPrivilegio, String icon) {
		this(modulo, clase, null, accion, tipoPrivilegio, null, null, icon);
	}
	
	private Privilegio(Modulo modulo, String clase, Accion accion, TipoPrivilegio tipoPrivilegio) {
		this(modulo, clase, null, accion, tipoPrivilegio, null, null, null);
	}

	@Override
	public String getAuthority() {
		return name();
	}
	
	public String getLink() {
		return ControllerUtil.getTileRequestGET2(modulo, clase, subClase, accion);
	}
	
	public Modulo getModulo() {
		return modulo;
	}

	public String getClase() {
		return clase;
	}

	public Accion getAccion() {
		return accion;
	}
	
	public int getOrdinal() {
		return ordinal();
	}

	public String getSubClase() {
		return subClase;
	}

	public void setSubClase(String subClase) {
		this.subClase = subClase;
	}

	public TipoPrivilegio getTipoPrivilegio() {
		return tipoPrivilegio;
	}
	
	public Modulo getModuloMenu() {
		return moduloMenu!=null?moduloMenu:modulo;
	}
	
	public String getClaseMenu() {
		return claseMenu!=null?claseMenu:clase;
	}

	public String getIcon() {
		return icon;
	}
	public String getIconSpan() {
		return icon!= null?Accion.getIconForMenu(icon):Accion.getIconForMenu(accion);
	}
	
	/**
	 * Devuleve el codigo para buscar el texto en el archivo messages para este Privilegio
	 * @return
	 */
	public String getMenuMessageSourceCode() {
		return "menu."+this.getModulo().name()+"."+this.getClaseMenu()+(StringUtils.isNotBlank(subClase)?"."+ subClase:"")+"."+this.getAccion().name();
	}
}
