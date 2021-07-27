package ar.com.signals.trading.seguridad.service;

import java.util.List;
import org.springframework.validation.BindingResult;

import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.TokenTemporalDTO;
import ar.com.signals.trading.seguridad.dto.UsuarioDTO;
import ar.com.signals.trading.util.rest.AutocompleteDTO;
import ar.com.signals.trading.util.web.Mensaje;

public interface UsuarioSrv {
	//Set<RespoUsuario> getResponsabilidades(Usuario usuario);

	/**
	 * Setea la responsabilidad seleccionada en el usuario y arma el menu del usuario
	 * @param usuario
	 * @param responsabilidadId
	 * @return
	 */
	void setResponsabilidadAndMakeMenu(Usuario usuario, Long responsabilidadId);
	
	/**
	 * Realiza validaciones de negocio y persiste ne caso que este todo correcto
	 * @param entidadDTO
	 * @param errors
	 * @return 
	 */
	List<Mensaje> validateAndPersist(UsuarioDTO entidadDTO, BindingResult errors, Usuario usuario);

	/**
	 * Busca los usuarios
	 * Se puede filtrar por nombre o apellido, no obligatorios
	 * @param nombre 
	 * @param apellido 
	 * @param owner 
	 * @return
	 */
	List<UsuarioDTO> getEntidades(String apellido, String nombre);

	UsuarioDTO obtenerDTO(Long id);

	void actualizar(Usuario entidad);

	Usuario obtener(Long id);
	
	List<? extends AutocompleteDTO> getAutocompleteDTO(String search);

	/**
	 * Se usa para crear un usuario de testing
	 * @param usuario
	 * @param string
	 */
	void persistir(Usuario entidad, String password);

	/**
	 * Se llama cuando el usuario desea cambiar la password
	 * @param entidadDTO
	 * @param errors
	 */
	void validateAndPersistPass(UsuarioDTO entidadDTO, BindingResult errors);
	
	boolean existe(Long id);
	
	void cleanRespoSujetoMenu(Usuario usuario);
	
	UsuarioDTO resetPassword(Long id, Usuario usuario);
	
	String obtenerTelegramId(Long id);
	
	void vincularConTelegram(Long key, Long id);
	
	Usuario getByTelegramId(String telegramId);
	
	void resetTelegram(Usuario usuario);
	
	Usuario obtenerPorUsername(String username);
}
