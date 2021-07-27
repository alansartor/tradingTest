package ar.com.signals.trading.trading.support;

import java.math.BigDecimal;
import java.util.List;

import ar.com.signals.trading.util.web.ChartCandlePointDTO;

public interface IEstadisticas {
	public BigDecimal getResistenciaZi();
	
	public BigDecimal getSoporteZi();
	
	public List<ChartCandlePointDTO> getCandlesResistenciasZI(BigDecimal zi);
	
	public List<ChartCandlePointDTO> getCandlesSoportesZI(BigDecimal zi);

	public void addClosedCandle(ChartCandlePointDTO candleClosed);

	/**
	 * Devuelve el ultimo zi, solo si la lista tiene al menos 2 registros
	 * @return
	 */
	public BigDecimal getUltimaResistenciaZi();
	
	/**
	 * Devuelve el ultimo zi, solo si la lista tiene al menos 2 registros
	 * @return
	 */
	public BigDecimal getUltimaSoporteZi();

	public List<ChartCandlePointDTO> getCandlesResistenciasZiMayorQue(BigDecimal maxZi);

	public List<ChartCandlePointDTO> getCandlesSoportesZiMenorQue(BigDecimal maxZi);
}