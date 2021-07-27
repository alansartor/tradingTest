package ar.com.signals.trading.configuracion.support;

import ar.com.signals.trading.configuracion.support.TipoDato;

/**
 * Son las Categorias que se utilizan para las PropiedadCategoria
 * Deben existir en la base de datos
 * Max length=50
 * @author Pepe
 *
 */
public enum PropiedadCategoriaEnum {//la descripcion no debe superar los 255 caracteres
	//30/07/2019 cambio la url del inase!
	PROPIEDAD_MAILS_REPORTE_ERRORES("Son los mails a los que le van a llegar los reportes de error que disparen los Usuarios", "pepe@hotmail.com", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.Array),
	PROPIEDAD_MAILS_OAUTH2_ID_CLIENT("Es el Client Id", "tertrete", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.Array),
	PROPIEDAD_MAILS_OAUTH2_CLIENT_SECRET("Es el Client Secret", "ertert", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String),
	PROPIEDAD_MAILS_OAUTH2_REFRESH_TOKEN("Es el refres Token,  This is generally long-lived, expiring after six months of disuse or after too many other refresh tokens have been generated", "rrettert", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String),
	PROPIEDAD_MAILS_OAUTH2_TOKEN("Es el ultimo token generado", "ertertretr", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String),
	PROPIEDAD_MAILS_OAUTH2_TOKEN_EXPIRATION("Es la fecha de expiracion del ultimo token en miliseguntos", "1581626633456", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.Long),
	PROPIEDAD_DIRECTORIO_HOME("Es el directorio donde se guardan los archivos que genera o utiliza el servidor", "/HOME/TOMCAT/TRADING/",PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String),
	
	//para conseguir el token, hay que crear el bot en telegram usando el @BotFather
	PROPIEDAD_BOTNAME_TELEGRAM("Nombre del Bot del Sistema de Telegram", "TraiElsadtBot", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String),
	PROPIEDAD_URL_TELEGRAM("Url de telegram, para hacer las llamadas al Bot Api de Telegram", "https://api.telegram.org", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String),
	PROPIEDAD_TOKEN_TELEGRAM("Token del bot, para hacer las llamadas al Bot Api de Telegram", "ertertretertret", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String),
	PROPIEDAD_INICIAR_TELEGRAM("Iniciar la conexion con Telegram en el arranque (para que tome efecto un cambio, hay que reiniciar tomcat)", "false", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.Boolean),
	
	PROPIEDAD_INICIAR_THREAD_TRUEFX("Iniciar el hilo que se conectara indefinidamente a la pagina de truefx", "false", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.Boolean),
	PROPIEDAD_USUARIO_TRUEFX("Usuario para iniciar session en la pagina truefx", "erty", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String),
	PROPIEDAD_URL_TRUEFX("Url para conectarse a truefx", "https://webrates.truefx.com/rates/connect.html", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String),
	
	PROPIEDAD_URL_SISTEMA("Url externa del Sistema, se usa para armar links que se envian afuera del entorno por lo que no se puede usar relative path", "http://localhost:8080/trading", PropiedadCategoriaSetEnum.PROPIEDAD_CONFIGURACIONES_GLOBAL, TipoDato.String);

	private String descripcion;
	private String defaultValue;
	private PropiedadCategoriaSetEnum categoriaSet;
	private TipoDato tipoDato;
	
	private PropiedadCategoriaEnum(String descripcion, String defaultValue, PropiedadCategoriaSetEnum categoriaSet, TipoDato tipoDato) {
		this.setDescripcion(descripcion);
		this.categoriaSet = categoriaSet;
		this.tipoDato = tipoDato;
		this.defaultValue = defaultValue;
	}
	
	public PropiedadCategoriaSetEnum getCategoriaSet() {
		return categoriaSet;
	}
	public void setCategoriaSet(PropiedadCategoriaSetEnum categoriaSet) {
		this.categoriaSet = categoriaSet;
	}
	public TipoDato getTipoDato() {
		return tipoDato;
	}
	public void setTipoDato(TipoDato tipoDato) {
		this.tipoDato = tipoDato;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
