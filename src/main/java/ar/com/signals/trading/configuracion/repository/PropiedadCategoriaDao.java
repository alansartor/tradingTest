package ar.com.signals.trading.configuracion.repository;

import java.sql.Timestamp;
import java.util.List;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.configuracion.domain.PropiedadCategoria;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaDTO2;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaTipoComprobanteDTO;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.util.repository.GenericDao;

public interface PropiedadCategoriaDao extends GenericDao<PropiedadCategoria> {

	List<PropiedadCategoriaDTO2> getPropiedadesDTO(List<Long> unidadOperativaIds, boolean superUser);

	PropiedadCategoria obtener(String codigoCategoria, Long planta_id);

	boolean existe(String codigoCategoria, Long empresaId, Long plantaId);

	List<PropiedadCategoria> getPropiedadesCategoriaSincronizar(Long empresaId, Long plantaId, Timestamp ultimaSync);

	boolean tieneCategoriaActiva(String codigoCategoria, Long empresaId, Long plantaId);

	boolean existe(String codigoCategoria, String valor, Long id);

	boolean existe(String name, Long sujeto_id, Long sucursal_id, Long unidadProductiva_id, Long id);

}