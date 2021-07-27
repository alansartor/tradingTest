package ar.com.signals.trading.trading.support;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class TrueFxPreciosData {
	private final Timestamp timestamp;
	private final BigDecimal bid;//vendedor
	private final BigDecimal ask;//comprador
	
	private boolean controlada;//para saber si ya fue verificada por el meto que busca patrones
	
	public TrueFxPreciosData(Timestamp timestamp, BigDecimal bid, BigDecimal ask) {
		this.timestamp = timestamp;
		this.bid = bid;
		this.ask = ask;
	}

	@Override
	public String toString() {
		return timestamp + ", bid=" + bid + ", ask=" + ask;
	}


	public Timestamp getTimestamp() {
		return timestamp;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public BigDecimal getAsk() {
		return ask;
	}

	public boolean isControlada() {
		return controlada;
	}

	public void setControlada(boolean controlada) {
		this.controlada = controlada;
	}
}
