package ar.com.signals.trading.configuracion.repository;

import java.sql.Timestamp;
import java.util.List;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.util.repository.GenericDao;

public interface CategoriaDao extends GenericDao<Categoria> {

	List<Categoria> getCategoriasSincronizacion(Timestamp ultimaSync);
}