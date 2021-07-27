package ar.com.signals.trading.eventos.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.eventos.domain.RegistroEvento;
import ar.com.signals.trading.eventos.domain.RegistroEvento.RegistroEventoTipo;
import ar.com.signals.trading.eventos.domain.RegistroNotificacion;
import ar.com.signals.trading.eventos.dto.RegistroNotificacionDTO;
import ar.com.signals.trading.eventos.dto.SuscripcionDTO;
import ar.com.signals.trading.eventos.support.MetodoNotificacion;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class RegistroNotificacionDaoImpl extends GenericDaoImpl<RegistroNotificacion> implements RegistroNotificacionDao {
	@Autowired
	public RegistroNotificacionDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<RegistroNotificacion> getDomainClass() {
		return RegistroNotificacion.class;
	}

	@Override
	public List<RegistroNotificacion> obtenerPendientesInformar(MetodoNotificacion metodoNotificacion) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.add(Restrictions.isNull("fechaInforme"));
		if(metodoNotificacion != null) {
			criteria.add(Restrictions.eq("metodoNotificacion", metodoNotificacion));
		}
		return (List<RegistroNotificacion>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public void borrarNotificacionesViejas(Date fechaHasta) {
		//delete with joins NO se puede, entonces se puede ir por dos alternativas:
		//a)Primero hacer un select de los ids que queremos borrar, y luego hacer el delete
		//b)Hacer un delete, y en el where hacer una subquery con el select! (esto puede que no funcione en MySql por una restriccion del motor)
		
		//busco solo los id de los RegistroEvento, y con esos busco las notificaciones
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("select re.id from RegistroEvento re "
				+ " where re.fecha < :fechaHasta");
		query.setParameter("fechaHasta", fechaHasta);;
		List<Long> ids = query.list();
		if(!ids.isEmpty()) {
			query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("delete from RegistroNotificacion where registroEvento_id in (:ids)");
			query.setParameterList("ids", ids);
			query.executeUpdate();
		}
	}

	@Override
	public List<RegistroNotificacion> getInformadasSinMarcar(Usuario usuario, MetodoNotificacion metodoNotificacion) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("registroEvento", "re");
		criteria.add(Restrictions.eq("usuario", usuario));
		criteria.add(Restrictions.eq("metodoNotificacion", metodoNotificacion));
		criteria.add(Restrictions.isNotNull("fechaInforme"));
		criteria.add(Restrictions.eq("visto", false));
		criteria.addOrder(Order.desc("re.fecha"));
		
		return (List<RegistroNotificacion>) getHibernateTemplate().findByCriteria(criteria);
		
	}

	@Override
	public List<RegistroNotificacion> getPendientes(Privilegio evento, RegistroEventoTipo registroEventoTipo, Long idRelacion) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("registroEvento", "re");
		criteria.add(Restrictions.eq("re.evento", evento));
		criteria.add(Restrictions.eq("re.registroEventoTipo", registroEventoTipo));
		criteria.add(Restrictions.eq("re.idRelacion", idRelacion));
		criteria.add(Restrictions.isNull("fechaInforme"));
		return (List<RegistroNotificacion>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<RegistroNotificacion> ultimasVistas(Usuario usuario, boolean vistas, Integer top) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("registroEvento", "re");
		criteria.add(Restrictions.eq("usuario", usuario));
		
		criteria.add(Restrictions.eq("visto", vistas));
		criteria.addOrder(Order.desc("re.fecha"));
		return (List<RegistroNotificacion>) (top!=null?getHibernateTemplate().findByCriteria(criteria, 0, top):getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public List<RegistroNotificacion> getByIds(Usuario usuario, List<Long> ids) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("registroEvento", "re");
		criteria.add(Restrictions.eq("usuario", usuario));
		criteria.add(Restrictions.in("re.id", ids));
		return (List<RegistroNotificacion>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public List<RegistroNotificacionDTO> getRegistrosNotificacionesDTO(Usuario usuario) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("registroEvento", "re");
		criteria.createAlias("re.sujeto", "suj");
		criteria.createAlias("re.sucursal", "suc", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("re.unidadProductiva", "uni", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("usuario", usuario));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("re.id"), "evento_id")
				.add(Projections.property("re.fecha"), "evento_fecha")
				.add(Projections.property("re.evento"), "evento")
				.add(Projections.property("suj.id"), "sujeto_id")
				.add(Projections.property("suj.descripcion"), "sujeto_descripcion")
				.add(Projections.property("suc.id"), "sucursal_id")
				.add(Projections.property("suc.codigo"), "sucursal_codigo")
				.add(Projections.property("uni.id"), "unidadProductiva_id")
				.add(Projections.property("uni.codigo"), "unidadProductiva_codigo")
				.add(Projections.property("re.observaciones"), "observaciones"));
		criteria.setResultTransformer(Transformers.aliasToBean(RegistroNotificacionDTO.class));
		
		
		criteria.addOrder(Order.asc("suc.codigo"));
		criteria.addOrder(Order.asc("evento"));
		//siempre filtrar por una sucursal, sino podrian ver datos de otros!
		
		return (List<RegistroNotificacionDTO>) getHibernateTemplate().findByCriteria(criteria);
	}
}
