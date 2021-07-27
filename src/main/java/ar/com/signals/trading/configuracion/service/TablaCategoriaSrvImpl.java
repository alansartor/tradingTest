package ar.com.signals.trading.configuracion.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.signals.trading.configuracion.domain.TablaCategoria;
import ar.com.signals.trading.configuracion.repository.TablaCategoriaDao;

@Service
@Transactional
public class TablaCategoriaSrvImpl implements TablaCategoriaSrv {
	@Autowired TablaCategoriaDao dao;

	@Override
	public BigDecimal obtener(String categoria_id, BigDecimal valor) {
		return dao.obtener(categoria_id, valor);
	}

	@Override
	public void actualizar(TablaCategoria entidad) {
		dao.guardar(entidad);
	}
}