package ar.com.signals.trading.configuracion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.configuracion.domain.RespoCategoria;
import ar.com.signals.trading.configuracion.repository.RespoCategoriaDao;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.support.Privilegio;

@Service
@Transactional
public class RespoCategoriaSrvImpl implements RespoCategoriaSrv {
	@Autowired RespoCategoriaDao dao;

	@Override
	public void actualizar(RespoCategoria entidad) {
		dao.guardar(entidad);
	}

	@Override
	public List<Categoria> getEntidades(Respo respo, String categoriaSet_id, Privilegio privilegio) {
		return dao.getEntidades(respo, categoriaSet_id, privilegio);
	}


}