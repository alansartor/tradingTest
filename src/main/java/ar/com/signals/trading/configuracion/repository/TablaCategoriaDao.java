package ar.com.signals.trading.configuracion.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import ar.com.signals.trading.configuracion.domain.TablaCategoria;
import ar.com.signals.trading.util.repository.GenericDao;

public interface TablaCategoriaDao extends GenericDao<TablaCategoria> {

	BigDecimal obtener(String categoria_id, BigDecimal valor);

	List<TablaCategoria> getTablasCategoriaSincronizar(Timestamp ultimaSync);

}