package ar.com.signals.trading.seguridad.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.support.Privilegio;

public class MenuOption {
	private String name;
	private String iconSpan;
	/**
	 * Si no tiene opciones entonces es una accion
	 */
	private Map<String, MenuOption> submenu = new LinkedHashMap<String, MenuOption>();//LinkedHashMap para respetar el orden de insercion
	private Privilegio privilegio;
	
	//Propiedades adicionales para favoritos
	private String argumentos;
	private String nombreExtendido;
	
	private List<String> niveles = new ArrayList<>();//se usa en edicion de respo, para mostrar los niveles que tiene asociado un privilegio de una respo en particular
	
	public MenuOption(Modulo modulo) {
		this.name = modulo.name();
		this.iconSpan = modulo.getIconSpan();
	}
	
	public MenuOption(String name) {
		this.name = name;
	}
	
	public MenuOption(String name, String iconSpan) {
		this.name = name;
		this.iconSpan = iconSpan;
	}
	
	public MenuOption(Privilegio privilegio) {
		this.name = privilegio.name();
		this.privilegio = privilegio;
		this.iconSpan = privilegio.getIconSpan();
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
	public Privilegio getPrivilegio() {
		return privilegio;
	}
	public void setPrivilegio(Privilegio privilegio) {
		this.privilegio = privilegio;
	}
	public String getArgumentos() {
		return argumentos;
	}
	public void setArgumentos(String argumentos) {
		this.argumentos = argumentos;
	}
	public String getNombreExtendido() {
		return nombreExtendido;
	}
	public void setNombreExtendido(String nombreExtendido) {
		this.nombreExtendido = nombreExtendido;
	}

	public List<String> getNiveles() {
		return niveles;
	}

	public void setNiveles(List<String> niveles) {
		this.niveles = niveles;
	}

	public String getIconSpan() {
		return iconSpan;
	}

	public void setIconSpan(String iconSpan) {
		this.iconSpan = iconSpan;
	}
}
