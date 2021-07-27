package ar.com.signals.trading.eventos.repository;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.util.repository.GenericDaoImpl;
import ar.com.signals.trading.eventos.domain.Suscripcion;
import ar.com.signals.trading.eventos.dto.SuscripcionDTO;

@Repository
public class SuscripcionDaoImpl extends GenericDaoImpl<Suscripcion> implements SuscripcionDao {
	@Autowired
	public SuscripcionDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<Suscripcion> getDomainClass() {
		return Suscripcion.class;
	}

	@Override
	public Suscripcion getBySucursPepedTipoEvento(Usuario usuario, Privilegio evento) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.add(Restrictions.eq("usuario", usuario));
		criteria.add(Restrictions.eq("evento", evento));
		return (Suscripcion) criteria.uniqueResult();
	}

	@Override
	public List<SuscripcionDTO> getSuscripcionesDTO(Usuario usuario) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("sucursal", "suc", JoinType.LEFT_OUTER_JOIN);//por si hay suscripciones globales sin sucursal!
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id")
				.add(Projections.property("suc.codigo"), "sucursal_codigo")
		        .add(Projections.property("evento"), "evento"));
		criteria.setResultTransformer(Transformers.aliasToBean(SuscripcionDTO.class));
		criteria.addOrder(Order.asc("suc.codigo"));
		criteria.addOrder(Order.asc("evento"));
		//siempre filtrar por una sucursal, sino podrian ver datos de otros!
		//criteria.add(Restrictions.or(Restrictions.in("suc.id", sucursal_id), Restrictions.isNull("sucursal")));//por si hay suscripciones globales sin sucursal!
		criteria.add(Restrictions.eq("usuario", usuario));		
		return (List<SuscripcionDTO>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<Usuario> getUsuariosSuscriptos(Privilegio evento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("usuario", "usu");
		
		criteria.add(Restrictions.eq("evento", evento));
		
		criteria.setProjection(Projections.property("usuario"));
		return (List<Usuario>) getHibernateTemplate().findByCriteria(criteria);
	}
}
