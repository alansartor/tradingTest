package ar.com.signals.trading.seguridad.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.domain.RespoUsuario;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.seguridad.repository.RespoUsuarioDao;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.dto.RespoUsuarioDTO;

@Service
@Transactional
public class RespoUsuarioSrvImpl implements RespoUsuarioSrv {
	@Autowired RespoUsuarioDao dao;
	@Resource UsuarioSrv usuarioSrv;
	@Resource private RespoSrv respoSrv;

	@Override
	public List<ResponsabilidadDTO> getResponsabilidadesDTO(Usuario usuario) {
		return dao.getResponsabilidadesDTO(usuario);
	}

	@Override
	public void persistir(RespoUsuario entidad) {
		dao.guardar(entidad);
	}
	
	@Override
	public void validateAndPersist(RespoUsuarioDTO entidadDTO, BindingResult errors, boolean sinSucursal) {
		//primero verifico que exista el usuario
		Usuario usuario = null;
		if(entidadDTO.getUsuario_id() == null) {
			String username = entidadDTO.getUsuario_username().trim();
			usuario = usuarioSrv.obtenerPorUsername(username.toLowerCase());
		}else {
			usuario = usuarioSrv.obtener(entidadDTO.getUsuario_id());
		}
		if(usuario == null) {
			errors.rejectValue("usuario_username", "", "Seleccione un usuario o ingrese un Cuit o Alias valido!");
		}
		if(errors.hasErrors()){
			return;
		}
		if(dao.existe(usuario.getId(), entidadDTO.getRespo_id(), entidadDTO.getId())) {
			errors.rejectValue("respo_id", "", "Ya existe una Asignacion de Responsabilidad para este Usuario" + (sinSucursal?" y esa Responsabilidad":", con esa Responsabilidad y esa Sucursal"));
		}
		if(errors.hasErrors()){
			return;
		}

		RespoUsuario entidad = null;
		Timestamp fechaActual = new Timestamp(new Date().getTime());	
		if(entidadDTO.getId() != null){//Modificacion
			entidad = dao.obtener(entidadDTO.getId());

		}else{//Nuevo
			 entidad = new RespoUsuario();
			 entidad.setUsuario(usuario);
			 entidad.setCreation_date(fechaActual);
		}
		entidad.setRespo(respoSrv.obtenerWithJoins(entidadDTO.getRespo_id()));
		entidad.setActivo(entidadDTO.isActivo());
		entidad.setLast_update_date(fechaActual);
		if(entidadDTO.getId() == null){
			dao.guardar(entidad);
			entidadDTO.setId(entidad.getId());
			entidadDTO.setRespo_codigo(entidad.getRespo().toString());
		}
		//se usan en el MSP cuando la creacion de la entidad se genera de aquel lado
		entidadDTO.setLast_update_date(entidad.getLast_update_date());
		entidadDTO.setCreation_date(entidad.getCreation_date());
		entidadDTO.setId(entidad.getId());
	}

	@Override
	public RespoUsuarioDTO obtenerDTO(Long id) {
		return new RespoUsuarioDTO(dao.obtener(id));
	}

	@Override
	public List<RespoUsuarioDTO> getEntidades() {
		return dao.getEntidades();
	}
}