package ar.com.signals.trading.configuracion.service;

import ar.com.signals.trading.configuracion.domain.CategoriaSet;
import ar.com.signals.trading.configuracion.support.EntitySet;
import ar.com.signals.trading.configuracion.support.ICategoriaSet;

public interface CategoriaSetSrv {

	CategoriaSet obtener(String codigo);

	/**
	 * Crea la categoriaSet en base al Enum, esto es para no tener que andar creando a mano las categorias
	 * para las Propiedades, que estan todas basadas en Enums
	 * @param codigo
	 * @return
	 */
	CategoriaSet crearCategoriaSet(ICategoriaSet categoriaSet, EntitySet entitySet);

	void actualizar(CategoriaSet entidad);
}
