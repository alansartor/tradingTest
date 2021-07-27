package ar.com.signals.trading.util.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ar.com.signals.trading.trading.support.TrueFxPreciosData;

public class ChartCandlePointDTO {
	private Date t;// date.valueOf(),
	private BigDecimal o;//: open,
	private BigDecimal h;//: high,
	private BigDecimal l;//: low,
	private BigDecimal c;//: close
	
	private final List<TrueFxPreciosData> precios = new ArrayList<TrueFxPreciosData>();
	
	private boolean notificada;//una vela notificada no es necesario volver a notificarla
	private String observacion;//es lo que se esta notificando
	private boolean NotificadaPorPantalla;//Forma rapida de mostrar una sola vez por pantalla. Si hay dos view viendo la misma divisa, solo uno va a ver esta notificacion 
	
	/**
	 * Este valor solo esta disponible si anteriormente se llamo a getMaxZI() o getMinZI()
	 */
	private BigDecimal candleZi = null;
	
	private ChartCandlePointDTO candleZiClose;//si una vela anterior que toca una zi, se usa cuando hay un falso rompimiento, entonces necesito esta vela que paso por la zi, ya que la considero un pico
	
	public ChartCandlePointDTO(Date t, BigDecimal o, BigDecimal h, BigDecimal l, BigDecimal c) {
		super();
		this.t = t;
		this.o = o;
		this.h = h;
		this.l = l;
		this.c = c;
	}
	
	public ChartCandlePointDTO(Date minuto, BigDecimal closeAnterior, TrueFxPreciosData data) {
		t = minuto;
		c = data.getBid().add(data.getAsk()).divide(new BigDecimal(2), RoundingMode.HALF_DOWN);
		h = c;
		l = c;
		if(closeAnterior != null) {
			o = closeAnterior;
		}else {
			o = c;
		}
		precios.add(data);
	}
	
	public void addNewPrecio(TrueFxPreciosData data) {
		c = data.getBid().add(data.getAsk()).divide(new BigDecimal(2), RoundingMode.HALF_DOWN);
		//verificar higt an low
		if(c.compareTo(l) < 0) {
			l = c;
		}else if(c.compareTo(h) > 0) {
			h = c;
		}
		//solo lo acumulo si es distinto al la ultima informacion obtenida
		TrueFxPreciosData ultimo = precios.get(precios.size()-1);//siempre hay uno, porue le contructor agrega un punto
		if(!ultimo.getTimestamp().equals(data.getTimestamp())) {
			precios.add(data);
		}
	}

	public Date getT() {
		return t;
	}
	public void setT(Date t) {
		this.t = t;
	}
	public BigDecimal getO() {
		return o;
	}
	public void setO(BigDecimal o) {
		this.o = o;
	}
	public BigDecimal getH() {
		return h;
	}
	public void setH(BigDecimal h) {
		this.h = h;
	}
	public BigDecimal getL() {
		return l;
	}
	public void setL(BigDecimal l) {
		this.l = l;
	}
	public BigDecimal getC() {
		return c;
	}
	public void setC(BigDecimal c) {
		this.c = c;
	}

	public List<TrueFxPreciosData> getPrecios() {
		return precios;
	}

	public boolean isNotificada() {
		return notificada;
	}

	public void setNotificada(boolean notificada) {
		this.notificada = notificada;
	}

	public BigDecimal getCuerpoAbsoluto() {
		return o.subtract(c).abs();
	}
	
	public BigDecimal getCuerpoConMechaAbsoluto() {
		return l.subtract(h).abs();
	}
	
	public BigDecimal getCuerpo() {
		return c.subtract(o);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public void addObservacion(String obs) {
		if(observacion == null) {
			observacion = obs;
		}else {
			observacion += " y " + obs;
		}
	}
	
	public boolean isNotificadaPorPantalla() {
		return NotificadaPorPantalla;
	}

	public void setNotificadaPorPantalla(boolean notificadaPorPantalla) {
		NotificadaPorPantalla = notificadaPorPantalla;
	}

	/**
	 * Si las mechas contienen una ZI, entonces la devuelve!
	 * Este metodo devuelve la mayor zona, y se devuelve como un numero entero!
	 * @return
	 */
	public BigDecimal getMaxZI() {
		String hi = h.toString().replaceAll("\\.", "");
		String lo = l.toString().replaceAll("\\.", "");
		//TODO aca hay un error, el largo de cada par deberia estar preconfigurado, entonces siempre relleno con ceros hasta completar el largo, esto es por si algun dia coinciden justo el hi y lo en numeros decimales que terminan en cero, entonces pierdo ese decimal y se busca mal el zi!
		if(hi.length() != lo.length()) {//los dos tiene que tener la misma cantidad de digitos
			if(hi.length() > lo.length()) {
				lo = StringUtils.rightPad(lo, hi.length(), "0");
			}else {
				hi = StringUtils.rightPad(hi, lo.length(), "0");
			}
		}
		for (int i = 0; i < hi.length() -1; i++) {// (-1) No interesan el ultimo digito
			if(i == (hi.length() - 2)) {//si los ultimos digitos contienen el 50
				if(new Integer(String.valueOf(hi.charAt(i))) >= 5 && new Integer(String.valueOf(lo.charAt(i))) < 5) {
					String zi = hi.substring(0, hi.length() - 2) + "50";
					int indexDot = h.toString().indexOf(".");
					candleZi = new BigDecimal(indexDot!=-1?zi.substring(0, indexDot)+"."+zi.substring(indexDot, zi.length()):zi);
					return candleZi;
				}
			}else if(hi.charAt(i) != lo.charAt(i)) {
				String zi = hi.substring(0, i+1) + StringUtils.leftPad("", hi.length() - (i+1), "0");
				int indexDot = h.toString().indexOf(".");
				candleZi = new BigDecimal(indexDot!=-1?zi.substring(0, indexDot)+"."+zi.substring(indexDot, zi.length()):zi);
				return candleZi;
			}
		}
		return null;
	}
	
	/**
	 * Si las mechas contienen una ZI, entonces la devuelve!
	 * Este metodo devuelve la menor zona, y se devuelve como un numero entero!
	 * @return
	 */
	public BigDecimal getMinZI() {
		String hi = h.toString().replaceAll("\\.", "");
		String lo = l.toString().replaceAll("\\.", "");
		//TODO aca hay un error, el largo de cada par deberia estar preconfigurado, entonces siempre relleno con ceros hasta completar el largo, esto es por si algun dia coinciden justo el hi y lo en numeros decimales que terminan en cero, entonces pierdo ese decimal y se busca mal el zi!
		if(hi.length() != lo.length()) {//los dos tiene que tener la misma cantidad de digitos
			if(hi.length() > lo.length()) {
				lo = StringUtils.rightPad(lo, hi.length(), "0");
			}else {
				hi = StringUtils.rightPad(hi, lo.length(), "0");
			}
		}
		for (int i = 0; i < hi.length() -1; i++) {// (-1) No interesan el ultimo digito
			if(i == (hi.length() - 2)) {//si los ultimos digitos contienen el 50
				if(new Integer(String.valueOf(hi.charAt(i))) >= 5 && new Integer(String.valueOf(lo.charAt(i))) < 5) {
					String zi = hi.substring(0, hi.length() - 2) + "50";
					int indexDot = h.toString().indexOf(".");
					candleZi = new BigDecimal(indexDot!=-1?zi.substring(0, indexDot)+"."+zi.substring(indexDot, zi.length()):zi);
					return candleZi;
				}
			}else if(hi.charAt(i) != lo.charAt(i)) {
				String zi = null;
				//a diferencia de maxZi, aca se devuleve el char de li + 1, en vez del char de hi y adicionalmente se verifica antes que no contengan una zi del 50, de ser asi se devuelve el char de li + el 50 final
				if(new Integer(String.valueOf(hi.charAt(hi.length() - 2))) >= 5 && new Integer(String.valueOf(lo.charAt(hi.length() - 2))) < 5) {
					zi = lo.substring(0, i+1) + (lo.length() > (i+3)?StringUtils.leftPad("", lo.length() - (i+3), "0"):"") + "50";
				}else {
					zi = lo.substring(0, i) + (new Integer(lo.substring(i, i+1))+1) + StringUtils.leftPad("", lo.length() - (i+1), "0");
				}
				int indexDot = h.toString().indexOf(".");
				candleZi = new BigDecimal(indexDot!=-1?zi.substring(0, indexDot)+"."+zi.substring(indexDot, zi.length()):zi);
				return candleZi;
			}
		}
		return null;
	}

	/**
	 * Este valor solo esta disponible si anteriormente se llamo a getMaxZI() o getMinZI()
	 * @return
	 */
	public BigDecimal getCandleZi() {
		return candleZi;
	}

	public ChartCandlePointDTO getCandleZiClose() {
		return candleZiClose;
	}

	public void setCandleZiClose(ChartCandlePointDTO candleZiClose) {
		this.candleZiClose = candleZiClose;
	}
}
