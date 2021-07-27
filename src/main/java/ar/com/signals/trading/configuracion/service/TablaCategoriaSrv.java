package ar.com.signals.trading.configuracion.service;

import java.math.BigDecimal;

import ar.com.signals.trading.configuracion.domain.TablaCategoria;

public interface TablaCategoriaSrv {

	BigDecimal obtener(String categoria_id, BigDecimal valor);

	void actualizar(TablaCategoria entidad);
	
}
