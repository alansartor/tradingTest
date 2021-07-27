package ar.com.signals.trading.configuracion.rest;

import java.util.List;

import ar.com.signals.trading.configuracion.domain.RespoCategoria;
import ar.com.signals.trading.util.rest.MyRestGenericResponse;

/**
 * Respuesta del EndPoint analisis
 * @author pepe@hotmail.com
 *
 */
public class RespoCategoriaResponse extends MyRestGenericResponse<List<RespoCategoria>>{
	private List<RespoCategoria> data;

	@Override
	public List<RespoCategoria> getData() {
		return data;
	}

	@Override
	public void setData(List<RespoCategoria> data) {
		this.data = data;
	}
}
