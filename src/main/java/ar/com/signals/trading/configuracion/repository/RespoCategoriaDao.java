package ar.com.signals.trading.configuracion.repository;

import java.sql.Timestamp;
import java.util.List;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.configuracion.domain.RespoCategoria;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.util.repository.GenericDao;

public interface RespoCategoriaDao extends GenericDao<RespoCategoria> {

	List<RespoCategoria> getRespoCategoriasSincronizar(Timestamp ultimaSync);

	List<Categoria> getEntidades(Respo respo, String categoriaSet_id, Privilegio privilegio);
}