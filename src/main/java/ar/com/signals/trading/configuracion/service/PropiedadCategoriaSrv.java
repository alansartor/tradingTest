package ar.com.signals.trading.configuracion.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import ar.com.signals.trading.configuracion.domain.PropiedadCategoria;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaDTO;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaDTO2;
import ar.com.signals.trading.configuracion.dto.PropiedadCategoriaTipoComprobanteDTO;
import ar.com.signals.trading.configuracion.support.CategoriaException;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.configuracion.support.TipoDato;
import ar.com.signals.trading.seguridad.domain.Usuario;

public interface PropiedadCategoriaSrv {
	public static final String PROPIEDAD_ARRAY_SEPARATOR = ";";//no estoy usando esto por ahora


	PropiedadCategoria obtener(Long id);
	
	PropiedadCategoriaDTO obtenerDTO(Long id);
	
	List<PropiedadCategoriaDTO2> getPropiedadesDTO(boolean superUser);
	
	/**
	 * Verificar que si la propiedad de planta existe y si esta activa, eso seria un true
	 * En caso que no exista, o este inactiva, eso seria un false.
	 * @param planta
	 * @param propiedadCategoriaEnum
	 * @return
	 */
	public boolean tieneCategoriaActiva(PropiedadCategoriaEnum propiedadCategoriaEnum);
	
	/**
	 * Si la propiedad que se busca no existe, se devuelve null (si se esta llamando en el MSC, se crea la propiedad con valor null)
	 * No importa el tipo de dato de la propiedad, se devuelve como String
	 * @param caladoLaboratorioAsyncronico
	 * @return
	 */
	public String getString(PropiedadCategoriaEnum propiedadCategoriaEnum);
	
	
	PropiedadCategoria getPropiedad(PropiedadCategoriaEnum propiedadCategoriaEnum);
	
	void actualizar(PropiedadCategoria entidad);
	
	/**
	 * Si la propiedad que se busca no existe, se devuelve null salvo que se llame desde el MSC, donde se crea la propiedad con valor 0
	 * Si la propiedad no es del tipo Long ni Integer, entonces se arroja una excepcion CategoriaException
	 * @param propiedadCategoriaEnum
	 * @return
	 */
	Long getLong(PropiedadCategoriaEnum propiedadCategoriaEnum) throws CategoriaException;
	
	public String[] getArray(PropiedadCategoriaEnum propiedadCategoriaEnum);
	
	public boolean contiene(PropiedadCategoriaEnum propiedadCategoriaEnum, String search);
	
	public void validarPropiedad(TipoDato tipoDato, String valor, String dto_property, BindingResult errors);

	PropiedadCategoriaTipoComprobanteDTO obtenerPropiedadCategoriaTipoComprobanteDTO(Long id);

	void validateAndPersist(PropiedadCategoriaDTO entidadDTO, BindingResult errors, Usuario usuario);

	boolean getBoolean(PropiedadCategoriaEnum propiedadCategoriaEnum);
	
	/**
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto false.
	 * Si la propiedad no es del tipo Boolean, entonces se arroja una excepcion RuntimeException
	 * @param planta
	 * @param PropiedadPlantaPrefijo
	 * @return
	 */
	//boolean getBoolean(Planta planta, PropiedadPlantaPrefijo propiedadPlantaPrefijo);
	
	/**
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto false.
	 * Si la propiedad no es del tipo Boolean, entonces se arroja una excepcion RuntimeException
	 * @param caladoLaboratorioAsyncronico
	 * @return
	 */
	//boolean getBoolean(PropiedadGlobalID propiedadGlobalID);
	
	/**
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto 0.
	 * Si la propiedad no es del tipo Integer, entonces se arroja una excepcion RuntimeException
	 * @param caladoLaboratorioAsyncronico
	 * @return
	 */
	//int getInteger(PropiedadGlobalID propiedadGlobalID);
	
	/**
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto 0
	 * @param planta
	 * @param capacidadDescargaTerminal
	 */
	//int getInteger(Planta planta, PropiedadPlantaPrefijo propiedadPlantaPrefijo);
	
	//long getLong(PropiedadGlobalID propiedadGlobalID);
	
	/**
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto 0
	 * @param planta
	 * @param capacidadDescargaTerminal
	 */
	//long getLong(Planta planta, PropiedadPlantaPrefijo propiedadPlantaPrefijo);
	
	/**
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto null.
	 * @param caladoLaboratorioAsyncronico
	 * @return
	 */
	//public String[] getArray(PropiedadGlobalID propiedadGlobalID);

	//boolean contiene(PropiedadGlobalID propiedadGlobalID, String search);

	/**
	 * Devuelve la propiedad con id: PropiedadProductoPrefijo + "_" + codigoProducto
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto null
	 * @param mascara
	 * @param productoCod
	 * @return
	 */
	//String getString(PropiedadProductoPrefijo propiedadProductoPrefijo, String codigoProducto);
	
	/**
	 * Devuelve la propiedad con id: PropiedadProductoPrefijo + "_" + codigoProducto
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto null
	 * @param mascara
	 * @param productoCod
	 * @return
	 */
	//String[] getArray(PropiedadProductoPrefijo propiedadProductoPrefijo, String codigoProducto);
	
	/**
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto null
	 * @param planta
	 * @param propiedadPlantaPrefijo
	 * @return
	 */
	//String getString(Planta planta, PropiedadPlantaPrefijo propiedadPlantaPrefijo);
	
	/**
	 * Devuelve la propiedad con id: PropiedadContratoPrefijo + "_" + nroEmpresa + "_" + nroContrato 
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto null
	 * @param propiedadContratoPrefijo
	 * @param nroEmpresa
	 * @param nroContrato
	 * @return
	 */
	//Integer getInteger(PropiedadContratoPrefijo propiedadContratoPrefijo, Integer nroEmpresa, Integer nroContrato);

	/**
	 * Devuelve la propiedad con id: PropiedadContratoPrefijo + "_" + nroEmpresa + "_" + nroContrato + "_" + codigoProducto
	 * Si la propiedad que se busca no existe, entonces se crea con valor por defecto null
	 * @param propiedadContratoProductoPrefijo
	 * @param nroEmpresa
	 * @param nroContrato
	 * @param codigoProducto
	 * @return
	 */
	//Integer getInteger(PropiedadContratoProductoPrefijo propiedadContratoProductoPrefijo, Integer nroEmpresa, Integer nroContrato, String codigoProducto);
}
