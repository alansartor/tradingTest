package ar.com.signals.trading.configuracion.repository;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.configuracion.domain.RespoCategoria;
import ar.com.signals.trading.seguridad.domain.Respo;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class RespoCategoriaDaoImpl  extends GenericDaoImpl<RespoCategoria> implements RespoCategoriaDao {
	@Autowired
	public RespoCategoriaDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<RespoCategoria> getDomainClass() {
		return RespoCategoria.class;
	}

	@Override
	public List<RespoCategoria> getRespoCategoriasSincronizar(Timestamp ultimaSync) {
		Query query = getSessionFactory().getCurrentSession().createQuery("select rc " + 
				" from RespoCategoria as rc" + 
				" inner join RespoUsuario as ru with ru.respo = rc.respo " + 
				" where ru.subSeccion.seccion.planta = :planta " +
				(ultimaSync!=null?" and (rc.last_update_date > :ultimaSync or ru.last_update_date > :ultimaSync2)":""));
				//" and rp.activo = :activo");sincronizar todo, tanto los habilitados como los que no!
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(ultimaSync!=null){
			query.setTimestamp("ultimaSync", ultimaSync);
			query.setTimestamp("ultimaSync2", ultimaSync);
		}
		return query.list();
	}

	@Override
	public List<Categoria> getEntidades(Respo respo, String categoriaSet_id, Privilegio privilegio) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.add(Restrictions.eq("respo", respo));
		criteria.add(Restrictions.eq("privilegio", privilegio));
		criteria.add(Restrictions.eq("activo", true));
		criteria.createAlias("categoria", "cat");
		criteria.createAlias("cat.categoriaSet", "catSet");
		criteria.add(Restrictions.eq("catSet.id", categoriaSet_id));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("categoria")));
		//No se repite la categoria, porque hay un unique constraint respo, privilegio, categoria
		return (List<Categoria>) getHibernateTemplate().findByCriteria(criteria);
	}

}
