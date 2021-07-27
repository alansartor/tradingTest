package ar.com.signals.trading.util.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;


public class ControllerUtil {

	public static String getTileName(Modulo modulo, Accion accion) {
		return getTileName(modulo, "", "", accion);
	}

	public static String getTileName(Modulo modulo, Class<?> clase, Accion accion) {
		return getTileName(modulo, clase, null, accion);
	}
	
	public static String getTileName(Modulo modulo, String clase, Accion accion) {
		return getTileName(modulo, clase, null, accion);
	}
	
	public static String getTileName(Modulo modulo, Class<?> clase, String subClase, Accion accion) {
		return modulo.name() + "." + (clase!=null?clase.getSimpleName() + ".":"") + (StringUtils.isNotBlank(subClase)?subClase + ".":"") + accion.name();
	}
	
	public static String getTileName(Modulo modulo, String clase, String subClase, Accion accion) {
		return modulo.name() + "." + (StringUtils.isNotBlank(clase)?clase + ".":"") + (StringUtils.isNotBlank(subClase)?subClase + ".":"") + accion.name();
	}
	
	public static String getTileRequestGET(Modulo modulo, Class<?> clase, String subClase, Accion accion) {
		return "/web/" + modulo.name() + "/" + (clase!=null?clase.getSimpleName() + "/":"")+ (StringUtils.isNotBlank(subClase)?subClase + "/":"") + accion.name();
	}
	
	public static String getTileRequestGET2(Modulo modulo, String clase, String subClase, Accion accion) {
		return "/web/" + modulo.name() + "/" + (StringUtils.isNotBlank(clase)?clase + "/":"")+ (StringUtils.isNotBlank(subClase)?subClase + "/":"") + accion.name();
	}
	
	public static String redirectGET(Modulo modulo, Accion accion) {
		return redirectGET(modulo, (Class<?>) null, accion);
	}
	
	public static String redirectGET(Modulo modulo, Accion accion, String requestParams) {
		return "redirect:" + getTileRequestGET(modulo, null, null, accion) + (StringUtils.isNotEmpty(requestParams)?"?"+requestParams:"");
	}
	
	public static String redirectGET(Modulo modulo, String clase, Accion accion) {
		return "redirect:" + getTileRequestGET2(modulo, clase, null, accion);
	}
	
	public static String redirectGET(Modulo modulo, Class<?> clase, Accion accion) {
		return "redirect:" + getTileRequestGET(modulo, clase, null, accion);
	}
	
	public static String redirectGET(Modulo modulo, Class<?> clase, Accion accion, String requestParams) {
		return "redirect:" + getTileRequestGET(modulo, clase, null, accion) + (StringUtils.isNotEmpty(requestParams)?"?"+requestParams:"");
	}
	
	public static String redirectGET(Modulo modulo, Class<?> clase, String subClase, Accion accion) {
		return "redirect:" + getTileRequestGET(modulo, clase, subClase, accion);
	}
	
	public static String redirectGET(Modulo modulo, Class<?> clase, String subClase, Accion accion, String requestParams) {
		return "redirect:" + getTileRequestGET(modulo, clase, subClase, accion) + (StringUtils.isNotEmpty(requestParams)?"?"+requestParams:"");
	}
	
	/**
	 * forward solo se puede usar en RequestMethod.GET
	 * Cuidado: al hacer un forward, la url queda como estaba antes, lo que hace que al hacer post en esa 
	 * pagina que se forwardio, si no se define un action en el form, entonces se va a hacer un post a la url incorrecta
	 * @param modulo
	 * @param accion
	 * @return
	 */
	public static String forwardGET(Modulo modulo, Accion accion) {
		return forwardGET(modulo, null, accion);
	}
	
	/**
	 * forward solo se puede usar en RequestMethod.GET
	 * Cuidado: al hacer un forward, la url queda como estaba antes, lo que hace que al hacer post en esa 
	 * pagina que se forwardio, si no se define un action en el form, entonces se va a hacer un post a la url incorrecta
	 * @param modulo
	 * @param accion
	 * @return
	 */
	public static String forwardGET(Modulo modulo, Accion accion, String requestParams) {
		return forwardGET(modulo, null, accion) + (StringUtils.isNotEmpty(requestParams)?"?"+requestParams:"");
	}
	
	/**
	 * forward solo se puede usar en RequestMethod.GET
	 * Cuidado: al hacer un forward, la url queda como estaba antes, lo que hace que al hacer post en esa 
	 * pagina que se forwardio, si no se define un action en el form, entonces se va a hacer un post a la url incorrecta
	 * @param modulo
	 * @param accion
	 * @return
	 */
	public static String forwardGET(Modulo modulo, Class<?> clase, Accion accion, String requestParams) {
		return forwardGET(modulo, clase, accion) + (StringUtils.isNotEmpty(requestParams)?"?"+requestParams:"");
	}
	
	/**
	 * forward solo se puede usar en RequestMethod.GET
	 * @param modulo
	 * @param clase
	 * @param accion
	 * @return
	 */
	public static String forwardGET(Modulo modulo, Class<?> clase, Accion accion) {
		return "forward:" + getTileRequestGET(modulo, clase, null, accion);
	}
	
	/**
	 * forward solo se puede usar en RequestMethod.GET
	 * @param modulo
	 * @param clase
	 * @param accion
	 * @return
	 */
	public static String forwardGET2(Modulo modulo, String clase, Accion accion) {
		return "forward:" + getTileRequestGET2(modulo, clase, null, accion);
	}
	
	/**
	 * forward solo se puede usar en RequestMethod.GET
	 * @param modulo
	 * @param clase
	 * @param subClase
	 * @param accion
	 * @return
	 */
	public static String forwardGET(Modulo modulo, Class<?> clase, String subClase, Accion accion) {
		return "forward:" + getTileRequestGET(modulo, clase, subClase, accion);
	}

	public static List<Breadcrumb> getBreadcrumb(Modulo modulo, Class<?> clase, Accion accion) {
		return getBreadcrumb(modulo, clase, accion, null);
	}
	
	public static List<Breadcrumb> getBreadcrumb(Modulo modulo, String clase, Accion accion) {
		return getBreadcrumb(modulo, clase, null, accion, null);
	}

	public static List<Breadcrumb> getBreadcrumb(Modulo modulo, Class<?> clase, Accion accion, Object idEntidad) {
		return getBreadcrumb(modulo, clase!=null?clase.getSimpleName():"", null, accion, idEntidad);
	}
	
	public static List<Breadcrumb> getBreadcrumb(Modulo modulo, String clase, Accion accion, Object idEntidad) {
		return getBreadcrumb(modulo, clase, null, accion, idEntidad);
	}
	
	public static List<Breadcrumb> getBreadcrumb(Modulo modulo, Class<?> clase, String subClase, Accion accion) {
		return getBreadcrumb(modulo, clase!=null?clase.getSimpleName():"", subClase, accion, null);
	}
	
	public static List<Breadcrumb> getBreadcrumb(Modulo modulo, String clase, String subClase, Accion accion) {
		return getBreadcrumb(modulo, clase, subClase, accion, null);
	}
	public static List<Breadcrumb> getBreadcrumb(Modulo modulo, Class<?> clase, String subClase, Accion accion, Object idEntidad) {
		return getBreadcrumb(modulo, clase!=null?clase.getSimpleName():"", subClase, accion, idEntidad);
	}
	
	public static List<Breadcrumb> getBreadcrumb(Modulo modulo, String clase, String subClase, Accion accion, Object idEntidad) {
		List<Breadcrumb> lista = new ArrayList<Breadcrumb>();
		lista.add(new Breadcrumb(Modulo.GENERAL, Accion.HOME));
		switch (accion) {
		case NUEVO:
		case EDITAR:
		case VER:
			lista.add(new Breadcrumb(modulo, clase, Accion.LISTAR));//saco la subclase en el bread, lista.add(new Breadcrumb(modulo, clase, subClase, Accion.LISTAR));
			break;
		case PASO_DOS:
			lista.add(new Breadcrumb(modulo, clase, Accion.PASO_UNO));
			break;
		case PASO_TRES:
			lista.add(new Breadcrumb(modulo, clase, Accion.PASO_UNO));
			lista.add(new Breadcrumb(modulo, clase, Accion.PASO_DOS));
			break;
		default:
			break;
		}
		lista.add(new Breadcrumb(modulo, clase, subClase, accion, true, idEntidad));
		return lista;
	}
}
