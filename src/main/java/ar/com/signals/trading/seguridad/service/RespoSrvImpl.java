package ar.com.signals.trading.seguridad.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.configuracion.service.RespoCategoriaSrv;
import ar.com.signals.trading.configuracion.support.SeguridadCategoriaSetEnum;
import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.MenuOption;
import ar.com.signals.trading.seguridad.dto.PrivilegioDTO;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.seguridad.repository.RespoDao;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.TipoPrivilegio;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.util.support.SeguridadException;

@Service
@Transactional
public class RespoSrvImpl implements RespoSrv {
	@Autowired RespoDao dao;
	@Resource private RespoCategoriaSrv respoCategoriaSrv;
	
	@Override
	public List<Respo> getResponsabilidades() {
		return dao.getEntidades();
	}

	@Override
	public List<ResponsabilidadDTO> getResponsabilidadesDTO(List<TipoResponsabilidad> tipos) {
		return dao.getEntidadesDTO(tipos);
	}

	@Override
	public void validateAndPersist(ResponsabilidadDTO entidadDTO, BindingResult errors, Usuario usuario) {
		String CODIGO = entidadDTO.getCodigo().trim().toUpperCase();
		if(dao.existe(CODIGO , entidadDTO.getId())){//verifico que el codigo no este repetido. si es una respo global, entonces no se debe repetir entre los globales; y si es una custom entonces no se debe repetir con ninguna global ni con ninguna otra respo del mismo owner		
			errors.rejectValue("codigo", "", "Ya existe una Responsabilidad con ese Nombre");	
		}
		if(errors.hasErrors()){
			return;
		}
		//Persistir
		Respo entidad = null;
		//Toda entidad que se crea en el MSC y se sincroniza en el MSP debe tener en la fecha de ultima actulazicion un plus de segundos!
		Timestamp fechaActual = new Timestamp(new Date().getTime());	
		if(entidadDTO.getId() == null){
			entidad = new Respo();
			entidad.setId(entidadDTO.getId());
			entidad.setCreation_date(fechaActual);
			entidad.setActivo(true);
			entidad.setTipo(entidadDTO.getTipo());
			List<Privilegio> privilegios = new ArrayList<Privilegio>();
			for (PrivilegioDTO privilegioDTO : entidadDTO.getPrivilegios()) {
				if(privilegioDTO.isChecked()){
					privilegios.add(privilegioDTO.getPrivilegio());
				}
			}
			entidad.setPrivilegios(privilegios);
		}else{
			entidad = dao.obtener(entidadDTO.getId());

			List<Privilegio> nuevosAndViejos = new ArrayList<Privilegio>();
			for (PrivilegioDTO privilegioDTO : entidadDTO.getPrivilegios()) {
				if(privilegioDTO.isChecked()){
					nuevosAndViejos.add(privilegioDTO.getPrivilegio());
				}
			}
			
			//Recorro lo registros guardados para ver cual hay que borrar y cual agregar
			for (Iterator<Privilegio> iterator = entidad.getPrivilegios().iterator(); iterator.hasNext();) {
				Privilegio privilegio = iterator.next();
				if(!nuevosAndViejos.contains(privilegio)){
					iterator.remove();//Remuevo el camion de la lista
				}else{//la patente no se toco, por lo que no es necesario guardarla neuvamente
					nuevosAndViejos.remove(privilegio);
				}			
			}
			for (Privilegio privilegio : nuevosAndViejos) {
				entidad.getPrivilegios().add(privilegio);
			}
		}
		entidad.setCodigo(CODIGO);
		entidad.setDescripcion(entidadDTO.getDescripcion());
		entidad.setLast_update_date(fechaActual);
		if(entidadDTO.getId() == null) {
			dao.guardar(entidad);
			entidadDTO.setId(entidad.getId());
			entidadDTO.setCodigo(CODIGO);
		}
	}
	
	@Override
	public ResponsabilidadDTO obtenerDTO(Long id) {
		Respo entidad = dao.obtener(id);
		ResponsabilidadDTO entidadDTO = new ResponsabilidadDTO();
		entidadDTO.setId(entidad.getId());
		entidadDTO.setCodigo(entidad.getCodigo());
		entidadDTO.setTipo(entidad.getTipo());
		entidadDTO.setDescripcion(entidad.getDescripcion());
		//List<PrivilegioDTO> privilegios = new ArrayList<PrivilegioDTO>();
		for (Privilegio privilegio : Privilegio.values()) {//Cargo todas las acciones pero solo chekeadas las de la responsabilidad
			//privilegios.add(privilegio.name());
			entidadDTO.getPrivilegios().add(new PrivilegioDTO(privilegio, entidad.getPrivilegios().contains(privilegio)));
		}
/*		for (Privilegio privilegio : Privilegio.values()) {
			if(!privilegios.contains(privilegio.name())){
				privilegios.add(new PrivilegioDTO(privilegio, false));
			}	
		}*/
		//entidadDTO.setPrivilegios(privilegios);
		return entidadDTO;
	}

	@Override
	public Map<Modulo, MenuOption> obtenerPrivilegiosDTO(Long idRespoPadre, Long idRespoEdicion) {
		Respo respoPadre = null;
		if(idRespoPadre != null) {
			respoPadre = dao.obtener(idRespoPadre);
		}
		Respo respoEdicion = null;
		if(idRespoEdicion != null) {
			respoEdicion = dao.obtener(idRespoEdicion);
		}
		MenuOption menuAccion = null;
		List<Privilegio> privilegios = Arrays.asList(Privilegio.values());
		//respetar el orden en que se muestran en el menu!
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
		
		//*** ARMADO DE ARBOL ***
		Map<Modulo, MenuOption> privilegiosMap = new LinkedHashMap<Modulo, MenuOption>();//LinkedHashMap para respetar el orden de indercion
		for (Privilegio p : privilegios) {
			if(TipoPrivilegio.PrivilegioSoporte.equals(p.getTipoPrivilegio())){//Si es un menu de soporte, no se muestra en ABM responsabilidad
				continue;
			}
			if(respoPadre != null && !respoPadre.getPrivilegios().contains(p)) {//los usuarios maestros pueden crear responsabilidades custom, pero solo con los privilegios que poseen
				continue;
			}
			//*** MENU ***
			MenuOption menu = privilegiosMap.get(p.getModuloMenu());
			if(menu == null){//Creo el menu si no existia
				menu = new MenuOption(p.getModuloMenu());
				privilegiosMap.put(p.getModuloMenu(), menu);
			}
			//*** SUBMENU ***
			MenuOption submenu = null;
			if(TipoPrivilegio.PrivilegioEvento.equals(p.getTipoPrivilegio())) {//NEW los eventos se muestran todos juntos, aunque figuren como privilegios con clases distintas
				String claseMenu = "Evento";
				submenu = menu.getSubmenu().get(claseMenu);
				if(submenu == null){//Creo el menu si no existia
					submenu = new MenuOption(claseMenu, Accion.getIconForMenu(Accion.getIconClase(claseMenu)));
					menu.getSubmenu().put(claseMenu, submenu);
				}
			}else {
				submenu = menu.getSubmenu().get(p.getClaseMenu());
				if(submenu == null){//Creo el menu si no existia
					submenu = new MenuOption(p.getClaseMenu(), Accion.getIconForMenu(Accion.getIconClase(p.getClaseMenu())));
					menu.getSubmenu().put(p.getClaseMenu(), submenu);
				}
			}
			
			//*** ACCION ***
			menuAccion = new MenuOption(p);
			submenu.getSubmenu().put(p.name(), menuAccion);
			if(respoEdicion != null) {
				List<Categoria> nivelesHabilitados = respoCategoriaSrv.getEntidades(respoEdicion, SeguridadCategoriaSetEnum.SEGURIDAD_NIVEL_EDICION.name(), p);
				for (Categoria categoria : nivelesHabilitados) {
					menuAccion.getNiveles().add(categoria.getId());
				}
			}
		}
		return privilegiosMap;
	}

	@Override
	public void actualizar(Respo entidad) {
		dao.guardar(entidad);
	}

	@Override
	public Respo obtenerWithJoins(Long id) {
		return dao.obtenerWithJoins(id);
	}

	@Override
	public void persistir(Respo entidad) {
		dao.guardar(entidad);
	}	
}