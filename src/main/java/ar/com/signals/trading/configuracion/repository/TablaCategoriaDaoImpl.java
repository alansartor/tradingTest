package ar.com.signals.trading.configuracion.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.configuracion.domain.TablaCategoria;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class TablaCategoriaDaoImpl  extends GenericDaoImpl<TablaCategoria> implements TablaCategoriaDao {
	@Autowired
	public TablaCategoriaDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<TablaCategoria> getDomainClass() {
		return TablaCategoria.class;
	}

	@Override
	public BigDecimal obtener(String categoria_id, BigDecimal valor) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.createAlias("categoria", "cat");
		criteria.add(Restrictions.eq("cat.id", categoria_id));
		criteria.add(Restrictions.eq("valor", valor));
		TablaCategoria entidad = (TablaCategoria) criteria.uniqueResult();
		return entidad != null && entidad.isActivo()?entidad.getValorRelacionado():null;
	}

	@Override
	public List<TablaCategoria> getTablasCategoriaSincronizar(Timestamp ultimaSync) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		if(ultimaSync != null){
			criteria.add(Restrictions.gt("last_update_date", ultimaSync));
		}
		return (List<TablaCategoria>) getHibernateTemplate().findByCriteria(criteria);
	}
}
