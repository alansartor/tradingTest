package ar.com.signals.trading.configuracion.service;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.configuracion.support.ClienteCategoriaEnum;
import ar.com.signals.trading.configuracion.support.ProductoCategoriaEnum;
import ar.com.signals.trading.configuracion.support.ProductoGenericoCategoriaEnum;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.configuracion.support.SucursalCategoriaEnum;

public interface CategoriaSrv {

	Categoria obtener(String codigo);

	/**
	 * Crea la categoria en base al Enum, esto es para no tener que andar creando a mano las categorias
	 * para las Propiedades, que estan todas basadas en Enums
	 * @param codigo
	 * @return
	 */
	Categoria crearCategoriaForPropiedad(PropiedadCategoriaEnum codigo);

	void actualizar(Categoria entidad);

	/**
	 * Crea la categoria en base al Enum, esto es para no tener que andar creando a mano las categorias
	 * para las Propiedades, que estan todas basadas en Enums
	 * @param productoEquivalenteOracleEbs
	 * @return
	 */
	Categoria crearCategoriaForProducto(ProductoCategoriaEnum codigo);

	/**
	 * Crea la categoria en base al Enum, esto es para no tener que andar creando a mano las categorias
	 * para las Propiedades, que estan todas basadas en Enums
	 * @param codigo
	 * @return
	 */
	Categoria crearCategoriaForSucursal(SucursalCategoriaEnum codigo);

	/**
	 * Crea la categoria en base al Enum, esto es para no tener que andar creando a mano las categorias
	 * para las Propiedades, que estan todas basadas en Enums
	 * @param clienteEquivalenteOracleEbs
	 * @return
	 */
	Categoria crearCategoriaForCliente(ClienteCategoriaEnum codigo);

	Categoria crearCategoriaForProductoGenerico(ProductoGenericoCategoriaEnum codigo);
}
