package ar.com.signals.trading.trading.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrueFXDivisaLastData {
	private Date minuto;
	private final List<TrueFxPreciosData> precios = new ArrayList<TrueFxPreciosData>();
	
	public TrueFXDivisaLastData(Date minuto) {
		this.minuto = minuto;
	}
	public Date getMinuto() {
		return minuto;
	}
	public void setMinuto(Date minuto) {
		this.minuto = minuto;
	}
	public List<TrueFxPreciosData> getPrecios() {
		return precios;
	}
}
