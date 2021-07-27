package ar.com.signals.trading.ayuda.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import ar.com.signals.trading.ayuda.suport.AyudaEnum;

public class MenuOption {
	private String name;
	/**
	 * Si no tiene opciones entonces es una accion o un field
	 */
	private Map<String, MenuOption> submenu = new LinkedHashMap<String, MenuOption>();
	private AyudaEnum ayuda;
	
	public MenuOption(String name) {
		this.name = name;
	}
	
	public MenuOption(String name, AyudaEnum ayuda) {
		this.name = name;
		this.setAyuda(ayuda);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, MenuOption> getSubmenu() {
		return submenu;
	}
	public void setSubmenu(Map<String, MenuOption> submenu) {
		this.submenu = submenu;
	}
	public AyudaEnum getAyuda() {
		return ayuda;
	}
	public void setAyuda(AyudaEnum ayuda) {
		this.ayuda = ayuda;
	}
}
