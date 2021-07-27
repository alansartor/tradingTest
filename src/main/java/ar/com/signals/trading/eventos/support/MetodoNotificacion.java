package ar.com.signals.trading.eventos.support;

public enum MetodoNotificacion {//No modificar el orden!
	telegram("fab fa-telegram-plane"), 
	pantalla("fas fa-tv"), 
	email("glyphicon glyphicon-envelope");
	
	private String icon;
	
	private MetodoNotificacion(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
