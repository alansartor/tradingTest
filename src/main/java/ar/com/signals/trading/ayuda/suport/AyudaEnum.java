package ar.com.signals.trading.ayuda.suport;

import java.util.HashMap;
import java.util.Map;

import ar.com.signals.trading.ayuda.dto.MenuOption;
import ar.com.signals.trading.general.support.Accion;
import ar.com.signals.trading.general.support.Modulo;

/**
 * Se utiliza para las paginas de ayuda
 * En la jsp se hace referencia a un enum
 * Y luego con ese enum se llama a AyudaController para resolver la pantalla de ayuda
 * 
 * @author pepe@hotmail.com
 *
 */
public enum AyudaEnum {
	//************ IMPORTANTE: EL ORDEN ES IMPORTANTE! SINO ARROJA ERROR EL ARMADO DEL NAVEGADOR DE AYUDA! SIEMPRE DEL MAS GENERAL AL MAS ESPECIFICO!
	GASTOGLOBAL_NUEVO_COSTODESCONTADO(Modulo.ESTABLECIMIENTO, "GastoGlobal", Accion.NUEVO, "costoDescontado"),
	GASTOGLOBAL_LISTAR_COMPROBANTE_ID(Modulo.ESTABLECIMIENTO, "GastoGlobal", Accion.LISTAR, "fltComprobanteId"),
	MOVIMIENTO_NUEVO_TIPOMOVIMIENTO(Modulo.MOVIMIENTOS, "Movimiento", Accion.NUEVO, "tipoMovimiento"),
	MOVIMIENTO_NUEVO_TIPOORIGEN(Modulo.MOVIMIENTOS, "Movimiento", Accion.NUEVO, "tipoOrigen"),
	MOVIMIENTO_NUEVO_NROCOMPROBANTE(Modulo.MOVIMIENTOS, "Movimiento", Accion.NUEVO, "nroComprobante"),
	MOVIMIENTO_NUEVO_ULTIMOMOVIMIENTOPARCIAL(Modulo.MOVIMIENTOS, "Movimiento", Accion.NUEVO, "ultimoMovimientoParcial"),
	MOVIMIENTO_NUEVO_NETO(Modulo.MOVIMIENTOS, "Movimiento", Accion.NUEVO, "neto"),
	REPORTE_FLUJOCAJA_LISTAR(Modulo.CONTABILIDAD, "FlujoCaja", Accion.LISTAR, null),
	COMPROBANTE_COMPRA_LISTAR(Modulo.CONTABILIDAD, "ComprobanteCompra", Accion.LISTAR, null),
	COMPROBANTE_COMPRA_LISTAR_PRODUCTO_ID(Modulo.CONTABILIDAD, "ComprobanteCompra", Accion.LISTAR, "fltProductoId"),
	COMPROBANTE_COMPRA_NUEVO_CONDICIONPAGO_ID(Modulo.CONTABILIDAD, "ComprobanteCompra", Accion.NUEVO, "condicionPago_id"),
	COMPROBANTE_COMPRA_NUEVO_OBSERVACIONES(Modulo.CONTABILIDAD, "ComprobanteCompra", Accion.NUEVO, "observaciones"),
	LABOR_LISTAR(Modulo.PRODUCCION, "Labor", Accion.LISTAR, null),
	LABOR_LISTAR_COMPROBANTE_ID(Modulo.PRODUCCION, "Labor", Accion.LISTAR, "fltComprobanteId"),
	LABOR_NUEVO_PRODUCTO_LABOR_ID(Modulo.PRODUCCION, "Labor", Accion.NUEVO, "producto_labor_id"),
	PRODUCCION_NUEVO_PRODUCIDO(Modulo.PRODUCCION, "Produccion", Accion.NUEVO, "producido");
	
	private final Modulo modulo;
	private final String clase;
	private String subClase;
	private final Accion accion;
	private final String field;
	
	private AyudaEnum(Modulo modulo, String clase, String subClase, Accion accion, String field) {
		this.modulo = modulo;
		this.clase = clase;
		this.subClase = subClase;
		this.accion = accion;
		this.field = field;
	}
	
	private AyudaEnum(Modulo modulo, String clase, Accion accion, String field) {
		this(modulo, clase, null, accion, field);
	}
	
	public Modulo getModulo() {
		return modulo;
	}

	public String getClase() {
		return clase;
	}

	public Accion getAccion() {
		return accion;
	}
	
	public int getOrdinal() {
		return ordinal();
	}

	public String getSubClase() {
		return subClase;
	}

	public String getField() {
		return field;
	}

	public static Map<Modulo, MenuOption> obtenerAyudasDTO() {
		//MenuOption menuAccion = null;
		MenuOption field = null;
		//*** ARMADO DE MENU ***
		Map<Modulo, MenuOption> ayudas = new HashMap<Modulo, MenuOption>();
		for (AyudaEnum a : values()) {
			//*** MENU ***
			MenuOption menu = ayudas.get(a.getModulo());
			if(menu == null){//Creo el menu si no existia
				if(a.getClase() != null) {
					menu = new MenuOption(a.getModulo().name());
					ayudas.put(a.getModulo(), menu);
				}else {//Si es una ayuda de modulo, entonces dejo el Enum en el MenuOption
					menu = new MenuOption(a.getModulo().name(), a);
					ayudas.put(a.getModulo(), menu);
					menu.setAyuda(a);
					continue;//vuelvo a seguir iterando ya que no tiene accion esta ayudaEnumm
				}
			}
			//*** SUBMENU ***
			MenuOption submenu = menu.getSubmenu().get(a.getClase());
			if(submenu == null){//Creo el menu si no existia
				if(a.getAccion() != null) {
					submenu = new MenuOption(a.getClase());
					menu.getSubmenu().put(a.getClase(), submenu);
				}else {//Si es una ayuda de clase, entonces dejo el Enum en el MenuOption
					submenu = new MenuOption(a.getClase(), a);
					menu.getSubmenu().put(a.getClase(), submenu);
					continue;//vuelvo a seguir iterando ya que no tiene accion esta ayudaEnumm
				}
			}
			//*** ACCION ***
			MenuOption subSubMenu = submenu.getSubmenu().get(a.getAccion().name());
			if(subSubMenu == null){//Creo el menu si no existia
				if(a.getField() != null) {
					subSubMenu = new MenuOption(a.getAccion().name());
					submenu.getSubmenu().put(a.getAccion().name(), subSubMenu);
				}else {//Si es una ayuda de accion, entonces dejo el Enum en el MenuOption
					subSubMenu = new MenuOption(a.getAccion().name(), a);
					submenu.getSubmenu().put(a.getAccion().name(), subSubMenu);
					continue;//vuelvo a seguir iterando ya que no tiene field esta ayudaEnumm
				}
			}			
			if(a.getField() != null) {
				//*** FIELD ***
				field = new MenuOption(a.name(), a);
				subSubMenu.getSubmenu().put(a.name(), field);
			}
	
		}
		return ayudas;
	}
	
}
