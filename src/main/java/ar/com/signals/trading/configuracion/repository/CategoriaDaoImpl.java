package ar.com.signals.trading.configuracion.repository;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class CategoriaDaoImpl extends GenericDaoImpl<Categoria> implements CategoriaDao {
	@Autowired
	public CategoriaDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<Categoria> getDomainClass() {
		return Categoria.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Categoria> getCategoriasSincronizacion(Timestamp ultimaSync) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("categoriaSet", "cats");
		if(ultimaSync != null){
			criteria.add(Restrictions.or(Restrictions.gt("last_update_date", ultimaSync), Restrictions.gt("cats.last_update_date", ultimaSync)));
		}
		return (List<Categoria>) getHibernateTemplate().findByCriteria(criteria);
	}
}
