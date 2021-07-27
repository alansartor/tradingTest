package ar.com.signals.trading.seguridad.repository;

import java.sql.Timestamp;
import java.util.List;

import ar.com.signals.trading.seguridad.domain.Favorito;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.util.repository.GenericDao;

public interface FavoritoDao extends GenericDao<Favorito> {

	List<Favorito> obtenerByResponsabilidad(Respo responsabilidad);

	List<Favorito> getFavoritosSincronizar(Timestamp ultimaSync);

}