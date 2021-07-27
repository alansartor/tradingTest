package ar.com.signals.trading.seguridad.rest;

import java.util.List;

import ar.com.signals.trading.seguridad.domain.RespoUsuario;
import ar.com.signals.trading.util.rest.MyRestGenericResponse;

/**
 * Respuesta del EndPoint analisis
 * @author pepe@hotmail.com
 *
 */
public class RespoUsuarioResponse extends MyRestGenericResponse<List<RespoUsuario>>{
	private List<RespoUsuario> data;

	@Override
	public List<RespoUsuario> getData() {
		return data;
	}

	@Override
	public void setData(List<RespoUsuario> data) {
		this.data = data;
	}
}
