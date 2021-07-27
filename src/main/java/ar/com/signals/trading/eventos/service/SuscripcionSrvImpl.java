package ar.com.signals.trading.eventos.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.SeguridadUtil;
import ar.com.signals.trading.util.support.BusinessGenericException;
import ar.com.signals.trading.eventos.domain.Suscripcion;
import ar.com.signals.trading.eventos.domain.SuscripcionConfig;
import ar.com.signals.trading.eventos.dto.SuscripcionDTO;
import ar.com.signals.trading.eventos.repository.SuscripcionDao;

@Service
@Transactional
public class SuscripcionSrvImpl implements SuscripcionSrv{
	@Resource private SuscripcionDao dao;

	
	@Override
	public Suscripcion getBySucursPepedTipoEvento(Usuario usuario, Privilegio evento) {
		return dao.getBySucursPepedTipoEvento(usuario, evento);
	}
	
	@Override
	public void validateAndPersist(SuscripcionDTO entidadDTO, BindingResult errors, Usuario usuario) {
		if(entidadDTO.getEventos().isEmpty()) {
			errors.rejectValue("eventos", "", "Debe seleccionar al menos un evento");
		}

		if(errors.hasErrors()) {
			return;
		}
		
		for (Privilegio evento : entidadDTO.getEventos()) {
			Suscripcion entidad = dao.getBySucursPepedTipoEvento(usuario, evento);
			if(entidad != null) {
				errors.rejectValue("eventos", "", "El usuario ya esta Suscripto a evento " +  evento);
				break;
			}
		}
	
		if(errors.hasErrors()) {
			return;
		}
		Timestamp fechaActual = new Timestamp(new Date().getTime());

		Suscripcion entidad = new Suscripcion();
		entidad.setUsuario(usuario);
		entidad.setEvento(entidadDTO.getEventos().get(0));
		entidad.setCreation_date(fechaActual);
		entidad.setLast_update_date(fechaActual);
		dao.guardar(entidad);
	
	}

	@Override
	public SuscripcionDTO eliminar(Usuario usuario, Long id) {
		Suscripcion entidad = dao.obtener(id);
		SeguridadUtil.validarAutorizacion(usuario, Privilegio.SUSCRIPCION_ELIMINAR);
		if(!usuario.equals(entidad.getUsuario())) {
			throw new BusinessGenericException("No puede eliminar la Suscripcion de otro Usuario");
		}
		SuscripcionDTO entidadDTO = new SuscripcionDTO();
		entidadDTO.setId(entidad.getId());
		entidadDTO.setEvento(entidad.getEvento());
		dao.eliminar(entidad);
		return entidadDTO;
	}

	@Override
	public List<SuscripcionDTO> getSuscripcionesDTO(Usuario usuario) {
		return dao.getSuscripcionesDTO(usuario);
	}

	@Override
	public List<Usuario> getUsuariosSuscriptos(Privilegio evento) {
		return dao.getUsuariosSuscriptos(evento);
	}

	@Override
	public void eliminar(Usuario usuario, Privilegio evento) {
		Suscripcion suscripcion = dao.getBySucursPepedTipoEvento(usuario, evento);
		if(suscripcion != null) {
			dao.eliminar(suscripcion);
		}
	}

	@Override
	public void guardar(Suscripcion entidad) {
		dao.guardar(entidad);
	}

}