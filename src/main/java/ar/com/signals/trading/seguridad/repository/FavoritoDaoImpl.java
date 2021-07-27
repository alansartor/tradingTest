package ar.com.signals.trading.seguridad.repository;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.seguridad.domain.Favorito;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class FavoritoDaoImpl  extends GenericDaoImpl<Favorito> implements FavoritoDao {
	@Autowired
	public FavoritoDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<Favorito> getDomainClass() {
		return Favorito.class;
	}

	@Override
	public List<Favorito> obtenerByResponsabilidad(Respo responsabilidad) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.add(Restrictions.eq("respo", responsabilidad));
		criteria.add(Restrictions.eq("activo", true));
		return (List<Favorito>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<Favorito> getFavoritosSincronizar(Timestamp ultimaSync) {
		Query query = getSessionFactory().getCurrentSession().createQuery("select fa " + 
				" from Favorito as fa" +
				" inner join RespoUsuario as ru with ru.respo = fa.respo " +
				" left join fetch fa.respo.privilegios as pri " + //privilegios son lazy, entonces tengo que hacer join para que cargue la lista, ya que si no json no lo puede parsear para enviarlo al MSP
				" where ru.subSeccion.seccion.planta = :planta " +
				(ultimaSync!=null?" and (fa.last_update_date > :ultimaSync)":""));
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//query.setParameter("planta", sucursal);
		if(ultimaSync!=null){
			query.setTimestamp("ultimaSync", ultimaSync);
		}
		return query.list();
	}

}
