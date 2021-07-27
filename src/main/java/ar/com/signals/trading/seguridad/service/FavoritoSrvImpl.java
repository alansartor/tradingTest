package ar.com.signals.trading.seguridad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.signals.trading.seguridad.domain.Favorito;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.repository.FavoritoDao;

@Service
@Transactional
public class FavoritoSrvImpl implements FavoritoSrv {
	@Autowired FavoritoDao dao;

	@Override
	public List<Favorito> obtenerByResponsabilidad(Respo responsabilidad) {
		return dao.obtenerByResponsabilidad(responsabilidad);
	}

	@Override
	public void actualizar(Favorito entidad) {
		dao.guardar(entidad);
	}
	
}