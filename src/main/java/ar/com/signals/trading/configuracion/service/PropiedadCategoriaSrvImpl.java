package ar.com.signals.trading.configuracion.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import ar.com.signals.trading.configuracion.domain.Categoria;
import ar.com.signals.trading.configuracion.domain.PropiedadCategoria;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaDTO;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaDTO2;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaTipoComprobanteDTO;
import ar.com.signals.trading.configuracion.repository.PropiedadCategoriaDao;
import ar.com.signals.trading.configuracion.support.CategoriaException;
import ar.com.signals.trading.configuracion.support.NivelSet;
import ar.com.signals.trading.configuracion.support.ProductoCategoriaEnum;
import ar.com.signals.trading.configuracion.support.ProductoToxicidadEnum;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.configuracion.support.TipoDato;
import ar.com.signals.trading.configuracion.support.TipoSet;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.util.service.ServiceException;
import ar.com.signals.trading.util.support.LogicaNoImplementadaException;
import ar.com.signals.trading.util.web.ServidorUtil;

@Service
@Transactional
public class PropiedadCategoriaSrvImpl implements PropiedadCategoriaSrv {
	@Autowired private PropiedadCategoriaDao dao;
	@Resource private CategoriaSrv categoriaSrv;
	@Resource private ServidorUtil servidorUtil;
	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	
	public PropiedadCategoria obtener(Long id) {
		return dao.obtener(id);
	}
	
	public PropiedadCategoriaDTO obtenerDTO(Long id) {
		PropiedadCategoria entidad = dao.obtener(id);
		PropiedadCategoriaDTO entidadDTO = new PropiedadCategoriaDTO();
		entidadDTO.setId(id);
		entidadDTO.setCodigo(PropiedadCategoriaEnum.valueOf(entidad.getCategoria().getId()));
		switch (entidadDTO.getCodigo().getCategoriaSet().getNivelSet()) {
/*		case sujeto:
			entidadDTO.setsujetoId(entidad.getId());
			break;
		case sucursal:
			entidadDTO.setsucursalId(entidad.getId());
			break;*/
		default:
			break;
		}
		entidadDTO.setValor(entidad.getValor());
		entidadDTO.setActivo(entidad.isActivo());
		entidadDTO.setCreation_date(entidad.getCreation_date());
		entidadDTO.setLast_update_date(entidad.getLast_update_date());
		return entidadDTO;
	}
	
	@Override
	public void actualizar(PropiedadCategoria entidad) {
		dao.guardar(entidad);
	}
	
	@Override
	public List<PropiedadCategoriaDTO2> getPropiedadesDTO(boolean superUser) {
		List<Long> unidades = new ArrayList<>();
		return dao.getPropiedadesDTO(unidades, superUser);
	}
	
	@Override
	public boolean tieneCategoriaActiva(PropiedadCategoriaEnum propiedadCategoriaEnum) {
		if(!TipoSet.agrupador.equals(propiedadCategoriaEnum.getCategoriaSet().getTipoSet()) && !TipoSet.caracteristica.equals(propiedadCategoriaEnum.getCategoriaSet().getTipoSet())) {
			throw new CategoriaException("Esta queriendo verificar si la propiedad " + propiedadCategoriaEnum + " existe y esta activa" + 
					" pero esa categoria es de TipoSet " +  propiedadCategoriaEnum.getCategoriaSet().getTipoSet() + ", no de " + TipoSet.agrupador + " o " + TipoSet.caracteristica);
		}
		if(!NivelSet.global.equals(propiedadCategoriaEnum.getCategoriaSet().getNivelSet())) {
			throw new CategoriaException("Esta queriendo obtener una PropiedadCategoria de nivel " + propiedadCategoriaEnum.getCategoriaSet().getNivelSet() + 
					" pero la llamo a traves del metodo para obtener una de nivel " + NivelSet.global);
		}
		return dao.tieneCategoriaActiva(propiedadCategoriaEnum.name(), null, null);
	}
	
	@Override
	public PropiedadCategoria getPropiedad(PropiedadCategoriaEnum propiedadCategoriaEnum) {
		if(!NivelSet.global.equals(propiedadCategoriaEnum.getCategoriaSet().getNivelSet())) {
			throw new CategoriaException("Esta queriendo obtener una PropiedadCategoria de nivel " + propiedadCategoriaEnum.getCategoriaSet().getNivelSet() + 
					" pero la llamo a traves del metodo para obtener una de nivel " + NivelSet.global);
		}
		PropiedadCategoria propiedad = dao.obtener(propiedadCategoriaEnum.name(), null);
		if(propiedad == null){
			propiedad = new PropiedadCategoria();
			Categoria categoria = categoriaSrv.obtener(propiedadCategoriaEnum.name());
			if(categoria == null){
				categoria = categoriaSrv.crearCategoriaForPropiedad(propiedadCategoriaEnum);
			}
			propiedad.setCategoria(categoria);
			//propiedad.setOrganization_id(empresa.getId());
			propiedad.setCreation_date(new Timestamp(new Date().getTime()));
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue());
			propiedad.setActivo(true);
			propiedad.setLast_update_date(propiedad.getCreation_date());
			dao.guardar(propiedad);
			return propiedad;
		}else{//No se controla que el tipo de propiedad sea el correcto, ya que todas las propiedades se pueden devolver como String
			return propiedad;
		}
	}
	
	@Override
	public boolean getBoolean(PropiedadCategoriaEnum propiedadCategoriaEnum) {
		if(!NivelSet.global.equals(propiedadCategoriaEnum.getCategoriaSet().getNivelSet())) {
			throw new CategoriaException("Esta queriendo obtener una PropiedadCategoria de nivel " + propiedadCategoriaEnum.getCategoriaSet().getNivelSet() + 
					" pero la llamo a traves del metodo para obtener una de nivel " + NivelSet.global);
		}
		PropiedadCategoria propiedad = dao.obtener(propiedadCategoriaEnum.name(), null);
		if(propiedad == null){
			propiedad = new PropiedadCategoria();
			Categoria categoria = categoriaSrv.obtener(propiedadCategoriaEnum.name());
			if(categoria == null){
				categoria = categoriaSrv.crearCategoriaForPropiedad(propiedadCategoriaEnum);
			}
			propiedad.setCategoria(categoria);
			//propiedad.setAbstract_sujeto_id(sujeto.getId());
			propiedad.setCreation_date(new Timestamp(new Date().getTime()));
			//id manual
			//propiedad.setId(dao.getNextSequence(PropiedadCategoria.SEQUENCE_NAME).longValue());
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue());
			propiedad.setActivo(true);
			propiedad.setLast_update_date(propiedad.getCreation_date());
			dao.guardar(propiedad);
		}else{
			if(!TipoDato.Boolean.equals(propiedadCategoriaEnum.getTipoDato())){
				throw new CategoriaException("No se puede obtener Boolean de la propiedad id:'"+propiedad.getId()+"', categoria: "+propiedadCategoriaEnum+" debido a que es de tipo " + propiedadCategoriaEnum.getTipoDato());
			}
		}
		return new Boolean(propiedad.getValor());
	}
	
	public String getString(PropiedadCategoriaEnum propiedadCategoriaEnum) {
		if(!NivelSet.global.equals(propiedadCategoriaEnum.getCategoriaSet().getNivelSet())) {
			throw new CategoriaException("Esta queriendo obtener una PropiedadCategoria de nivel " + propiedadCategoriaEnum.getCategoriaSet().getNivelSet() + 
					" pero la llamo a traves del metodo para obtener una de nivel " + NivelSet.global);
		}
		PropiedadCategoria propiedad = dao.obtener(propiedadCategoriaEnum.name(), null);
		if(propiedad == null){
			propiedad = new PropiedadCategoria();
			Categoria categoria = categoriaSrv.obtener(propiedadCategoriaEnum.name());
			if(categoria == null){
				categoria = categoriaSrv.crearCategoriaForPropiedad(propiedadCategoriaEnum);
			}
			propiedad.setCategoria(categoria);
			//propiedad.setAbstract_sujeto_id(sujeto.getId());
			propiedad.setCreation_date(new Timestamp(new Date().getTime()));
			//id manual
			//propiedad.setId(dao.getNextSequence(PropiedadCategoria.SEQUENCE_NAME).longValue());
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue());
			propiedad.setActivo(true);
			propiedad.setLast_update_date(propiedad.getCreation_date());
			dao.guardar(propiedad);
			return propiedad.getValor();
		}else{//No se controla que el tipo de propiedad sea el correcto, ya que todas las propiedades se pueden devolver como String
			return propiedad.getValor();
		}
	}
	
	public Long getLong(PropiedadCategoriaEnum propiedadCategoriaEnum) throws CategoriaException{
		if(!NivelSet.global.equals(propiedadCategoriaEnum.getCategoriaSet().getNivelSet())) {
			throw new CategoriaException("Esta queriendo obtener una PropiedadCategoria de nivel " + propiedadCategoriaEnum.getCategoriaSet().getNivelSet() + 
					" pero la llamo a traves del metodo para obtener una de nivel " + NivelSet.global);
		}
		PropiedadCategoria propiedad = dao.obtener(propiedadCategoriaEnum.name(), null);
		if(propiedad == null){
			//if(ServidorUtil.Servidor.MSC.equals(servidorUtil.getServidor())) {
				propiedad = new PropiedadCategoria();
				Categoria categoria = categoriaSrv.obtener(propiedadCategoriaEnum.name());
				if(categoria == null){
					categoria = categoriaSrv.crearCategoriaForPropiedad(propiedadCategoriaEnum);
				}
				propiedad.setCategoria(categoria);
				propiedad.setCreation_date(new Timestamp(new Date().getTime()));
				//id manual
				//propiedad.setId(dao.getNextSequence(PropiedadCategoria.SEQUENCE_NAME).longValue());
				propiedad.setValor(propiedadCategoriaEnum.getDefaultValue());
				propiedad.setActivo(true);
				propiedad.setLast_update_date(propiedad.getCreation_date());
				dao.guardar(propiedad);
			
		}else{
			if(!TipoDato.Long.equals(propiedadCategoriaEnum.getTipoDato()) && !TipoDato.Integer.equals(propiedadCategoriaEnum.getTipoDato())){
				throw new CategoriaException("No se puede obtener Long de la propiedad id:'"+propiedad.getId()+"', categoria: "+propiedadCategoriaEnum+" debido a que es de tipo " + propiedadCategoriaEnum.getTipoDato());
			}
		}
		return new Long(propiedad.getValor());
	}
	
	@Override
	public String[] getArray(PropiedadCategoriaEnum propiedadCategoriaEnum) {
		if(!NivelSet.global.equals(propiedadCategoriaEnum.getCategoriaSet().getNivelSet())) {
			throw new CategoriaException("Esta queriendo obtener una PropiedadCategoria de nivel " + propiedadCategoriaEnum.getCategoriaSet().getNivelSet() + 
					" pero la llamo a traves del metodo para obtener una de nivel " + NivelSet.global);
		}
		PropiedadCategoria propiedad = dao.obtener(propiedadCategoriaEnum.name(), null);
		if(propiedad == null){
			//if(ServidorUtil.Servidor.MSC.equals(servidorUtil.getServidor())) {
				propiedad = new PropiedadCategoria();
				Categoria categoria = categoriaSrv.obtener(propiedadCategoriaEnum.name());
				if(categoria == null){
					categoria = categoriaSrv.crearCategoriaForPropiedad(propiedadCategoriaEnum);
				}
				propiedad.setCategoria(categoria);
				propiedad.setAbstract_sujeto_id(null);
				propiedad.setCreation_date(new Timestamp(new Date().getTime()));
				//id manual
				//propiedad.setId(dao.getNextSequence(PropiedadCategoria.SEQUENCE_NAME).longValue());
				propiedad.setValor(propiedadCategoriaEnum.getDefaultValue());
				propiedad.setActivo(true);
				propiedad.setLast_update_date(propiedad.getCreation_date());
				dao.guardar(propiedad);
				return this.splitPropiedad(propiedad, propiedadCategoriaEnum);

		}else{
			return this.splitPropiedad(propiedad, propiedadCategoriaEnum);
		}
	}
	
	private String[] splitPropiedad(PropiedadCategoria propiedad, PropiedadCategoriaEnum propiedadCategoriaEnum) {
		if(StringUtils.isBlank(propiedad.getValor())){
			return new String[]{};
		}
		if(TipoDato.Array.equals(propiedadCategoriaEnum.getTipoDato())){
			return propiedad.getValor().split(PROPIEDAD_ARRAY_SEPARATOR);
		}else if(TipoDato.Array_Contenedor.equals(propiedadCategoriaEnum.getTipoDato())){			
			return StringUtils.removeEnd(StringUtils.removeStart(propiedad.getValor(), PROPIEDAD_ARRAY_SEPARATOR), PROPIEDAD_ARRAY_SEPARATOR).split(PROPIEDAD_ARRAY_SEPARATOR);
		}
		return null;
	}

	public void validarPropiedad(TipoDato tipoDato, String valor, String dto_property, BindingResult errors) {
		if(tipoDato == null){//seria la propiedad boolean, tiene asignada la propiedad (y activa) o no la tiene
			if(StringUtils.isNotBlank(valor)){
				errors.rejectValue(dto_property, "001", "Debe dejar el valor en blanco");
			}
			return;
		}
		switch (tipoDato) {
/*		case BOOLEAN:
			if(!"true".equals(dto.getValor()) && !"false".equals(dto.getValor())){
				errors.rejectValue("valor", "001", "Debe ingresar valor 'true' (Verdadero) o 'false' (Falso)");
			}			
			break;*/
		case Integer:
			try {
				new Integer(valor);
			} catch (Exception e) {
				errors.rejectValue(dto_property, "001", "Debe ingresar valor Integer (Numero hasta 10 digitos). " + e.getMessage());
			}
			break;
		case Long:
			try {
				new Long(valor);
			} catch (Exception e) {
				errors.rejectValue(dto_property, "001", "Debe ingresar valor Long (Numero hasta 19 digitos). " + e.getMessage());
			}
			break;
		case Double:
			try {
				new Double(valor);
			} catch (Exception e) {
				errors.rejectValue(dto_property, "001", "Debe ingresar valor Decimal (Separador decimal '.'). " + e.getMessage());
			}
			break;
		case String:
			//No hay validacion, puede poner cualquiera cosa (empty y espacios en blanco se consideran null)
			break;
		case Array:
			//Puede estar vacio (empty y espacios en blanco se consideran null) o pueden ser valores separados por ;
			break;
		case Array_Contenedor:
			//Puede estar vacio (empty y espacios en blanco se consideran null) o pueden ser valores separados por ; pero debe empezar y terminar tambien con ;
			if(StringUtils.isNotBlank(valor)){
				if(valor.length() < 3){
					errors.rejectValue(dto_property, "001", "Debe dejar el valor vacio o debe ingresar mas de 2 caracteres");
				}else if(!valor.startsWith(PropiedadCategoriaSrv.PROPIEDAD_ARRAY_SEPARATOR) || !valor.endsWith(PropiedadCategoriaSrv.PROPIEDAD_ARRAY_SEPARATOR)){
					errors.rejectValue(dto_property, "001", "El valor debe empezar y terminar con " + PropiedadCategoriaSrv.PROPIEDAD_ARRAY_SEPARATOR);
				}
			}
			break;
		default:
			break;
		}
	}
	
	public boolean contiene(PropiedadCategoriaEnum propiedadCategoriaEnum, String search) {
		PropiedadCategoria propiedad = dao.obtener(propiedadCategoriaEnum.name(), null);
		if(propiedad == null){
			//if(ServidorUtil.Servidor.MSC.equals(servidorUtil.getServidor())) {
				propiedad = new PropiedadCategoria();
				Categoria categoria = categoriaSrv.obtener(propiedadCategoriaEnum.name());
				if(categoria == null){
					categoria = categoriaSrv.crearCategoriaForPropiedad(propiedadCategoriaEnum);
				}
				propiedad.setCategoria(categoria);
				propiedad.setCreation_date(new Timestamp(new Date().getTime()));
				//id manual
				//propiedad.setId(dao.getNextSequence(PropiedadCategoria.SEQUENCE_NAME).longValue());
				propiedad.setValor(propiedadCategoriaEnum.getDefaultValue());
				propiedad.setActivo(true);
				propiedad.setLast_update_date(propiedad.getCreation_date());
				dao.guardar(propiedad);
			
		}else{
			if(!TipoDato.Array_Contenedor.equals(propiedadCategoriaEnum.getTipoDato())){
				throw new CategoriaException("No se puede obtener Array_Contenedor de la propiedad id:'"+propiedad.getId()+"', categoria: "+propiedadCategoriaEnum+" debido a que es de tipo " + propiedadCategoriaEnum.getTipoDato());
			}
		}
		String valor = propiedad.getValor();
		if(StringUtils.isNotEmpty(valor)){
			if(!valor.startsWith(PROPIEDAD_ARRAY_SEPARATOR)){
				valor = PROPIEDAD_ARRAY_SEPARATOR + valor;
			}
			if(!valor.endsWith(PROPIEDAD_ARRAY_SEPARATOR)){
				valor = valor + PROPIEDAD_ARRAY_SEPARATOR;
			}
		}else{//propiedad en blanco, da false
			return false;
		}
		return StringUtils.contains(valor, PROPIEDAD_ARRAY_SEPARATOR + search + PROPIEDAD_ARRAY_SEPARATOR);
	}
/*	public boolean getBoolean(sucursal sucursal, PropiedadsucursalPrefijo propiedadsucursalPrefijo) {
		String id = propiedadsucursalPrefijo.name() + "_" + sucursal.getId();
		Propiedad propiedad = dao.obtener(id);
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(id);
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()"false");//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_sucursal);//Valor por defecto
			propiedad.setTipoDato(TipoDato.BOOLEAN);
			propiedad.setsucursal(sucursal);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto
			if(!TipoDato.BOOLEAN.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener Boolean de la propiedad id:'"+id+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}
		return new Boolean(propiedad.getValor());
	}
	
	public boolean getBoolean(PropiedadGlobalID propiedadGlobalID) {
		Propiedad propiedad = dao.obtener(propiedadGlobalID.name());
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(propiedadGlobalID.name());
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()"false");//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_GENERAL);//Valor por defecto
			propiedad.setTipoDato(TipoDato.BOOLEAN);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto
			if(!TipoDato.BOOLEAN.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener Boolean de la propiedad id:'"+propiedadGlobalID.name()+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}
		return new Boolean(propiedad.getValor());
	}*/

	@Override
	public PropiedadCategoriaTipoComprobanteDTO obtenerPropiedadCategoriaTipoComprobanteDTO(Long id) {
		PropiedadCategoria entidad = dao.obtener(id);
		PropiedadCategoriaTipoComprobanteDTO dto = new PropiedadCategoriaTipoComprobanteDTO();
		dto.setId(entidad.getId());
		dto.setTipoComprobante_id(new Integer(entidad.getValor()));
		dto.setCodigo(PropiedadCategoriaEnum.valueOf(entidad.getCategoria().getId()));
		return dto;
	}

	@Override
	public void validateAndPersist(PropiedadCategoriaDTO entidadDTO, BindingResult errors, Usuario usuario) {
		switch (entidadDTO.getCodigo().getCategoriaSet().getNivelSet()) {
		case global:
			if(entidadDTO.getSujeto_id() != null || entidadDTO.getSucursal_id() != null || entidadDTO.getUnidadProductiva_id() != null){
				errors.rejectValue(entidadDTO.getSujeto_id() != null?"sujeto_id":(entidadDTO.getSucursal_id() != null?"sucursal_id":"unidadProductiva_id"), "", "Para esa Categoria no debe seleccionar ni Sujeto ni Sucursal ni Unidad Productiva)");
			}
			break;
		case sujeto:
			throw new LogicaNoImplementadaException("No se implemento logica para ABM PropiedadCategoria Nivel.sujeto");
		case sucursal:
			throw new LogicaNoImplementadaException("No se implemento logica para ABM PropiedadCategoria Nivel.sucursal");
		case unidadProductiva:
			throw new LogicaNoImplementadaException("No se implemento logica para ABM PropiedadCategoria Nivel.unidadProductiva");
		default:
			break;
		}

		if(errors.hasErrors()){
			return;
		}
		if(dao.existe(entidadDTO.getCodigo().name(), entidadDTO.getSujeto_id(), entidadDTO.getSucursal_id(), entidadDTO.getUnidadProductiva_id(), entidadDTO.getId())){
			errors.rejectValue("codigo", "", "Ya existe Categoria "+entidadDTO.getCodigo().name()+" tipo Global/Suejto/Sucursal/UnidadProductiva");
		}
		propiedadCategoriaSrv.validarPropiedad(entidadDTO.getCodigo().getTipoDato(), entidadDTO.getValor(), "valor", errors);
		if(errors.hasErrors()){
			return;
		}
		
		//Persistir
		PropiedadCategoria entidad = null;
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		if(entidadDTO.getId() != null){//Modificacion
			entidad = dao.obtener(entidadDTO.getId());
		}else{//Nuevo
			 entidad = new PropiedadCategoria();
			 Categoria categoria = categoriaSrv.obtener(entidadDTO.getCodigo().name());
			 if(categoria == null){
				 categoria = categoriaSrv.crearCategoriaForPropiedad(entidadDTO.getCodigo());
			 }
			 entidad.setCategoria(categoria);
			 entidad.setAbstract_sujeto_id(entidadDTO.getSujeto_id() != null?entidadDTO.getSujeto_id():(entidadDTO.getSucursal_id()!=null?entidadDTO.getSucursal_id():entidadDTO.getUnidadProductiva_id()));
			 entidad.setCreation_date(fechaActual);
			 //id manual
			 //entidad.setId(dao.getNextSequence(PropiedadCategoria.SEQUENCE_NAME).longValue());
		}
		entidad.setValor(StringUtils.isBlank(entidadDTO.getValor())?null:entidadDTO.getValor().trim());
		entidad.setActivo(entidadDTO.isActivo());
		entidad.setLast_update_date(fechaActual);
		if(entidadDTO.getId() == null) {
			dao.guardar(entidad);
			entidadDTO.setId(entidad.getId());
		}
	}

/*	public int getInteger(PropiedadGlobalID propiedadGlobalID) {
		Propiedad propiedad = dao.obtener(propiedadGlobalID.name());
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(propiedadGlobalID.name());
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()"0");//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_GENERAL);//Valor por defecto
			propiedad.setTipoDato(TipoDato.INTEGER);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto
			if(!TipoDato.INTEGER.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener Integer de la propiedad id:'"+propiedadGlobalID.name()+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}
		return new Integer(propiedad.getValor());
	}
	
	public long getLong(PropiedadGlobalID propiedadGlobalID) {
		Propiedad propiedad = dao.obtener(propiedadGlobalID.name());
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(propiedadGlobalID.name());
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()"0");//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_GENERAL);//Valor por defecto
			propiedad.setTipoDato(TipoDato.LONG);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto (Un tipo integer se puede devolver como long)
			if(!TipoDato.LONG.equals(propiedad.getTipoDato()) && !TipoDato.INTEGER.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener Long de la propiedad id:'"+propiedadGlobalID.name()+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}
		return new Long(propiedad.getValor());
	}
	
	public long getLong(sucursal sucursal, PropiedadsucursalPrefijo propiedadsucursalPrefijo) {
		String id = propiedadsucursalPrefijo.name() + "_" + sucursal.getId();
		Propiedad propiedad = dao.obtener(id);
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(id);
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()"0");//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_sucursal);//Valor por defecto
			propiedad.setTipoDato(TipoDato.LONG);
			propiedad.setsucursal(sucursal);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto
			if(!TipoDato.LONG.equals(propiedad.getTipoDato()) && !TipoDato.INTEGER.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener Long de la propiedad id:'"+id+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}
		return new Long(propiedad.getValor());
	}
	
	public String[] getArray(PropiedadGlobalID propiedadGlobalID) {
		Propiedad propiedad = dao.obtener(propiedadGlobalID.name());
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(propiedadGlobalID.name());
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()null);//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_GENERAL);//Valor por defecto
			propiedad.setTipoDato(TipoDato.ARRAY);
			dao.guardar(propiedad);
		}
		return this.splitPropiedad(propiedad);
	}
	
	public String[] getArray(PropiedadProductoPrefijo propiedadProductoPrefijo, String codigoProducto) {
		String id = propiedadProductoPrefijo.name() + "_" + codigoProducto;
		Propiedad propiedad = dao.obtener(id);
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(id);
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()null);//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_PRODUCTO);//Valor por defecto
			propiedad.setTipoDato(TipoDato.ARRAY);
			dao.guardar(propiedad);
		}
		return this.splitPropiedad(propiedad);
	}



	public boolean contiene(PropiedadGlobalID propiedadGlobalID, String search) {
		Propiedad propiedad = dao.obtener(propiedadGlobalID.name());
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(propiedadGlobalID.name());
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()null);//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_GENERAL);//Valor por defecto
			propiedad.setTipoDato(TipoDato.ARRAY_CONTENEDOR);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto
			if(!TipoDato.ARRAY_CONTENEDOR.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener ARRAY_CONTENEDOR de la propiedad id:'"+propiedadGlobalID.name()+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}
		String valor = propiedad.getValor();
		if(StringUtils.isNotEmpty(valor)){
			if(!valor.startsWith(PROPIEDAD_ARRAY_SEPARATOR)){
				valor = PROPIEDAD_ARRAY_SEPARATOR + valor;
			}
			if(!valor.endsWith(PROPIEDAD_ARRAY_SEPARATOR)){
				valor = valor + PROPIEDAD_ARRAY_SEPARATOR;
			}
		}else{//propiedad en blanco, da false
			return false;
		}
		return StringUtils.contains(valor, PROPIEDAD_ARRAY_SEPARATOR + search + PROPIEDAD_ARRAY_SEPARATOR);
	}

	public String getString(PropiedadProductoPrefijo propiedadProductoPrefijo, String codigoProducto) {
		String id = propiedadProductoPrefijo.name() + "_" + codigoProducto;
		Propiedad propiedad = dao.obtener(id);
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(id);
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()null);//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_PRODUCTO);//Valor por defecto
			propiedad.setTipoDato(TipoDato.STRING);
			dao.guardar(propiedad);
		}
		return propiedad.getValor();
	}

	public String getString(sucursal sucursal, PropiedadsucursalPrefijo propiedadsucursalPrefijo) {
		String id = propiedadsucursalPrefijo.name() + "_" + sucursal.getId();
		Propiedad propiedad = dao.obtener(id);
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(id);
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()null);//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_sucursal);//Valor por defecto
			propiedad.setTipoDato(TipoDato.STRING);
			propiedad.setsucursal(sucursal);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto
			if(!TipoDato.STRING.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener String de la propiedad id:'"+id+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}
		return propiedad.getValor();
	}

	public int getInteger(sucursal sucursal, PropiedadsucursalPrefijo propiedadsucursalPrefijo) {
		String id = propiedadsucursalPrefijo.name() + "_" + sucursal.getId();
		Propiedad propiedad = dao.obtener(id);
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(id);
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()"0");//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_sucursal);//Valor por defecto
			propiedad.setTipoDato(TipoDato.INTEGER);
			propiedad.setsucursal(sucursal);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto
			if(!TipoDato.INTEGER.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener Integer de la propiedad id:'"+id+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}
		return new Integer(propiedad.getValor());
	}

	public Integer getInteger(PropiedadContratoPrefijo propiedadContratoPrefijo, Integer nrosujeto, Integer nroContrato) {
		String id = propiedadContratoPrefijo.name() + "_" + nrosujeto + "_" + nroContrato;
		Propiedad propiedad = dao.obtener(id);
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(id);
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()null);//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_CONTRATO);//Valor por defecto
			propiedad.setTipoDato(TipoDato.INTEGER);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto
			if(!TipoDato.INTEGER.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener Integer de la propiedad id:'"+id+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}	
		return propiedad.getValor()!=null?new Integer(propiedad.getValor()):null;
	}

	@Override
	public Integer getInteger(PropiedadContratoProductoPrefijo propiedadContratoProductoPrefijo, Integer nrosujeto, Integer nroContrato, String codigoProducto) {
		String id = propiedadContratoProductoPrefijo.name() + "_" + nrosujeto + "_" + nroContrato + "_" + codigoProducto;
		Propiedad propiedad = dao.obtener(id);
		if(propiedad == null){
			propiedad = new Propiedad();
			propiedad.setId(id);
			propiedad.setValor(propiedadCategoriaEnum.getDefaultValue()null);//Valor por defecto si no existe la propiedad
			propiedad.setCategoriaPropiedad(CategoriaPropiedad.CONFIGURACION_CONTRATO_PRODUCTO);//Valor por defecto
			propiedad.setTipoDato(TipoDato.INTEGER);
			dao.guardar(propiedad);
		}else{//Controlo que la propiedad sea del tipo correcto
			if(!TipoDato.INTEGER.equals(propiedad.getTipoDato())){
				throw new RuntimeException("No se puede obtener Integer de la propiedad id:'"+id+"' debido a que es de tipo " + propiedad.getTipoDato());
			}
		}	
		return propiedad.getValor()!=null?new Integer(propiedad.getValor()):null;
	}*/
}