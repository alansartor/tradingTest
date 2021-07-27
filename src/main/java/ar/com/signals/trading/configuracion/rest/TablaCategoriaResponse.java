package ar.com.signals.trading.configuracion.rest;

import java.util.List;

import ar.com.signals.trading.configuracion.domain.TablaCategoria;
import ar.com.signals.trading.util.rest.MyRestGenericResponse;

/**
 * Respuesta del EndPoint analisis
 * @author pepe@hotmail.com
 *
 */
public class TablaCategoriaResponse extends MyRestGenericResponse<List<TablaCategoria>>{
	private List<TablaCategoria> data;

	@Override
	public List<TablaCategoria> getData() {
		return data;
	}

	@Override
	public void setData(List<TablaCategoria> data) {
		this.data = data;
	}
}
