package ar.com.signals.trading.seguridad.repository;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class RespoDaoImpl  extends GenericDaoImpl<Respo> implements RespoDao {
	@Autowired
	public RespoDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<Respo> getDomainClass() {
		return Respo.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Respo> getEntidades() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		return (List<Respo>) getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResponsabilidadDTO> getEntidadesDTO(List<TipoResponsabilidad> tipos) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		if(tipos != null && !tipos.isEmpty()) {
			criteria.add(Restrictions.in("tipo", tipos));
		}
		criteria.setProjection(Projections.projectionList()
		        .add(Projections.property("id"), "id")
		        .add(Projections.property("codigo"), "codigo")
		        .add(Projections.property("descripcion"), "descripcion"));
		criteria.setResultTransformer(Transformers.aliasToBean(ResponsabilidadDTO.class));
		return (List<ResponsabilidadDTO>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<Respo> getEntidadesWithJoins(Timestamp ultimaSync) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("privilegios", "priv", JoinType.LEFT_OUTER_JOIN);
		if(ultimaSync != null){
			criteria.add(Restrictions.gt("last_update_date", ultimaSync));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Respo>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public Respo obtenerWithJoins(Long id) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.createAlias("privilegios", "priv", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		return (Respo) criteria.uniqueResult();
	}

	@Override
	public boolean existe(String CODIGO, Long id) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.setProjection(Projections.rowCount());
		//criteria.add(Restrictions.eq("tipoOperacion", tipoOperacion));
		criteria.add(Restrictions.eq("codigo", CODIGO));
		
		if(id != null){//Busco que no exista otro asi
			criteria.add(Restrictions.ne("id", id));
		}
		return 0 < ((Long) criteria.uniqueResult());
	}
}
