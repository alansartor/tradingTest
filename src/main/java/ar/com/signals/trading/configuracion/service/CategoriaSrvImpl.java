package ar.com.signals.trading.configuracion.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.configuracion.domain.CategoriaSet;
import ar.com.signals.trading.configuracion.repository.CategoriaDao;
import ar.com.signals.trading.configuracion.support.ClienteCategoriaEnum;
import ar.com.signals.trading.configuracion.support.EntitySet;
import ar.com.signals.trading.configuracion.support.ProductoCategoriaEnum;
import ar.com.signals.trading.configuracion.support.ProductoGenericoCategoriaEnum;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.configuracion.support.SucursalCategoriaEnum;

@Service
@Transactional
public class CategoriaSrvImpl implements CategoriaSrv{
	@Resource private CategoriaDao dao;
	@Resource private CategoriaSetSrv categoriaSetSrv;
	
	@Override
	public Categoria obtener(String codigo) {
		return dao.obtener(codigo);
	}

	@Override
	public Categoria crearCategoriaForPropiedad(PropiedadCategoriaEnum codigo) {
		CategoriaSet categoriaSet = categoriaSetSrv.obtener(codigo.getCategoriaSet().name());
		if(categoriaSet == null){
			categoriaSet = categoriaSetSrv.crearCategoriaSet(codigo.getCategoriaSet(), EntitySet.propiedad);
		}
		Categoria entidad = new Categoria();
		entidad.setId(codigo.name());
		entidad.setCategoriaSet(categoriaSet);
		entidad.setTipoDato(codigo.getTipoDato());
		entidad.setDescripcion(codigo.getDescripcion());
		entidad.setActivo(true);
		//Toda entidad que se crea en el MSC y se sincroniza en el MSP debe tener en la fecha de ultima actualizacion un plus de segundos!
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		entidad.setCreation_date(fechaActual);
		entidad.setLast_update_date(fechaActual);
		dao.guardar(entidad);
		return entidad;
	}

	@Override
	public void actualizar(Categoria entidad) {
		dao.guardar(entidad);
	}

	@Override
	public Categoria crearCategoriaForProducto(ProductoCategoriaEnum codigo) {
		CategoriaSet categoriaSet = categoriaSetSrv.obtener(codigo.getCategoriaSet().name());
		if(categoriaSet == null){
			categoriaSet = categoriaSetSrv.crearCategoriaSet(codigo.getCategoriaSet(), EntitySet.producto);
		}
		Categoria entidad = new Categoria();
		entidad.setId(codigo.name());
		entidad.setCategoriaSet(categoriaSet);
		entidad.setTipoDato(codigo.getTipoDato());
		entidad.setDescripcion(codigo.getDescripcion());
		entidad.setActivo(true);
		//Toda entidad que se crea en el MSC y se sincroniza en el MSP debe tener en la fecha de ultima actualizacion un plus de segundos!
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		entidad.setCreation_date(fechaActual);
		entidad.setLast_update_date(fechaActual);
		dao.guardar(entidad);
		return entidad;
	}

	@Override
	public Categoria crearCategoriaForSucursal(SucursalCategoriaEnum codigo) {
		CategoriaSet categoriaSet = categoriaSetSrv.obtener(codigo.getCategoriaSet().name());
		if(categoriaSet == null){
			categoriaSet = categoriaSetSrv.crearCategoriaSet(codigo.getCategoriaSet(), EntitySet.sucursal);
		}
		Categoria entidad = new Categoria();
		entidad.setId(codigo.name());
		entidad.setCategoriaSet(categoriaSet);
		entidad.setTipoDato(codigo.getTipoDato());
		entidad.setDescripcion(codigo.getDescripcion());
		entidad.setActivo(true);
		//Toda entidad que se crea en el MSC y se sincroniza en el MSP debe tener en la fecha de ultima actualizacion un plus de segundos!
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		entidad.setCreation_date(fechaActual);
		entidad.setLast_update_date(fechaActual);
		dao.guardar(entidad);
		return entidad;
	}

	@Override
	public Categoria crearCategoriaForCliente(ClienteCategoriaEnum codigo) {
		CategoriaSet categoriaSet = categoriaSetSrv.obtener(codigo.getCategoriaSet().name());
		if(categoriaSet == null){
			categoriaSet = categoriaSetSrv.crearCategoriaSet(codigo.getCategoriaSet(), EntitySet.cliente);
		}
		Categoria entidad = new Categoria();
		entidad.setId(codigo.name());
		entidad.setCategoriaSet(categoriaSet);
		entidad.setTipoDato(codigo.getTipoDato());
		entidad.setDescripcion(codigo.getDescripcion());
		entidad.setActivo(true);
		//Toda entidad que se crea en el MSC y se sincroniza en el MSP debe tener en la fecha de ultima actualizacion un plus de segundos!
		Timestamp fechaActual = new Timestamp(new Date().getTime());entidad.setCreation_date(fechaActual);
		entidad.setLast_update_date(fechaActual);
		dao.guardar(entidad);
		return entidad;
	}

	@Override
	public Categoria crearCategoriaForProductoGenerico(ProductoGenericoCategoriaEnum codigo) {
		CategoriaSet categoriaSet = categoriaSetSrv.obtener(codigo.getCategoriaSet().name());
		if(categoriaSet == null){
			categoriaSet = categoriaSetSrv.crearCategoriaSet(codigo.getCategoriaSet(), EntitySet.productoGenerico);
		}
		Categoria entidad = new Categoria();
		entidad.setId(codigo.name());
		entidad.setCategoriaSet(categoriaSet);
		entidad.setTipoDato(codigo.getTipoDato());
		entidad.setDescripcion(codigo.getDescripcion());
		entidad.setActivo(true);
		//Toda entidad que se crea en el MSC y se sincroniza en el MSP debe tener en la fecha de ultima actualizacion un plus de segundos!
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		entidad.setCreation_date(fechaActual);
		entidad.setLast_update_date(fechaActual);
		dao.guardar(entidad);
		return entidad;
	}
	
}