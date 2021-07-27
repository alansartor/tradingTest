package ar.com.signals.trading.general.support;

import org.apache.commons.lang3.StringUtils;

public enum Modulo {
	ESTABLECIMIENTO(1, "far fa-map"),
	PRODUCCION(2, "fas fa-tools"), 
	MOVIMIENTOS(3, "fas fa-truck"),
	CONTABILIDAD(4, "fas fa-file-invoice-dollar"), 
	REPORTES(5, "fas fa-file-invoice"),
	GENERAL(6, "fas fa-info"), 
	SEGURIDAD(7, "fas fa-lock"), 
	CONFIGURACION(8, "fas fa-sliders-h"),
	AYUDA(9, "glyphicon glyphicon-question-sign"),
	
	COMPRA(97, "glyphicon glyphicon-shopping-cart"), 
	VENTA(98, "glyphicon glyphicon-usd"),
	EVENTOS(99, null);//no se muestra en menu, dejar ultimo siempre
	
	private final int orden;
	private final String icon;
	
	private Modulo(int orden, String icon) {
		this.orden = orden;
		this.icon = icon;
	}

	public int getOrden() {
		return orden;
	}
	
	public String getIconSpan() {
		if(StringUtils.isEmpty(icon)) {
			return "";
		}
		return "<span class='"+icon+"'></span> ";
	}
}
