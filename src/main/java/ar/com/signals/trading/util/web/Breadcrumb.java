package ar.com.signals.trading.util.web;

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;

public class Breadcrumb {
	private Modulo modulo;
	private String clase;
	private String subClase;
	private Accion accion;
	//private String link;
	private boolean active;
	private String idEntidad;

	public Breadcrumb(Modulo modulo, Accion accion) {
		this(modulo, null, null, accion, false, null);
	}
	
	public Breadcrumb(Modulo modulo, String clase, Accion accion) {
		this(modulo, clase, null, accion, false, null);
	}
	
	public Breadcrumb(Modulo modulo, String clase, String subClase, Accion accion) {
		this(modulo, clase, subClase, accion, false, null);
	}
	
	public Breadcrumb(Modulo modulo, String clase, Accion accion, boolean active, Object idEntidad) {
		this(modulo, clase, null, accion, active, idEntidad);
	}
	
	public Breadcrumb(Modulo modulo, String clase, String subClase, Accion accion, boolean active, Object idEntidad) {
		this.modulo = modulo;
		this.clase = clase;
		this.subClase = subClase;
		this.accion = accion;
		this.active = active;
		//this.link = ControllerUtil.getTileRequestGET(modulo, clase, subClase, accion);
		this.setIdEntidad(idEntidad != null?String.valueOf(idEntidad):null);
	}

	public String getCode() {
		return "breadcrumb" + "." + accion.name();
	}
	public Accion getAccion() {
		return accion;
	}
	public String getLink() {
		return ControllerUtil.getTileRequestGET2(modulo, clase, subClase, accion);
	}
/*	public String getClaseName() {
		return StringUtils.isNotBlank(clase)?clase:null;
	}*/
	public String getSubClase() {
		return subClase;
	}
	public boolean isActive() {
		return active;
	}
	public String getIdEntidad() {
		return idEntidad;
	}
	public void setIdEntidad(String idEntidad) {
		this.idEntidad = idEntidad;
	}
	public Modulo getModulo() {
		return modulo;
	}
	public String getClase() {
		return clase;
	}
}
