package ar.com.signals.trading.seguridad.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.seguridad.domain.RespoUsuario;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.RespoUsuarioDTO;
import ar.com.signals.trading.seguridad.dto.ResponsabilidadDTO;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class RespoUsuarioDaoImpl  extends GenericDaoImpl<RespoUsuario> implements RespoUsuarioDao {
	@Autowired
	public RespoUsuarioDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<RespoUsuario> getDomainClass() {
		return RespoUsuario.class;
	}

	@Override
	public List<ResponsabilidadDTO> getResponsabilidadesDTO(Usuario usuario) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("respo", "res");
		criteria.add(Restrictions.eq("usuario", usuario));
		criteria.add(Restrictions.eq("activo", true));
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("res.id"), "id")
				.add(Projections.property("res.codigo"), "codigo")
				.add(Projections.property("res.descripcion"), "descripcion")
				.add(Projections.property("res.tipo"), "tipo")));
		criteria.setResultTransformer(Transformers.aliasToBean(ResponsabilidadDTO.class));
		return (List<ResponsabilidadDTO>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public boolean existe(Long usuario_id, Long respo_id, Long id) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.createAlias("usuario", "usu");
		criteria.createAlias("respo", "res");
		criteria.setProjection(Projections.rowCount());
		//criteria.add(Restrictions.eq("tipoOperacion", tipoOperacion));
		criteria.add(Restrictions.eq("usu.id", usuario_id));
		criteria.add(Restrictions.eq("res.id", respo_id));
		
		if(id != null){//Busco que no exista otro asi
			criteria.add(Restrictions.ne("id", id));
		}
		return 0 < ((Long) criteria.uniqueResult());
	}

	@Override
	public List<RespoUsuarioDTO> getEntidades() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("usuario", "usu");//usu1_
		criteria.createAlias("respo", "res");//res3_
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id")
				.add(Projections.property("usu.username"), "usuario_username")
				.add(Projections.property("usu.descripcion"), "usuario_descripcion")
				.add(Projections.property("res.id"), "respo_id")
				.add(Projections.property("res.codigo"), "respo_codigo")
				.add(Projections.property("activo"), "activo")
				.add(Projections.property("creation_date"), "creation_date")
				.add(Projections.property("last_update_date"), "last_update_date"));
		criteria.setResultTransformer(Transformers.aliasToBean(RespoUsuarioDTO.class));
		criteria.addOrder(Order.asc("usu.descripcion"));
		criteria.addOrder(Order.asc("res.id"));
		return (List<RespoUsuarioDTO>) getHibernateTemplate().findByCriteria(criteria);
	}
}
