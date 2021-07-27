package ar.com.signals.trading.seguridad.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Favorito;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.MenuOption;
import ar.com.signals.trading.seguridad.dto.UsuarioDTO;
import ar.com.signals.trading.seguridad.repository.UsuarioDao;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.TipoPrivilegio;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.util.rest.AutocompleteDTO;
import ar.com.signals.trading.util.support.CuilCuitValidator;
import ar.com.signals.trading.util.web.FormateadorUtil;
import ar.com.signals.trading.util.web.Mensaje;
import ar.com.signals.trading.util.web.ServidorUtil;

@Service("myUserDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService, UsuarioSrv {
	@Autowired private UsuarioDao dao;
	@Resource private RespoSrv respoSrv;
	@Resource private RespoUsuarioSrv respoUsuarioSrv;
	@Resource private ServidorUtil servidorUtil;
	@Resource private FavoritoSrv favoritoSrv;
    @Autowired  private SessionRegistry sessionRegistry;
	
	@Resource private BCryptPasswordEncoder bcryptEncoder;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		Usuario temp = dao.obtenerPorUsername(username.toLowerCase());//NEW 27/03/2019 habilito login por alias
		if(temp == null)
			throw new UsernameNotFoundException("No se encontro el usuario");
		return temp;
	}

/*	public Set<RespoUsuario> getResponsabilidades(Usuario usuario) {
		dao.refresh(usuario);
		Hibernate.initialize(usuario.getRespos());
		//usuario.getResponsabilidades().size();//
		return usuario.getRespos();
	}*/

	@Override
	public void setResponsabilidadAndMakeMenu(Usuario usuario, Long responsabilidadId) {
		Respo respo = respoSrv.obtenerWithJoins(responsabilidadId);
		//RespoUsuario responsabilidadUsuario = responsabilidadUsuarioDao.obtener(usuario, responsabilidadId);
		usuario.setResponsabilidad(respo);//Lo utiliza spring para obtener los privilegios, respoSrv.obtenerWithJoins forza el join con privilegios
		
		//*** IMPORTANTE: Esto reload user GrantedAuthority ***
		Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, usuario.getPassword(), usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        boolean onlyConfigAndSecurityOptions = false;//para que en el menu solo se muetren opciones generares de configuracion y seguridad. Nada de opciones para realizar operaciones en alguna sucursal
        //Logo para responsabilidades especiales
        if(TipoResponsabilidad.SuperUsuario.equals(respo.getTipo())) {
        	usuario.setMenuImagen(respo.getTipo().name());
        	onlyConfigAndSecurityOptions = true;
        }else if(TipoResponsabilidad.UsuarioMaestro.equals(respo.getTipo())) {
        	usuario.setMenuImagen(respo.getTipo().name());
        	onlyConfigAndSecurityOptions = true;
        }
        
		//*** ARMADO DE MENU ***
		Map<Modulo, MenuOption> menues = new LinkedHashMap<Modulo, MenuOption>();
		List<Privilegio> privilegios = usuario.getResponsabilidad().getPrivilegios();//El menu se arma de acuerdo al Servidor donde se logueo el usuario
		Collections.sort(privilegios, new Comparator<Privilegio>() {
			public int compare(Privilegio p1, Privilegio p2) {
				if(p1.getModuloMenu().getOrden() == p2.getModuloMenu().getOrden()){
					//return p1.name().compareTo(p2.name());
					return p1.getOrdinal() -  p2.getOrdinal();//el orden en el enum dan el orden en que se muestran las clases y las acciones dentro de cada modulo
				}else{
					return p1.getModuloMenu().getOrden() - p2.getModuloMenu().getOrden();
				}
			}
		});
		for (Privilegio p : privilegios) {
			if(onlyConfigAndSecurityOptions && !TipoPrivilegio.PrivilegioMenuConfigAndSecurity.equals(p.getTipoPrivilegio())) {
				continue;//se logueo con respo especial, entonces solo muestro los menu de configuracion
			}else if(!onlyConfigAndSecurityOptions && !(TipoPrivilegio.PrivilegioMenuConfigAndSecurity.equals(p.getTipoPrivilegio()) || TipoPrivilegio.PrivilegioMenu.equals(p.getTipoPrivilegio()))) {
				continue;//se logueo con usuario comun, entonces muestro todas las opciones de menu (comunes y de seguridad)
			}
			//*** MENU ***
			MenuOption menu = menues.get(p.getModuloMenu());
			if(menu == null){//Creo el menu si no existia
				menu = new MenuOption(p.getModuloMenu());
				menues.put(p.getModuloMenu(), menu);
			}
			//*** SUBMENU ***
			MenuOption submenu = menu.getSubmenu().get(p.getClaseMenu());
			if(submenu == null){//Creo el menu si no existia
				submenu = new MenuOption(p.getClaseMenu(), Accion.getIconForMenu(Accion.getIconClase(p.getClaseMenu())));
				menu.getSubmenu().put(p.getClaseMenu(), submenu);
			}
			//*** ACCION ***
			submenu.getSubmenu().put(p.name(), new MenuOption(p));
		}
		usuario.setMenu(menues);
		
		//*** ARMADO DE FAVORITOS ***
		List<Favorito> favoritos = favoritoSrv.obtenerByResponsabilidad(usuario.getResponsabilidad());
		Collections.sort(favoritos, new Comparator<Favorito>() {
			public int compare(Favorito f1, Favorito f2) {
				if(f1.getPrivilegio().getModuloMenu().name().compareTo(f2.getPrivilegio().getModuloMenu().name()) == 0){
					return f1.getPrivilegio().name().compareTo(f2.getPrivilegio().name());
				}else{
					return f1.getPrivilegio().getModuloMenu().name().compareTo(f2.getPrivilegio().getModuloMenu().name());
				}
			}
		});
		Map<Modulo, MenuOption> menuFavoritos = new LinkedHashMap<Modulo, MenuOption>();
		for (Favorito f : favoritos) {
			if(!TipoPrivilegio.PrivilegioMenu.equals(f.getPrivilegio().getTipoPrivilegio()) 
					|| !privilegios.contains(f.getPrivilegio())){//Si no es un privilegio de menu no hago nada, y si llega a ser un favorito para 
				continue;
			}
			//*** MENU ***
			MenuOption menu = menuFavoritos.get(f.getPrivilegio().getModuloMenu());
			if(menu == null){//Creo el menu si no existia
				menu = new MenuOption(f.getPrivilegio().getModuloMenu().name());
				menuFavoritos.put(f.getPrivilegio().getModuloMenu(), menu);
			}
			//*** SUBMENU ***
			//MenuOption submenu = menu.getSubmenu().get(f.getPrivilegio().getClase());
			//if(submenu == null){//Creo el menu si no existia
				//submenu = new MenuOption(f.getPrivilegio().getClase());
				//menu.getSubmenu().put(f.getPrivilegio().getClase(), submenu);
			//}
			//sin submenu, directamente el modulo y la lista de favoritos
			//*** ACCION ***
			MenuOption accion = new MenuOption(f.getPrivilegio());
			accion.setArgumentos(f.getArgumentos()!=null?f.getArgumentos():"");
			accion.setNombreExtendido(f.getNombreExtendido()!=null?f.getNombreExtendido():"");
			menu.getSubmenu().put(f.getId().toString(), accion);//si mando como key el privilegio, y hay mas de un favorito con el mismo id, se pisan, entonces pongo como id el id de favorito
		}
		usuario.setMenuFavoritos(menuFavoritos);
	}
	
	@Override
	public void cleanRespoSujetoMenu(Usuario usuario) {
		if(usuario != null) {
			usuario.setResponsabilidad(null);
			usuario.setMenuImagen(null);
			usuario.setMenu(null);
			usuario.setMenuFavoritos(null);
		}
	}
	
	public static void main(String [ ] args){
		System.out.println(System.currentTimeMillis());
		//Para generar claves encriptadas
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode("sechevarria");
		System.out.println(hashedPassword);
	}

	@Override
	public List<Mensaje> validateAndPersist(UsuarioDTO entidadDTO, BindingResult errors, Usuario usuario) {
		List<Mensaje> mensajes = new ArrayList<>();
		if(entidadDTO.getId() == null) {
			if(entidadDTO.getUsername().trim().length() < 4) {
				errors.rejectValue("username", "", "Ingrese al menos 4 caracteres");
			}else {
				//verifico que no exista otro usuario con ese cuit (si bien puede existir el sujeto, no deberia haber otro usuario)
				if(dao.existe(entidadDTO.getUsername().toLowerCase().trim())) {
					errors.rejectValue("username", "", "Ya existe usuario con ese Username");
				}
			}
		}
		if(entidadDTO.getDescripcion().trim().length() < 3) {
			errors.rejectValue("descripcion", "", "Debe ingresar algo valido, al menos 3 caracteres");
		}
		if(errors.hasErrors()){
			return mensajes;
		}
		//Persistir
		Usuario entidad = null;
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		if(entidadDTO.getId() == null){
			entidad = new Usuario();
			entidad.setUsername(entidadDTO.getUsername().trim().toLowerCase());
			entidad.setPassword(bcryptEncoder.encode(entidadDTO.getPassword()));
			entidad.setCreation_date(fechaActual);
		}else{
			entidad = dao.obtener(entidadDTO.getId());
		}
		entidad.setDescripcion(entidadDTO.getDescripcion().trim().toUpperCase());
		entidad.setUser_enabled(entidadDTO.isUser_enabled());
		entidad.setUser_email(entidadDTO.getUser_email());
		entidad.setLast_update_date(fechaActual);
		if(entidadDTO.getId() == null) {
			dao.guardar(entidad);
			entidadDTO.setId(entidad.getId());
		}
		return mensajes;
	}

	@Override
	public List<UsuarioDTO> getEntidades(String username, String nombre) {
		return dao.getEntidades(username, nombre);
	}

	@Override
	public UsuarioDTO obtenerDTO(Long id) {
		Usuario user = dao.obtener(id);
		UsuarioDTO entidadDTO = new UsuarioDTO();
		entidadDTO.setId(user.getId());
		entidadDTO.setUsername(user.getUsername());
		entidadDTO.setDescripcion(user.getDescripcion());
		entidadDTO.setUser_email(user.getUser_email());
		entidadDTO.setUser_enabled(user.isEnabled());
		return entidadDTO;
	}
	
	@Override
	public UsuarioDTO resetPassword(Long id, Usuario usuario) {
		Usuario user = dao.obtener(id);
		String encodedPass =  bcryptEncoder.encode("1234");
		user.setPassword(encodedPass);
		//Toda entidad que se crea en el MSC y se sincroniza en el MSP debe tener en la fecha de ultima actualizacion un plus de segundos!
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		user.setLast_update_date(fechaActual);
		
		UsuarioDTO entidadDTO = new UsuarioDTO();
		entidadDTO.setId(user.getId());
		entidadDTO.setUsername(user.getUsername());
		entidadDTO.setDescripcion(user.getDescripcion());
		entidadDTO.setUser_email(user.getUser_email());
		entidadDTO.setUser_enabled(user.isEnabled());
		return entidadDTO;
	}

	@Override
	public void actualizar(Usuario entidad) {
		dao.guardar(entidad);
	}

	@Override
	public Usuario obtener(Long id) {
		return dao.obtener(id);
	}

	@Override
	public void persistir(Usuario entidad, String password) {
		entidad.setPassword(bcryptEncoder.encode(password));
		dao.guardar(entidad);
	}
	
	@Override
	public void validateAndPersistPass(UsuarioDTO entidadDTO, BindingResult errors){
		Usuario entidad = dao.obtener(entidadDTO.getId());
		if(StringUtils.isNotEmpty(entidadDTO.getPassword())) {
			//verifico que sea igual a la base
			if(!bcryptEncoder.matches(entidadDTO.getPassword(), entidad.getPassword())) {
				errors.rejectValue("password", "", "La password actual es incorrecta"); 
			}else {
				if(StringUtils.isEmpty(entidadDTO.getPasswordNew())) {
					errors.rejectValue("passwordNew", "", "Si desea modificar la contraseña, debe completar este campo"); 
				}else if(entidadDTO.getPasswordNew().length() < 4){
					errors.rejectValue("passwordNew", "", "Debe ingresar una contraseña mas larga"); 
				}
				if(StringUtils.isEmpty(entidadDTO.getPasswordNew2())) {
					errors.rejectValue("passwordNew2", "", "Si desea modificar la contraseña, debe completar este campo"); 
				}
				if(StringUtils.isNotEmpty(entidadDTO.getPasswordNew()) && StringUtils.isNotEmpty(entidadDTO.getPasswordNew2()) && !entidadDTO.getPasswordNew().equals(entidadDTO.getPasswordNew2())) {
					errors.rejectValue("passwordNew2", "", "Debe repetir exactamente la contraseña nueva"); 
				}
			}
			if(errors != null && errors.hasErrors()){
				return;
			}
			String encodePassNew = bcryptEncoder.encode(entidadDTO.getPasswordNew());
			entidad.setPassword(encodePassNew);
		}else if(StringUtils.isNotEmpty(entidadDTO.getPasswordNew())) {
			errors.rejectValue("password", "", "Si desea modificar la contraseña, primero debe ingresar la actual");
		}
		if(errors != null && errors.hasErrors()){
			return;
		}

		if(StringUtils.isNotBlank(entidadDTO.getUser_email())){
			entidad.setUser_email(entidadDTO.getUser_email().trim());
		}
		//entidad.setAlias(alias);
		entidad.setLast_update_date(new Timestamp(new Date().getTime()));
	}

	@Override
	public boolean existe(Long id) {
		return dao.existe(id, "id");
	}

	@Override
	public List<? extends AutocompleteDTO> getAutocompleteDTO(String search) {
		return dao.getAutocompleteDTO(search.trim());
	}

	@Override
	public Usuario obtenerPorUsername(String alias) {
		return dao.obtenerPorUsername(alias);//el alias debe venir en minuscula!
	}

	@Override
	public String obtenerTelegramId(Long id) {
		return dao.getPropiedad(id, "id", "telegram_id");
	}

	@Override
	public void vincularConTelegram(Long id, Long chat_id) {
		Usuario usuario = dao.obtener(id);
		usuario.setTelegram_id(chat_id.toString());
		//si el usuario esta logueado al sistema, actualizar este dato!
		List<Object> principals = sessionRegistry.getAllPrincipals();
		for (Object principal: principals) {
		    if (principal instanceof Usuario) {
		    	if(usuario.getId().equals(((Usuario) principal).getId())) {
		    		((Usuario) principal).setTelegram_id(chat_id.toString());//Para lo unico que sirve esto es por si el usuario que recien activo la cuenta de telegram vuelve a ir a la pagina de "Configuracion de Usuarios", la pagina le informe que ya esta logueado
		    		break;
		    	}
		    }
		}
	}

	@Override
	public Usuario getByTelegramId(String telegramId) {
		return dao.obtenerPorFiltroUnico(telegramId, "telegram_id");//puse un unike constraint
	}

	@Override
	public void resetTelegram(Usuario usuario) {
		dao.refresh(usuario);
		usuario.setTelegram_id(null);
	}
}