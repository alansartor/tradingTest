package ar.com.signals.trading.seguridad.rest;

import java.util.List;

import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.util.rest.MyRestGenericResponse;

/**
 * Respuesta del EndPoint analisis
 * @author pepe@hotmail.com
 *
 */
public class ResponsabilidadResponse extends MyRestGenericResponse<List<Respo>>{
	private List<Respo> data;

	@Override
	public List<Respo> getData() {
		return data;
	}

	@Override
	public void setData(List<Respo> data) {
		this.data = data;
	}
}
