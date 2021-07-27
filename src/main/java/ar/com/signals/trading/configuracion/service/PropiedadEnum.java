package ar.com.signals.trading.configuracion.service;

@Deprecated
public enum PropiedadEnum {
	MEDICION_MOVIMIENTOS_TOLERANCIA(Long.class, "15000", "Kilogramos de diferencia entre el stock calculado por mediciones y el calculado por movimientos a partir del cual se generan alarmas"),
	STOCK_MINIMO_EN_TANQUE(Long.class, "0", "Kilogramos base de stock calculado por movimientos que debe existir para tirar alarma de falta de medicion"),
	ENTORNO_IMPLEMENTANDO_CAMBIO(Boolean.class, "true", "Se utiliza cuando se estan implementando nuevas funcionalidades, para mejorar la migracion");
	
	private Class<?> clase;
	private String defaultValue;
	private String observacion;
	
	private PropiedadEnum(Class<?> clase, String defaultValue, String observacion) {
		this.clase = clase;
		this.defaultValue = defaultValue;
		this.observacion = observacion;
	}

	public Class<?> getClase() {
		return clase;
	}

	public void setClase(Class<?> clase) {
		this.clase = clase;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
}
