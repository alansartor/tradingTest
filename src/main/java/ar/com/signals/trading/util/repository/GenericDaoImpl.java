package ar.com.signals.trading.util.repository;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.UnresolvableObjectException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

public abstract class GenericDaoImpl<T> extends HibernateDaoSupport implements GenericDao<T> {

	protected abstract Class<T> getDomainClass();
	
    @Autowired  
    public GenericDaoImpl(SessionFactory sessionFactory) {  
        super.setSessionFactory(sessionFactory);  
    }
    
	@Override
	public void guardar(T objeto) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(objeto);
	}

	@Override
	public T obtener(Serializable id) throws DataAccessException {
		return (T) getHibernateTemplate().get(getDomainClass(), id);
	}

	@Override
	public void eliminar(T objeto) throws DataAccessException {
		getHibernateTemplate().delete(objeto);
	}

	@Override
	public <E> E getPropiedad(Object filtro, String filtroName, String propiedadName){
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.add(Restrictions.eq(filtroName, filtro));//Filter id
		criteria.setProjection(Projections.property(propiedadName));//Projection
		@SuppressWarnings("unchecked")
		List<E> registros = (List<E>) getHibernateTemplate().findByCriteria(criteria,0,1);
		return registros.isEmpty()?null:registros.get(0);
	}
	
	@Override
	public boolean existe(Object filtro, String filtroName){
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq(filtroName, filtro));
		return 0 < ((Long) criteria.uniqueResult());
	}
	
	@Override
	public boolean existe(Object filtro, String filtroName, Object id) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq(filtroName, filtro));
		if(id != null) {
			criteria.add(Restrictions.not(Restrictions.idEq(id)));
		}
		return 0 < ((Long) criteria.uniqueResult());
	}
	
	@Override
	public T obtenerPorFiltroUnico(Object filtro, String filtroName){
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(getDomainClass());
		criteria.add(Restrictions.eq(filtroName, filtro));
		return (T) criteria.uniqueResult();
	}
	
	@Override
	public T obtenerPrimero(Object filtro, String filtroName){
		DetachedCriteria criteria = DetachedCriteria.forClass(getDomainClass());
		criteria.add(Restrictions.eq(filtroName, filtro));
		List<T> registros = (List<T>) getHibernateTemplate().findByCriteria(criteria,0,1);
		return registros.isEmpty()?null:registros.get(0);
	}
	//usamos @TableGenerator 
/*	@Override
	public BigDecimal getNextSequence(String sequenceName) {
		try {
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("select "+sequenceName+".NEXTVAL from dual");
			return ((BigDecimal)query.uniqueResult());
		}catch (SQLGrammarException e) {
			if(e.getCause() != null && e.getCause() instanceof SQLSyntaxErrorException) {
				throw new SQLGrammarException("No existe secuencia " + sequenceName,(SQLException) e.getCause());
			}
			throw e;
		}
	}*/
	
	@Override
	public void refresh(T objeto){
		getHibernateTemplate().evict(objeto);
		getHibernateTemplate().refresh(objeto);
	}
	
	@Override
	public boolean refreshOrEvict(T objeto) {
	    try {
	    	getHibernateTemplate().evict(objeto);
	    	getHibernateTemplate().refresh(objeto);
	        return true;
	    } catch (UnresolvableObjectException e) {
	        return false;
	    }
	}
	
	@Override
	public void flush(){
		getHibernateTemplate().flush();
	}
}
