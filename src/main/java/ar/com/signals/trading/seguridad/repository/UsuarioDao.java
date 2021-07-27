package ar.com.signals.trading.seguridad.repository;

import java.util.List;

import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.UsuarioDTO;
import ar.com.signals.trading.util.repository.GenericDao;
import ar.com.signals.trading.util.rest.AutocompleteDTO;

public interface UsuarioDao extends GenericDao<Usuario> {

	List<UsuarioDTO> getEntidades(String username, String descripcion);

	boolean existe(String username);

	Usuario obtenerPorUsername(String username);

	List<? extends AutocompleteDTO> getAutocompleteDTO(String partial);

}