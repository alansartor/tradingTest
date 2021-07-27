package ar.com.signals.trading.telegram.support;

public enum TelegramCommandEnum {
	margenBruto("margenBruto", "El sistema devolvera el ultimo Margen Bruto del establecimiento seleccionado");
	
	private final String command;
	private final String description;
	
	private TelegramCommandEnum(String command, String description) {
		this.command = command;
		this.description = description;
	}
	public String getCommand() {
		return command;
	}
	public String getDescription() {
		return description;
	}
	public String toJsonString() {
		return "{\"command\":\""+command+"\",\"description\":\""+description+"\"}";
	}
}
