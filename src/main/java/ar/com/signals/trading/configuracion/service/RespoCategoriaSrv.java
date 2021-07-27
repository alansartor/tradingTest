package ar.com.signals.trading.configuracion.service;

import java.util.List;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.configuracion.domain.RespoCategoria;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.support.Privilegio;

public interface RespoCategoriaSrv {

	void actualizar(RespoCategoria entidad);

	List<Categoria> getEntidades(Respo respo, String categoriaSet_id, Privilegio privilegio);

}
