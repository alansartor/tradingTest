package ar.com.signals.trading.configuracion.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.util.repository.GenericDaoImpl;
import ar.com.signals.trading.configuracion.domain.CategoriaSet;

@Repository
public class CategoriaSetDaoImpl extends GenericDaoImpl<CategoriaSet> implements CategoriaSetDao {
	@Autowired
	public CategoriaSetDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<CategoriaSet> getDomainClass() {
		return CategoriaSet.class;
	}
}
