package ar.com.signals.trading.configuracion.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.signals.trading.configuracion.domain.CategoriaSet;
import ar.com.signals.trading.configuracion.repository.CategoriaSetDao;
import ar.com.signals.trading.configuracion.support.EntitySet;
import ar.com.signals.trading.configuracion.support.ICategoriaSet;

@Service
@Transactional
public class CategoriaSetSrvImpl implements CategoriaSetSrv{
	@Resource private CategoriaSetDao dao;
	
	@Override
	public CategoriaSet obtener(String codigo) {
		return dao.obtener(codigo);
	}

	@Override
	public CategoriaSet crearCategoriaSet(ICategoriaSet categoriaSet, EntitySet entitySet) {
		CategoriaSet entidad = new CategoriaSet();
		entidad.setId(categoriaSet.getName());
		entidad.setDescripcion(categoriaSet.getDescripcion());
		entidad.setNivelSet(categoriaSet.getNivelSet());
		entidad.setTipoSet(categoriaSet.getTipoSet());
		entidad.setActivo(true);
		//Toda entidad que se crea en el MSC y se sincroniza en el MSP debe tener en la fecha de ultima actualizacion un plus de segundos!
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		entidad.setCreation_date(fechaActual);
		entidad.setLast_update_date(fechaActual);
		entidad.setEntitySet(entitySet);
		dao.guardar(entidad);
		return entidad;
	}

	@Override
	public void actualizar(CategoriaSet entidad) {
		dao.guardar(entidad);
	}	
}