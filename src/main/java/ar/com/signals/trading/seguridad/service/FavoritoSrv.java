package ar.com.signals.trading.seguridad.service;

import java.util.List;
import ar.com.signals.trading.seguridad.domain.Favorito;
import ar.com.signals.trading.seguridad.domain.Respo;

public interface FavoritoSrv {

	List<Favorito> obtenerByResponsabilidad(Respo responsabilidad);

	void actualizar(Favorito entidad);
	
}
