package ar.com.signals.trading.configuracion.rest;

import java.util.List;

import ar.com.signals.trading.configuracion.domain.PropiedadCategoria;
import ar.com.signals.trading.util.rest.MyRestGenericResponse;

/**
 * Respuesta del EndPoint analisis
 * @author pepe@hotmail.com
 *
 */
public class PropiedadCategoriaResponse extends MyRestGenericResponse<List<PropiedadCategoria>>{
	private List<PropiedadCategoria> data;

	@Override
	public List<PropiedadCategoria> getData() {
		return data;
	}

	@Override
	public void setData(List<PropiedadCategoria> data) {
		this.data = data;
	}
}
