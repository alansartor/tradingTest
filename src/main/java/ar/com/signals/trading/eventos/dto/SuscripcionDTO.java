package ar.com.signals.trading.eventos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import ar.com.signals.trading.eventos.support.MetodoNotificacion;
import ar.com.signals.trading.seguridad.support.Privilegio;

public class SuscripcionDTO {
	private Long id;
	private Long usuario_id;
	
	//cuando es una nueva suscripcion, se puede dar de alta en todas las sucursales
	private List<Long> sucursales = new ArrayList<Long>();
	private List<Privilegio> eventos = new ArrayList<Privilegio>();
	
	//para listar
	private Long sucursal_id;
	private String sucursal_codigo;
	private Privilegio evento;
	private String eventoDescripcion;//para mostrar el texto del evento
	
	private MetodoNotificacion metodoNotificacionPrincipal;
	
	public String getMetodoIconSpan() {
		return metodoNotificacionPrincipal!=null?"<span class='"+metodoNotificacionPrincipal.getIcon()+"'></span>":"";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}

	public Long getSucursal_id() {
		return sucursal_id;
	}

	public void setSucursal_id(Long sucursal_id) {
		this.sucursal_id = sucursal_id;
	}

	public List<Long> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<Long> sucursales) {
		this.sucursales = sucursales;
	}

	public Privilegio getEvento() {
		return evento;
	}

	public void setEvento(Privilegio evento) {
		this.evento = evento;
	}

	public String getSucursal_codigo() {
		return sucursal_codigo;
	}

	public void setSucursal_codigo(String sucursal_codigo) {
		this.sucursal_codigo = sucursal_codigo;
	}

	public List<Privilegio> getEventos() {
		return eventos;
	}

	public void setEventos(List<Privilegio> eventos) {
		this.eventos = eventos;
	}
	
	public String getEventoDescripcion() {
		return eventoDescripcion;
	}
	public void setEventoDescripcion(String eventoDescripcion) {
		this.eventoDescripcion = eventoDescripcion;
	}

	public MetodoNotificacion getMetodoNotificacionPrincipal() {
		return metodoNotificacionPrincipal;
	}

	public void setMetodoNotificacionPrincipal(MetodoNotificacion metodoNotificacionPrincipal) {
		this.metodoNotificacionPrincipal = metodoNotificacionPrincipal;
	}

}
