package ar.com.signals.trading.seguridad.repository;

import java.sql.Timestamp;
import java.util.List;

import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.util.repository.GenericDao;

public interface RespoDao extends GenericDao<Respo> {
	List<Respo> getEntidades();

	List<ResponsabilidadDTO> getEntidadesDTO(List<TipoResponsabilidad> tipos);

	List<Respo> getEntidadesWithJoins(Timestamp ultimaSync);

	Respo obtenerWithJoins(Long id);

	boolean existe(String CODIGO, Long id);
}