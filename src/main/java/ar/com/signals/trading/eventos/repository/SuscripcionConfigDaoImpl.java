package ar.com.signals.trading.eventos.repository;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.eventos.domain.SuscripcionConfig;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class SuscripcionConfigDaoImpl extends GenericDaoImpl<SuscripcionConfig> implements SuscripcionConfigDao {
	@Autowired
	public SuscripcionConfigDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<SuscripcionConfig> getDomainClass() {
		return SuscripcionConfig.class;
	}

	@Override
	public List<SuscripcionConfig> getTradingAlertSuscript(long minutos) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());

		criteria.add(Restrictions.eq("eventoTradingAlert", true));
		criteria.add(Restrictions.le("horaInicio", minutos));
		criteria.add(Restrictions.ge("horaFin", minutos));

		return (List<SuscripcionConfig>) getHibernateTemplate().findByCriteria(criteria);
	}
}
