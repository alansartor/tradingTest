package ar.com.signals.trading.util.web;

import java.math.BigDecimal;

//@Component NO declararlo como Component, se declara directamente como bean en el applicationContext ya que se inicializa con valores
//Si lo llegamos a declarar como component, entonces pisa al declarado en applicationContext y queda con las propiedades en null
public class ServidorUtil {
	public enum Entorno {TEST, PRODUCCION}

	private Entorno entorno;
	/**
	 * Es solo para Servidor MSP, es el id de la planta donde esta corriendo la aplicacion
	 * (en caso que el servidor este atendiendo a mas de una planta, se toma una como la principal)
	 */
	private BigDecimal organization_id;
	
	/**
	 * Devuelve la ip y el puerto para armar el link al sistema.
	 * @return
	 */
	public String getIpPort() {
		switch (entorno) {
		case TEST:
			return "localhost:8080";
		case PRODUCCION:
			return "192.168.00.25:8080";
		}
		return null;
	}
	
	/**
	 * Devuelve la ip y el puerto del sistema de camiones
	 * @return
	 */
	public String getIpPortSGC() {
		switch (entorno) {
		case TEST:
			return "192.168.00.146:8080";
		case PRODUCCION:
			return "192.168.00.55:8080";
		}
		return null;
	}
	
	/**
	 * @param entorno the entorno to set
	 */
	public void setEntorno(Entorno entorno) {
		this.entorno = entorno;
	}
	/**
	 * @return the entorno
	 */
	public Entorno getEntorno() {
		return entorno;
	}

	/**
	 * Es el id de la planta donde corre el MSP, solo usar en servidor MSP
	 * @return
	 */
	public BigDecimal getOrganization_id() {
		return organization_id;
	}

	public void setOrganization_id(BigDecimal organization_id) {
		this.organization_id = organization_id;
	}
}
