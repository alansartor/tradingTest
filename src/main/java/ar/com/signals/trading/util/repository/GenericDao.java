package ar.com.signals.trading.util.repository;

import java.io.Serializable;
import org.hibernate.NonUniqueResultException;
import org.springframework.dao.DataAccessException;

public interface GenericDao<T> {

	public void guardar(T entidad) throws DataAccessException;

	public T obtener(Serializable id)  throws DataAccessException;

	public void eliminar(T entidad) throws DataAccessException;
	
	//public List<T> obtenerTodos()  throws DataAccessException;
	
	/**
	 * Metodo que realiza una projection a la entidad manejada devolviendo la propiedad 'propiedadName' de la clase 'E'
	 * filtrando por el valor 'filtro' en la propiedad 'filtroName'
	 * @param <E>
	 * @param id
	 * @param idName
	 * @param propiedadName
	 * @return
	 */
	public <E> E getPropiedad(Object filtro, String filtroName, String propiedadName);
	
	/**
	 * Metodo que verifica si existe entidad
	 * filtrando por el valor 'filtro' en la propiedad 'filtroName'
	 * @param filtro
	 * @param filtroName
	 * @return
	 */
	public boolean existe(Object filtro, String filtroName);
	
	/**
	 * Metodo que verifica si existe entidad
	 * filtrando por el valor 'filtro' en la propiedad 'filtroName'
	 * pero que el id sea distinto que el que se pasa por parametro (si id es null entonces no se usa)
	 * @param filtro
	 * @param filtroName
	 * @param id
	 * @return
	 */
	public boolean existe(Object filtro, String filtroName, Object id);
	
	/**
	 * Devuelve el siguiente valor de la sequencia que se manda como parametro
	 * @param sequenceName
	 * @return
	 */
	//public BigDecimal getNextSequence(String sequenceName);
	
	/**
	 * Vuelve a traer el objeto desde la base de datos
	 * @param objeto
	 */
	public void refresh(T entidad);
	
	/**
	 * Vuelve a traer el objeto desde la base de datos
	 * pero si el objeto ya no existe mas (lo eliminaron en otro hilo)
	 * entonces devuelve false! 
	 * @param objeto
	 * @return
	 */
	public boolean refreshOrEvict(T objeto);
	
	public void flush();
	
	/**
	 * Buscar entidad por unique column
	 * Si la propiedad mo tiene unique constraint en la base, y llega a haber mas de un regitro, este metodo arrojara excepcion
	 * @param filtro
	 * @param filtroName
	 * @return
	 * @throws NonUniqueResultException
	 */
	public T obtenerPorFiltroUnico(Object filtro, String filtroName) throws NonUniqueResultException;
	
	/**
	 * Devuelve un registro que coincida con los buscado
	 * @param razonSocial
	 * @param string
	 * @return
	 */
	T obtenerPrimero(Object filtro, String filtroName);
	
}