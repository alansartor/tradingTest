package ar.com.signals.trading.eventos.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.eventos.domain.RegistroEvento;
import ar.com.signals.trading.eventos.domain.RegistroEvento.RegistroEventoTipo;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class RegistroEventoDaoImpl extends GenericDaoImpl<RegistroEvento> implements RegistroEventoDao {
	@Autowired
	public RegistroEventoDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<RegistroEvento> getDomainClass() {
		return RegistroEvento.class;
	}

	@Override
	public List<RegistroEvento> getPorMovimientoAndTipoEvento(String movimiento_id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.add(Restrictions.eq("movimiento_id", movimiento_id));
		criteria.addOrder(Order.desc("fecha"));
		return (List<RegistroEvento>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public boolean existeRegistroEvento(Privilegio evento, Long idRelacion, RegistroEventoTipo registroEventoTipo) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("evento", evento));
		criteria.add(Restrictions.eq("idRelacion", idRelacion));
		criteria.add(Restrictions.eq("registroEventoTipo", registroEventoTipo));
		return 0 < ((Long) criteria.uniqueResult());
	}

}
