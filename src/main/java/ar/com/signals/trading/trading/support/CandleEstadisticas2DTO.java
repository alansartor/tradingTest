package ar.com.signals.trading.trading.support;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.com.signals.trading.util.web.ChartCandlePointDTO;
import ar.com.signals.trading.util.web.FechaUtil;

@Deprecated
public class CandleEstadisticas2DTO implements IEstadisticas{
	/**
	 * Por ahora solo interesan las resistencias en zonas de interes, entonces voy acumulado solo cuando una vela verde contiene una
	 * zona institucional y esta es mayor que el pico maximo desde que se estan registrando datos
	 */
	private List<ChartCandlePointDTO> candlesResistenciasZI = new ArrayList<>();
	/**
	 * es la vela con mayor mecha, para ir sabiendo cuanto es el maximo, si es una vela en ZI, entonces hay una o mas velas en candlesResistenciasZI,
	 * pero si no es una vela en ZI, entonces la lista esta vacia
	 */
	private ChartCandlePointDTO candleMaxima = null;
	
	/**
	 * Por ahora solo interesan los soportes en zonas de interes, entonces voy acumulado solo cuando una vela verde contiene una
	 * zona institucional y esta es mayor que el pico maximo desde que se estan registrando datos
	 */
	private List<ChartCandlePointDTO> candlesSoportesZI = new ArrayList<>();
	/**
	 * es la vela con menor mecha, para ir sabiendo cuanto es el minimo, si es una vela en ZI, entonces hay una o mas velas en candlesSoportesZI,
	 * pero si no es una vela en ZI, entonces la lista esta vacia
	 */
	private ChartCandlePointDTO candleMinima = null;
	
	public CandleEstadisticas2DTO() {}
	
	public BigDecimal getResistenciaZi() {
		return candleMaxima!=null?candleMaxima.getMaxZI():null;
	}
	
	public BigDecimal getSoporteZi() {
		return candleMinima!=null?candleMinima.getMinZI():null;
	}
	
	public List<ChartCandlePointDTO> getCandlesResistenciasZI(BigDecimal zi) {
		return candlesResistenciasZI;
	}
	
	public List<ChartCandlePointDTO> getCandlesSoportesZI(BigDecimal zi) {
		return candlesSoportesZI;
	}
	

	@Override
	public BigDecimal getUltimaResistenciaZi() {
		return candlesResistenciasZI.size()>=2?getResistenciaZi():null;
	}
	
	@Override
	public BigDecimal getUltimaSoporteZi() {
		return candlesSoportesZI.size()>=2?getSoporteZi():null;
	}

	public void addClosedCandle(ChartCandlePointDTO candleClosed) {
		//este metodo se llama cuando arranca un nuevo minuto, entonces se sabe que la vela anterior ya cerro y se la guarda en la estadistica
		if(candleClosed.getC().compareTo(candleClosed.getO()) > 0) {//para detectar resistencias
			if(candleMaxima == null) {
				 candleMaxima = candleClosed;
				 if(candleClosed.getMaxZI() != null) {
					 candlesResistenciasZI.add(candleClosed);
				 }
			}else if(candleClosed.getH().compareTo(candleMaxima.getL()) > 0){//si la mecha superior de la vela actual llega a la mecha inferior de la vela maxima, entonces veo si hace falta hacer algo
				BigDecimal ziCandleMaxima = candleMaxima.getMaxZI();
				BigDecimal ziCandleClosed = candleClosed.getMaxZI();
				//1)la vela maxima contiene una ZI y la nueva no
				if(ziCandleMaxima != null && ziCandleClosed == null) {
					//1-a)si la mecha de la nueva es mas grande que la ZI (si se da eso, toda la vela esta arriba de la ZI) entonces la vela nueva es la nueva candleMaxima y no esta en una zona de interes, limpio la lista
					if(candleClosed.getH().compareTo(ziCandleMaxima) > 0) {
						candleMaxima = candleClosed;
						candlesResistenciasZI.clear();
					}//1-b)si la mecha de la nueva es mas chica que la ZI, entonces ignoro esta vela, ya que reboto antes de tocar la resistencia, aunque llego cerca					
				}else if(ziCandleMaxima != null && ziCandleClosed != null) {//2)la vela maxima contine una ZI y la nueva tambiem
					if(ziCandleMaxima.compareTo(ziCandleClosed) == 0) {//2-a)si las ZI son iguales, entonces acumular las velas en la lista y en candle maxima dejo siempre la ultima
						//NEW si se dan velas seguidas positivas, o alternada entre velas verdes y rojas y todas als verdes tocan la zi, entonces en vez
						//de considerarlas independientes, es decir como distintos choques contra la resistencia, los tomo como un solo intento, es decir reemplazo la ultima vela resistencia!
						if(FechaUtil.diferencia(candleClosed.getT(), candleMaxima.getT(), Calendar.MINUTE) <= 4) {//Pruebo con 4 minutos, es decir que el hueco entre toques a la linea de resistencia tiene que ser de 5 minutos minimo
							//estan muy cerca, las considero un solo intento de tocar la zona de resistencia, es decir reemplazo la ultima
							candlesResistenciasZI.set(candlesResistenciasZI.size()-1, candleClosed);//reemplazo la ultima
						}else {
							//si hay una distancia de al menos 4 velas con respecto a la ultima resistencia, entonces si la agrega a la lista
							candlesResistenciasZI.add(candleClosed);
						}
						candleMaxima = candleClosed;//siempre mantengo la ultima vela!
					}else if(ziCandleClosed.compareTo(ziCandleMaxima) > 0) {//2-b)si la ZI de la nueva es mayor, entonces cambia el mayor, limpio la lista y guardo la nueva
						candleMaxima = candleClosed;
						candlesResistenciasZI.clear();
						candlesResistenciasZI.add(candleClosed);
					}else {//2-c)Si la ZI de la nueva es menor, no hago nada
						
					}
				}else if(ziCandleMaxima == null && ziCandleClosed == null) {//3)la vela maxima no contiene ZI y la nueva tampoco
					//ver cual tiene mecha mayor y dejarla como maxima
					if(candleClosed.getH().compareTo(candleMaxima.getH()) > 0) {
						candleMaxima = candleClosed;
						candlesResistenciasZI.clear();
					}
				}else {//4)la vela maxima no contiene ZI y la nueva Si
					//Si la ZI de la nueva es mayor que la mecha de la actual, entonces cambio la mayor y guardo en la lista
					if(ziCandleClosed.compareTo(candleMaxima.getH()) > 0) {
						candleMaxima = candleClosed;
						candlesResistenciasZI.clear();
						candlesResistenciasZI.add(candleClosed);
					}
				}
			}
		}else if(candleClosed.getC().compareTo(candleClosed.getO()) < 0) {//para detectar soportes
			if(candleMinima == null) {
				 candleMinima = candleClosed;
				 if(candleClosed.getMinZI() != null) {
					 candlesSoportesZI.add(candleClosed);
				 }
			}else if(candleClosed.getL().compareTo(candleMinima.getH()) < 0){//si la mecha inferior de la vela actual llega a la mecha superior de la vela minima, entonces veo si hace falta hacer algo
				BigDecimal ziCandleMinima = candleMinima.getMinZI();
				BigDecimal ziCandleClosed = candleClosed.getMinZI();
				//1)la vela minima contiene una ZI y la nueva no
				if(ziCandleMinima != null && ziCandleClosed == null) {
					//1-a)si la mecha de la nueva es mas chica que la ZI (si se da eso, toda la vela esta debajo de la ZI) entonces la vela nueva es la nueva candleMinima y no esta en una zona de interes, limpio la lista
					if(candleClosed.getL().compareTo(ziCandleMinima) < 0) {
						candleMinima = candleClosed;
						candlesSoportesZI.clear();
					}//1-b)si la mecha de la nueva es mas grande que la ZI, entonces ignoro esta vela, ya que reboto antes de tocar la resistencia, aunque llego cerca
				}else if(ziCandleMinima != null && ziCandleClosed != null) {//2)la vela minima contine una ZI y la nueva tambiem
					if(ziCandleMinima.compareTo(ziCandleClosed) == 0) {//2-a)si las ZI son iguales, entonces acumular las velas en la lista y en candle minima dejo siempre la ultima
						//NEW si se dan velas seguidas negativas, o alternada entre velas rojas y verdes y todas las rojas tocan la zi, entonces en vez
						//de considerarlas independientes, es decir como distintos choques contra el soporte, los tomo como un solo intento, es decir reemplazo la ultima vela soporte!
						if(FechaUtil.diferencia(candleClosed.getT(), candleMinima.getT(), Calendar.MINUTE) <= 4) {//Pruebo con 4 minutos, es decir que el hueco entre toques a la linea de soporte tiene que ser de 5 minutos minimo
							//estan muy cerca, las considero un solo intento de tocar la zona de soporte, es decir reemplazo la ultima
							candlesSoportesZI.set(candlesSoportesZI.size()-1, candleClosed);//reemplazo la ultima
						}else {
							//si hay una distancia de al menos 4 velas con respecto a la ultima resistencia, entonces si la agrega a la lista
							candlesSoportesZI.add(candleClosed);
						}
						candleMinima = candleClosed;//siempre mantengo la ultima vela!
					}else if(ziCandleClosed.compareTo(ziCandleMinima) < 0) {//2-b)si la ZI de la nueva es menor, entonces cambia el menor, limpio la lista y guardo la nueva
						candleMinima = candleClosed;
						candlesSoportesZI.clear();
						candlesSoportesZI.add(candleClosed);
					}else {//2-c)Si la ZI de la nueva es mayor, no hago nada
						
					}
				}else if(ziCandleMinima == null && ziCandleClosed == null) {//3)la vela minima no contiene ZI y la nueva tampoco
					//ver cual tiene mecha menor y dejarla como maxima
					if(candleClosed.getL().compareTo(candleMinima.getL()) < 0) {
						candleMinima = candleClosed;
						candlesSoportesZI.clear();
					}
				}else {//4)la vela minima no contiene ZI y la nueva Si
					//Si la ZI de la nueva es menor que la mecha de la actual, entonces cambio la menor y guardo en la lista
					if(ziCandleClosed.compareTo(candleMinima.getL()) < 0) {
						candleMinima = candleClosed;
						candlesSoportesZI.clear();
						candlesSoportesZI.add(candleClosed);
					}
				}
			}
		}//vela sin cuerpo, por ahora la ignoro, ya que antes o despues va a venir una vela con cuerpo, entonces esa es la que uso para ver si es resistencia o soporte
	}

	@Override
	public List<ChartCandlePointDTO> getCandlesResistenciasZiMayorQue(BigDecimal maxZi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChartCandlePointDTO> getCandlesSoportesZiMenorQue(BigDecimal maxZi) {
		// TODO Auto-generated method stub
		return null;
	}
}
