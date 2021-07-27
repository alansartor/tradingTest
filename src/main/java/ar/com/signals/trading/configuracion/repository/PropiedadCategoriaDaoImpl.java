package ar.com.signals.trading.configuracion.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.com.signals.trading.configuracion.domain.PropiedadCategoria;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaDTO2;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaTipoComprobanteDTO;
import ar.com.signals.trading.configuracion.support.CategoriaException;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.util.repository.GenericDaoImpl;

@Repository
public class PropiedadCategoriaDaoImpl  extends GenericDaoImpl<PropiedadCategoria> implements PropiedadCategoriaDao {
	@Autowired
	public PropiedadCategoriaDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<PropiedadCategoria> getDomainClass() {
		return PropiedadCategoria.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PropiedadCategoriaDTO2> getPropiedadesDTO(List<Long> unidadOperativaIds, boolean superUser) {
		//abstract_sujeto_id;//referencia a Sujeto, Sucursal o UnidadProductiva, puede ser null. Esas tres entidades comparte id
		String queryString = "select pc.id as id, pc.categoria.id as codigo, suj.descripcion as sujetoDescripcion, suc.codigo as sucursalCodigo, up.codigo as unidadProductivaCodigo, pc.valor as valor, pc.activo as activo" +
				" from PropiedadCategoria as pc" +
				" left outer join Sujeto suj with suj.id = pc.abstract_sujeto_id" +
				" left outer join Sucursal suc with suc.id = pc.abstract_sujeto_id" +
				" left outer join UnidadProductiva up with up.id = pc.abstract_sujeto_id";
		if(!superUser) {
			if(!unidadOperativaIds.isEmpty()) {
				queryString += " and up.id in (:unidadOperativaIds) ";
			}
		}
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		query.setResultTransformer(Transformers.aliasToBean(PropiedadCategoriaDTO2.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PropiedadCategoria obtener(String codigoCategoria, Long planta_id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.createAlias("categoria", "cat");
		criteria.add(Restrictions.eq("cat.id", codigoCategoria));
		criteria.add(Restrictions.eq("activo", true));

		List<PropiedadCategoria> entidades = (List<PropiedadCategoria>) getHibernateTemplate().findByCriteria(criteria);
		if(!entidades.isEmpty()){
			PropiedadCategoria propiedadCategoria = entidades.get(0);
			if(entidades.size() > 1){
				throw new CategoriaException("Esta queriendo obtener una PropiedadCategoria con SetNivel: " + propiedadCategoria.getCategoria().getCategoriaSet().getNivelSet() + 
												" pero le falta el filtro " + propiedadCategoria.getCategoria().getCategoriaSet().getNivelSet() + " id");
			}
			return propiedadCategoria;
		}
		return null;
	}

	@Override
	public boolean existe(String codigoCategoria, Long empresa_id, Long planta_id) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.setProjection(Projections.rowCount());
		criteria.createAlias("categoria", "cat");
		criteria.add(Restrictions.eq("cat.id", codigoCategoria));
		//criteria.add(Restrictions.eq("activo", true));
		if(empresa_id != null){
			criteria.add(Restrictions.eq("organization_id", empresa_id));
		}else if(planta_id != null){
			criteria.add(Restrictions.eq("organization_id", planta_id));
		}
		return 0 < ((Long) criteria.uniqueResult());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PropiedadCategoria> getPropiedadesCategoriaSincronizar(Long empresaId, Long plantaId, Timestamp ultimaSync) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.add(Restrictions.or(Restrictions.isNull("organization_id"), Restrictions.eq("organization_id", empresaId), Restrictions.eq("organization_id", plantaId)));
		if(ultimaSync != null){//Si alguno de las categorias se modifica, entonces se actualiza en otro plansincronizacion
			criteria.add(Restrictions.gt("last_update_date", ultimaSync));
		}
		return (List<PropiedadCategoria>) getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public boolean tieneCategoriaActiva(String codigoCategoria, Long empresa_id, Long planta_id) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.createAlias("categoria", "cat");
		criteria.add(Restrictions.eq("cat.id", codigoCategoria));
		criteria.add(Restrictions.eq("activo", true));
		if(empresa_id != null){
			criteria.add(Restrictions.eq("organization_id", empresa_id));
		}else if(planta_id != null){
			criteria.add(Restrictions.eq("organization_id", planta_id));
		}
		criteria.setProjection(Projections.rowCount());
		return 0 < ((Long) criteria.uniqueResult());
	}

	@Override
	public boolean existe(String codigoCategoria, String valor, Long id) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.setProjection(Projections.rowCount());
		criteria.createAlias("categoria", "cat");
		criteria.add(Restrictions.eq("cat.id", codigoCategoria));
		criteria.add(Restrictions.eq("valor", valor));
		if(id != null){
			criteria.add(Restrictions.ne("id", id));
		}
		return 0 < ((Long) criteria.uniqueResult());
	}

	@Override
	public boolean existe(String codigoCategoria, Long sujeto_id, Long sucursal_id, Long unidadProductiva_id, Long id) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.setProjection(Projections.rowCount());
		criteria.createAlias("categoria", "cat");
		criteria.add(Restrictions.eq("cat.id", codigoCategoria));
		if(sujeto_id != null){
			criteria.add(Restrictions.eq("abstract_sujeto_id", sujeto_id));
		}else if(sucursal_id != null){
			criteria.add(Restrictions.eq("abstract_sujeto_id", sucursal_id));
		}else if(unidadProductiva_id != null){
			criteria.add(Restrictions.eq("abstract_sujeto_id", unidadProductiva_id));
		}
		if(id != null){
			criteria.add(Restrictions.ne("id", id));
		}
		return 0 < ((Long) criteria.uniqueResult());
	}
}
