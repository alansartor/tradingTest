package ar.com.signals.trading.seguridad.repository;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.UsuarioDTO;
import ar.com.signals.trading.util.repository.GenericDaoImpl;
import ar.com.signals.trading.util.rest.AutocompleteDTO;
import ar.com.signals.trading.util.rest.GenericAutocompleteDTO;

@Repository
public class UsuarioDaoImpl  extends GenericDaoImpl<Usuario> implements UsuarioDao {
	@Autowired
	public UsuarioDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<Usuario> getDomainClass() {
		return Usuario.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioDTO> getEntidades(String username, String descripcion) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		if(StringUtils.isNotBlank(username)){
			criteria.add(Restrictions.like("username", username, MatchMode.START));
		}
		if(StringUtils.isNotBlank(descripcion)){
			criteria.add(Restrictions.like("descripcion", descripcion, MatchMode.START));
		}
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id")
				.add(Projections.property("username"), "username")
				.add(Projections.property("descripcion"), "descripcion")
		        .add(Projections.property("user_email"), "user_email")
				.add(Projections.property("user_enabled"), "user_enabled")
				.add(Projections.property("creation_date"), "creation_date")
				.add(Projections.property("last_update_date"), "last_update_date"));
			criteria.setResultTransformer(Transformers.aliasToBean(UsuarioDTO.class));
		return (List<UsuarioDTO>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<? extends AutocompleteDTO> getAutocompleteDTO(String partial) {		
		Query query = getSessionFactory().getCurrentSession().createQuery("select user.id as id, user.username || ' ' || user.descripcion as value " +
				" from Usuario user " + 
				" where user.user_enabled = :suActivo" + 
				(partial != null?" and (user.username like :codigo or user.descripcion like :descripcion) ":"") +
				" order by user.username");
		query.setResultTransformer(Transformers.aliasToBean(GenericAutocompleteDTO.class));
		query.setParameter("suActivo", true);
		if(partial != null) {
			query.setParameter("codigo", partial.toLowerCase() + "%");
			query.setParameter("descripcion", partial.toUpperCase() + "%");
		}
		query.setMaxResults(500);//pongo un tope
		return query.list();
	}
	
	@Override
	public boolean existe(String username) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("username", username));
		return 0 < ((Long) criteria.uniqueResult());
	}

	@Override
	public Usuario obtenerPorUsername(String username) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.add(Restrictions.eq("username", username));
		return (Usuario) criteria.uniqueResult();
	}

/*	public List<Usuario> getEntidades(Planta planta, Timestamp ultimaSync) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		//criteria.createAlias("respos", "reUsu", JoinType.LEFT_OUTER_JOIN);
		//criteria.createAlias("respos.plantas", "pla", JoinType.LEFT_OUTER_JOIN);
		//No hago estos joins, debido a que hibernate esta repitiendo registros, algo no anda bien con el DISTINCT_ROOT_ENTITY cuando hay varios niveles de joins
		//criteria.createAlias("respos.respo.privilegios", "pri", JoinType.LEFT_OUTER_JOIN);
		//criteria.createAlias("respos.respo.privilegiosProductosGenericos", "priProd", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("pla.organization_id", planta.getOrganization_id()));
		if(ultimaSync != null){
			criteria.add(Restrictions.gt("last_update_date", ultimaSync));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Usuario>) getHibernateTemplate().findByCriteria(criteria);
	}*/
}
