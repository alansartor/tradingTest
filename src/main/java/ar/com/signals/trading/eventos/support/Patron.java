package ar.com.signals.trading.eventos.support;

/**
 * Ordinal, mantener orden de las opciones
 * 
 * @author pepe@hotmail.com
 *
 */
public enum Patron {
	Pmg_en_rango("PMG, XXL, RVR y VRV en ZI estable (dos toques y con al menos 1 hora de antiguedad, en ..50 o ..00)"),//asi lo armamos originalmente
	Pmg("PMG y XXL en ZI ..00 (no se tiene en cuenta antiguedad ni toques)");//asi lo quiere el ema para poder promocionar el bot, asi en todas las operaciones que toma esta la alerta (ya que con la otra forma, hay varias operaciones que toma el y que el bot no alerta)
	
	private String descripcion;
	
	private Patron(String descripcion){
		this.descripcion = descripcion;
	}
	
	 public String getDescripcion() {
		return descripcion;
	}

}
