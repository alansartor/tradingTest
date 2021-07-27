package ar.com.signals.trading.configuracion.rest;

import java.util.List;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.util.rest.MyRestGenericResponse;

/**
 * Respuesta del EndPoint analisis
 * @author pepe@hotmail.com
 *
 */
public class CategoriaResponse extends MyRestGenericResponse<List<Categoria>>{
	private List<Categoria> data;

	@Override
	public List<Categoria> getData() {
		return data;
	}

	@Override
	public void setData(List<Categoria> data) {
		this.data = data;
	}
}
