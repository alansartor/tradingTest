package ar.com.signals.trading.trading.support;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.lang3.time.DateUtils;

import ar.com.signals.trading.util.web.ChartCandlePointDTO;
import ar.com.signals.trading.util.web.FechaUtil;

public class CandleEstadisticasDTO implements IEstadisticas{
	/**
	 * Por ahora solo interesan las resistencias en zonas de interes, entonces voy acumulado solo cuando una vela verde contiene una
	 * zona institucional. Si aparece una resistencia mayor se limpian todas las anteriores, pero si aparece una resistencia menor si se registra
	 */
	private Map<BigDecimal, List<ChartCandlePointDTO>> resistenciasZI = new LinkedHashMap<>();//necesito mantener el orden de insersion!
	/**
	 * vela candidato a pico resistencia _/\_
	 */
	private ChartCandlePointDTO candidatoPicoResistencia = null;
	private ChartCandlePointDTO ultimoPicoResistencia = null;
	private ChartCandlePointDTO ultimoPicoResistenciaFueraZi = null;
	
	/**
	 * Por ahora solo interesan los soportes en zonas de interes, entonces voy acumulado solo cuando una vela verde contiene una
	 * zona institucional. Si aparece un soporte menor se limpian todas las anteriores, pero si aparece un soporte mayor si se registra
	 */
	private Map<BigDecimal, List<ChartCandlePointDTO>> soportesZI = new LinkedHashMap<>();//necesito mantener el orden de insersion!
	/**
	 * vela candidato a pico soporte ¨\/¨
	 */
	private ChartCandlePointDTO candidatoPicoSoporte = null;
	private ChartCandlePointDTO ultimoPicoSoporte = null;
	private ChartCandlePointDTO ultimoPicoSoporteFueraZi = null;
	
	//private CircularFifoQueue<ChartCandlePointDTO> ultimasVelas = new CircularFifoQueue<ChartCandlePointDTO>(10);//guardo las ultimas 10 velas para hacer un promedio de los cuerpos!
	private LinkedHashMap<Date,ChartCandlePointDTO> ultimasVelas = new LinkedHashMap<Date,ChartCandlePointDTO>(){
		protected boolean removeEldestEntry(Map.Entry<Date,ChartCandlePointDTO> eldest) {
	        return size() > 30;//en algunas ocaciones necesito ver valor de una vela anterior a un candidato a pico, entonces necesito al menos 30 minutos de info
	    }
	};
	
	public CandleEstadisticasDTO() {}
	
	public BigDecimal getResistenciaZi() {
		return candidatoPicoResistencia!=null?candidatoPicoResistencia.getMaxZI():null;
	}
	
	public BigDecimal getSoporteZi() {
		return candidatoPicoSoporte!=null?candidatoPicoSoporte.getMinZI():null;
	}
	
	public List<ChartCandlePointDTO> getCandlesResistenciasZI(BigDecimal zi) {
		List<ChartCandlePointDTO> picos = resistenciasZI.get(zi);
		if(picos != null) {
			return picos;
		}else {
			return Collections.emptyList();
		}
	}
	
	public List<ChartCandlePointDTO> getCandlesSoportesZI(BigDecimal zi) {
		List<ChartCandlePointDTO> picos = soportesZI.get(zi);
		if(picos != null) {
			return picos;
		}else {
			return Collections.emptyList();
		}
	}

	@Override
	public BigDecimal getUltimaResistenciaZi() {
		if(resistenciasZI.isEmpty()) {
			return null;
		}
		List<Entry<BigDecimal, List<ChartCandlePointDTO>>> entryList = new ArrayList<Map.Entry<BigDecimal, List<ChartCandlePointDTO>>>(resistenciasZI.entrySet());
		Entry<BigDecimal, List<ChartCandlePointDTO>> lastEntry = entryList.get(entryList.size()-1);
		return lastEntry.getValue().size()>=2?lastEntry.getKey():null;
	}
	
	@Override
	public BigDecimal getUltimaSoporteZi() {
		if(soportesZI.isEmpty()) {
			return null;
		}
		List<Entry<BigDecimal, List<ChartCandlePointDTO>>> entryList = new ArrayList<Map.Entry<BigDecimal, List<ChartCandlePointDTO>>>(soportesZI.entrySet());
		Entry<BigDecimal, List<ChartCandlePointDTO>> lastEntry = entryList.get(entryList.size()-1);
		return lastEntry.getValue().size()>=2?lastEntry.getKey():null;
	}
	
	//Soporte: nivel en el que el precio frena su descenso para volver a subir
	//Resistencia: nivel en el que el precio deja de subir para comenzar con su descenso
	public void addClosedCandle(ChartCandlePointDTO candleClosed) {
		ultimasVelas.put(candleClosed.getT(), candleClosed);
		//este metodo se llama cuando arranca un nuevo minuto, entonces se sabe que la vela anterior ya cerro y se la guarda en la estadistica
		if(candleClosed.getC().compareTo(candleClosed.getO()) > 0) {//vela verde
			if(candidatoPicoResistencia == null) {//verificar si es un nuevo candidato a ressitencia
				if(candidatoPicoSoporte == null && ultimoPicoSoporte == null) {//si no hay candidato a pico soporte, entonces no tengo contra que comparar, entonces lo tomo como candidato a pico resistencia
					candidatoPicoResistencia = candleClosed;
				}else {//si hay un candidato a pico soporte o un ultimo pico soporte, entonces para tomar la vela como candidata a resistencia, esta tiene que tener una diferencia significante con el candidato
					BigDecimal diferencia = candleClosed.getO().subtract(candidatoPicoSoporte!=null?candidatoPicoSoporte.getO():ultimoPicoSoporte.getO());
					if(diferencia.compareTo(promedioCuerpoConMechaUltimasVelas()) > 0) {//si hay una diferencia significativa entre el candidato a pico soporte y el posible nuevo candidato a pico resistencia, entonces lo registro como candidato pico resistencia
						candidatoPicoResistencia = candleClosed;
					}
				}
			}else if(candleClosed.getH().compareTo(candidatoPicoResistencia.getL()) > 0){//si la mecha superior de la vela actual llega a la mecha inferior del candidato a resistencia, entonces veo si hace falta hacer algo
				if(candleClosed.getH().compareTo(candidatoPicoResistencia.getH()) > 0) {//la nueva vela verde tiene mecha mayor, entonces la asigno como candidato! (usar mecha, no cierre, anda mejor el algoritmo, ya lo probe)				
					candidatoPicoResistencia = candleClosed;
					//No hago limpieza por ahora, espero hasta saber que estoy ante un pico resistencia antes de llamar a la limpieza
					//registrarNuevoPicoResistencia(candidatoPicoResistencia, false);
				}
			}
			if(candidatoPicoSoporte != null) {//verificar si el ultimo candidato a soporte es realmente un pico soporte, para ello tiene que tener una diferencia sustancial con la vela actual
				BigDecimal diferencia = candleClosed.getC().subtract(candidatoPicoSoporte.getC());
				//si la diferencia entre la base del candidato a soporte y el tope de la vela verde actual es al menos 2 velas promedio, entonce el candidato pasa a ser pico soporte
				if(diferencia.compareTo(promedioCuerpoConMechaUltimasVelas().multiply(new BigDecimal(2))) > 0
						|| FechaUtil.diferencia(candleClosed.getT(), candidatoPicoSoporte.getT(), Calendar.MINUTE) > 4) {//si ya pasaron 5 minutos entonces lo considero pico!
					//si el pico soporte esta en zi, entonces lo registro en la lista, sino solo limpio los picos que correspondan
					//NEW 28/01/2021 verifico si es un pico limpio, es decir que si las velas anteriores son mas grandes, al menos la de 5 minutos antes (para evitar que cuando el precio esta subiendo, que un pico casi inapreciable se considere como un toque en la zi!)
					ChartCandlePointDTO candleAnterior = ultimasVelas.get(DateUtils.addMinutes(candidatoPicoSoporte.getT(), -5));
					if(candleAnterior != null && candidatoPicoSoporte.getL().compareTo(candleAnterior.getL()) < 0) {
						registrarNuevoPicoSoporte(candidatoPicoSoporte, true);
						ultimoPicoSoporte = candidatoPicoSoporte;
						candidatoPicoSoporte = null;
					}else {//si no es un pico limpio, entonces solo hago limpieza
						registrarNuevoPicoSoporte(candidatoPicoSoporte, false);//no lo registro, solo limpio si es necesario picos mas chicos, aunque no es probable que pase eso!
						candidatoPicoSoporte = null;
					}
				}
			}
			if(ultimoPicoSoporteFueraZi != null) {//si hay un pico soporte fuera de zi pendiente de usar para hacer limpieza, entonces verifico, si ya pasaron mas de 12 minutos desde que aparecio el pico entonces veo de hacer la limpieza o lo considero un falso rompimiento
				long minutosDiferencia = FechaUtil.diferencia(candleClosed.getT(), ultimoPicoSoporteFueraZi.getT(), Calendar.MINUTE);
				if(minutosDiferencia > 12) {//ya paso mucho tiempo, entonces hago la limpieza usando esta vela, si es un falso rompimiento entonces esta velo probablemente este por encima de la zi soporte, entonces no se limpiaran los picos soportes en zi, pero si esta vela esta por debajo del zi entonces es probable que no sea un rompimiento entonces esta bien que se limpien los soportes en zi!
					//NEW Veo si lo tengo que considerar un falso rompimiento, caso positivo entonces me quedo con el zi que falsamente rompio!
					if(ultimoPicoSoporteFueraZi.getCandleZiClose() != null && ultimoPicoSoporteFueraZi.getCandleZiClose().getL().compareTo(candleClosed.getH()) < 0) {//lo considero falso rompimiento si la vela actual esta por arriba del zi que falsamente se rompio (es decir el low de la zi roja es menor que el higth de la verde actual)
						//registro la vela que paso por la zi!
						//NEW 28/01/2021 verifico si es un pico limpio, es decir que si las velas anteriores son mas grandes, al menos la de 5 minutos antes (para evitar que cuando el precio esta subiendo, que un pico casi inapreciable se considere como un toque en la zi!)
						ChartCandlePointDTO candleAnterior = ultimasVelas.get(DateUtils.addMinutes(ultimoPicoSoporteFueraZi.getCandleZiClose().getT(), -5));
						if(candleAnterior != null && ultimoPicoSoporteFueraZi.getCandleZiClose().getL().compareTo(candleAnterior.getL()) < 0) {
							registrarNuevoPicoSoporte(ultimoPicoSoporteFueraZi.getCandleZiClose(), true);
							ultimoPicoSoporteFueraZi = null;
						}else {
							//si no es un pico limpio, entonces solo hago limpieza
							registrarNuevoPicoSoporte(ultimoPicoSoporteFueraZi.getCandleZiClose(), false);//no lo registro, solo limpio si es necesario picos mas chicos, aunque no es probable que pase eso!
							ultimoPicoSoporteFueraZi = null;
						}
					}else {
						limpiarPicosSoporte(candleClosed);
						ultimoPicoSoporteFueraZi = null;
					}
				}
			}
		}else if(candleClosed.getC().compareTo(candleClosed.getO()) < 0) {//vela roja
			if(candidatoPicoSoporte == null) {//verificar si es un nuevo candidato a soporte
				if(candidatoPicoResistencia == null && ultimoPicoResistencia == null) {//si no hay candidato a pico resistencia, entonces no tengo contra que comparar, entonces lo tomo como candidato a pico soporte
					candidatoPicoSoporte = candleClosed;
				}else {//si hay un candidato a pico resistencia o un ultimoPicoResistencia, entonces para tomar la vela como candidata a soporte, esta tiene que tene una diferencia sgnificante con el candidato
					BigDecimal diferencia = candidatoPicoResistencia != null?candidatoPicoResistencia.getO():ultimoPicoResistencia.getO().subtract(candleClosed.getO());
					if(diferencia.compareTo(promedioCuerpoConMechaUltimasVelas()) > 0) {//si hay una diferencia significativa entre el candidato a pico resistencia y el posible nuevo candidato a pico soporte, entonces lo registro como candidato pico soporte
						candidatoPicoSoporte = candleClosed;
					}
				}
			}else if(candleClosed.getL().compareTo(candidatoPicoSoporte.getH()) < 0){//si la mecha inferior de la vela actual llega a la mecha superior del candidato a soporte, entonces veo si hace falta hacer algo
				if(candleClosed.getL().compareTo(candidatoPicoSoporte.getL()) < 0) {//la nueva vela roja tiene mecha menor, entonces la asigno como candidato! (usar mecha, no cierre, anda mejor el algoritmo, ya lo probe)
					//si las velas son seguidas, o medio seguidas, y el candidato anterior esta en zi, entonces lo guardo en el candidato nuevo, ya que si llega a ser un falso rompimiento, entonces me tengo que quedar con esa vela del zi!
					long minutosDiferencia = FechaUtil.diferencia(candleClosed.getT(), candidatoPicoSoporte.getT(), Calendar.MINUTE);
					if(minutosDiferencia <= 2) {
						BigDecimal minZI = candidatoPicoSoporte.getMinZI();
						if(minZI != null) {
							candleClosed.setCandleZiClose(candidatoPicoSoporte);
						}
					}
					candidatoPicoSoporte = candleClosed;
					//No hago limpieza por ahora, espero hasta saber que estoy ante un pico soporte antes de llamar a la limpieza
					//registrarNuevoPicoSoporte(candidatoPicoSoporte, false);
				}
			}
			if(candidatoPicoResistencia != null) {//verificar si el ultimo candidato a resistencia es realmente un pico resistencia, para ello tiene que tener una diferencia sustancial con la vela actual
				BigDecimal diferencia = candidatoPicoResistencia.getC().subtract(candleClosed.getC());
				//si la diferencia entre la base del candidato a resistencia y el tope de la vela roja actual es al menos 2 velas promedio, entonce el candidato pasa a ser pico resistencia
				if(diferencia.compareTo(promedioCuerpoConMechaUltimasVelas().multiply(new BigDecimal(2))) > 0
						|| FechaUtil.diferencia(candleClosed.getT(), candidatoPicoResistencia.getT(), Calendar.MINUTE) > 4) {//si ya pasaron 5 minutos entonces lo considero pico!
					//si el pico soporte esta en zi, entonces lo registro en la lista, sino solo limpio los picos que correspondan
					//NEW 28/01/2021 verifico si es un pico limpio, es decir que si las velas anteriores son mas chicas, al menos la de 5 minutos antes (para evitar que cuando el precio esta bajando, que un pico casi inapreciable se considere como un toque en la zi!)
					ChartCandlePointDTO candleAnterior = ultimasVelas.get(DateUtils.addMinutes(candidatoPicoResistencia.getT(), -5));
					if(candleAnterior != null && candidatoPicoResistencia.getH().compareTo(candleAnterior.getH()) > 0) {
						registrarNuevoPicoResistencia(candidatoPicoResistencia, true);
						ultimoPicoResistencia = candidatoPicoResistencia;
						candidatoPicoResistencia = null;
					}else {//si no es un pico limpio, entonces solo hago limpieza
						registrarNuevoPicoResistencia(candidatoPicoResistencia, false);//no lo registro, solo limpio si es necesario picos mas chicos, aunque no es probable que pase eso!
						candidatoPicoResistencia = null;
					}
				}
			}
			if(ultimoPicoResistenciaFueraZi != null) {//si hay un pico resistencia fuera de zi pendiente de usar para hacer limpieza, entonces verifico, si ya pasaron mas de 12 minutos desde que aparecio el pico entonces veo de hacer la limpieza o lo considero un falso rompimiento
				long minutosDiferencia = FechaUtil.diferencia(candleClosed.getT(), ultimoPicoResistenciaFueraZi.getT(), Calendar.MINUTE);
				if(minutosDiferencia > 12) {//ya paso mucho tiempo, entonces hago la limpieza usando esta vela, si es un falso rompimiento entonces esta velo probablemente este por debajo de la zi resistencia, entonces no se limpiaran los picos resistencias en zi, pero si esta vela esta por encima del zi entonces es probable que no sea un rompimiento entonces esta bien que se limpien los resistencias en zi!
					//NEW Veo si lo tengo que considerar un falso rompimiento, caso positivo entonces me quedo con el zi que falsamente rompio!
					if(ultimoPicoResistenciaFueraZi.getCandleZiClose() != null && ultimoPicoResistenciaFueraZi.getCandleZiClose().getH().compareTo(candleClosed.getL()) > 0) {//lo considero falso rompimiento si la vela actual esta por debajo del zi que falsamente se rompio (es decir el higth de la zi verde es menor que el low de la roja actual)
						//registro la vela que paso por la zi!
						//NEW 28/01/2021 verifico si al momento de pasar por la zi anteriormente era mas chico, es deecir que viene con tendencia alsista antes de pasar por la zi, es decir que si las velas anteriores son mas chicas, al menos la de 5 minutos antes (para evitar que cuando el precio esta bajando, que un pico casi inapreciable se considere como un toque en la zi!)
						ChartCandlePointDTO candleAnterior = ultimasVelas.get(DateUtils.addMinutes(ultimoPicoResistenciaFueraZi.getCandleZiClose().getT(), -5));
						if(candleAnterior != null && ultimoPicoResistenciaFueraZi.getCandleZiClose().getH().compareTo(candleAnterior.getH()) > 0) {
							registrarNuevoPicoResistencia(ultimoPicoResistenciaFueraZi.getCandleZiClose(), true);
							ultimoPicoResistenciaFueraZi = null;
						}else {
							//si no es un pico limpio, entonces solo hago limpieza
							registrarNuevoPicoResistencia(ultimoPicoResistenciaFueraZi.getCandleZiClose(), false);//no lo registro, solo limpio si es necesario picos mas chicos, aunque no es probable que pase eso!
							ultimoPicoResistenciaFueraZi = null;
						}
					}else {
						limpiarPicosResistencia(candleClosed);
						ultimoPicoResistenciaFueraZi = null;
					}
				}
			}
		}//vela sin cuerpo, por ahora la ignoro, ya que antes o despues va a venir una vela con cuerpo, entonces esa es la que uso para ver si es resistencia o soporte
	}

	private void registrarNuevoPicoResistencia(ChartCandlePointDTO picoResistencia, boolean registrar) {
		//TODO ver que una vela que esta un poquito arriba de la zi, hace que esa zi ya no se considere como zona de resitencia, quiza habria que permitir una tolerancia en getMaxZI
		BigDecimal ziResistencia = picoResistencia.getMaxZI();
		if(ziResistencia != null) {
			//limpio los picos mas chicos o muy viejos (por ahora pongo 36 horas?, como para que no se llene de forma indefinida)
			for (Iterator<Entry<BigDecimal, List<ChartCandlePointDTO>>> entries = resistenciasZI.entrySet().iterator(); entries.hasNext(); ) {
			    Entry<BigDecimal, List<ChartCandlePointDTO>> entry = entries.next();
			    if(entry.getKey().compareTo(ziResistencia) < 0) {//toda resistencia que este debajo del nuevo se limpia
			    	entries.remove();
				}else if(FechaUtil.diferencia(picoResistencia.getT(), entry.getValue().get(entry.getValue().size() - 1).getT(), Calendar.HOUR)>36) {//se limpian picos viejos! IMPORTANTE: se puede dar el caso que las lista cresca mucho, si se dan picos cada menos de 36 horas y nunca hay otro pico menor, entonces la lista crece y nunca se limpia! Creo que seria algo muy raro! Los fines de semana se limpiara, ya que se reinicia todas las estadisticas
					entries.remove();
				}
			}
			if(registrar) {
				//lo guardo donde corresponda
				List<ChartCandlePointDTO> picos = resistenciasZI.get(ziResistencia);
				if(picos == null) {
					picos = new ArrayList<>();
					resistenciasZI.put(ziResistencia, picos);
					picos.add(picoResistencia);
				}else if(FechaUtil.diferencia(picoResistencia.getT(), picos.get(picos.size() - 1).getT(), Calendar.MINUTE) > 12) {//NEW 26/01/2021 Si se da un pico sobre el mismo zi muy seguido, entonces lo ignoro, solo cuenta el primero!
					picos.add(picoResistencia);
				}		
			}
		}else {
			//NEW No limpio cuando aparece un pico resistencia fuera de una zi, lo que hago es esperar a que aparezca proximo pico soporte, entonces ahi veo si hay que limpiar picos resistencias en zi, o si este pico era un falso rompimiento!
			ultimoPicoResistenciaFueraZi = picoResistencia;
		}
		//NEW Nueva logica de control de falsos rompimientos. Recien aca, uso el pico soporte fuera de zi anterior con este pico resistencia para hacer limpieza de piscos soportes en zi!
		//Aca no es el unico lugar de control de falso rompimiento, tambien se hace luego de x minutos del pico soporte fuera de zi, entonces si se hace el control ahi y se llega a una conclucion entonces se blanquea ultimoPicoSoporteFueraZi y no entra aca
		if(ultimoPicoSoporteFueraZi != null) {
			//Si la diferencia entre los picos opuestos es poca, entonces hago la limpieza de picos resistencias con el pico soporte ya que el pico resistencia es considerado un falso rompimiento!
			long minutosDiferencia = FechaUtil.diferencia(picoResistencia.getT(), ultimoPicoSoporteFueraZi.getT(), Calendar.MINUTE);
			if(minutosDiferencia <= 12) {//en el minuto 13 se hace el corte, es decir que directamente se mira el valor de la divisa y se determina como hacer la limpieza, esto esta en otra parte
				limpiarPicosSoporte(picoResistencia);
				ultimoPicoSoporteFueraZi = null;				
			}
			
		}
	}

	private void registrarNuevoPicoSoporte(ChartCandlePointDTO picoSoporte, boolean registrar) {
		//TODO ver que una vela que esta un poquito abajo de la zi, hace que esa zi ya no se considere como zona de soporte, quiza habria que permitir una tolerancia en getMinZI
		BigDecimal ziSoporte = picoSoporte.getMinZI();
		if(ziSoporte != null) {
			//limpio los picos mas chicos o muy viejos (por ahora pongo 36 horas?, como para que no se llene de forma indefinida)
			for (Iterator<Entry<BigDecimal, List<ChartCandlePointDTO>>> entries = soportesZI.entrySet().iterator(); entries.hasNext(); ) {
			    Entry<BigDecimal, List<ChartCandlePointDTO>> entry = entries.next();
			    if(entry.getKey().compareTo(ziSoporte) > 0) {//todo soporte que este arriba del nuevo se limpia
			    	entries.remove();
				}else if(FechaUtil.diferencia(picoSoporte.getT(), entry.getValue().get(entry.getValue().size() - 1).getT(), Calendar.HOUR)>36) {//se limpian picos viejos! IMPORTANTE: se puede dar el caso que las lista cresca mucho, si se dan picos cada menos de 36 horas y nunca hay otro pico menor, entonces la lista crece y nunca se limpia! Creo que seria algo muy raro! Los fines de semana se limpiara, ya que se reinicia todas las estadisticas
					entries.remove();
				}
			}
			if(registrar) {
				//lo guardo donde corresponda
				List<ChartCandlePointDTO> picos = soportesZI.get(ziSoporte);
				if(picos == null) {
					picos = new ArrayList<>();
					soportesZI.put(ziSoporte, picos);
					picos.add(picoSoporte);
				}else if(FechaUtil.diferencia(picoSoporte.getT(), picos.get(picos.size() - 1).getT(), Calendar.MINUTE) > 12) {//NEW 26/01/2021 Si se da un pico sobre el mismo zi muy seguido, entonces lo ignoro, solo cuenta el primero!
					picos.add(picoSoporte);
				}
			}
		}else {
			//NEW No limpio cuando aparece un pico soporte fuera de una zi, lo que hago es esperar a que aparezca proximo pico resistencia, entonces ahi veo si hay que limpiar picos soportes en zi, o si este pico era un falso rompimiento!
			ultimoPicoSoporteFueraZi = picoSoporte;
		}
		//NEW Nueva logica de control de falsos rompimientos. Recien aca, uso el pico resistencia fuera de zi anterior con este pico soporte para hacer limpieza de piscos resistencias en zi!
		//Aca no es el unico lugar de control de falso rompimiento, tambien se hace luego de x minutos del pico resistencia fuera de zi, entonces si se hace el control ahi y se llega a una conclucion entonces se blanquea ultimoPicoResistenciaFueraZi y no entra aca
		if(ultimoPicoResistenciaFueraZi != null) {
			//Si la diferencia entre los picos opuestos es poca, entonces hago la limpieza de picos resistencias con el pico soporte ya que el pico resistencia es considerado un falso rompimiento!
			long minutosDiferencia = FechaUtil.diferencia(picoSoporte.getT(), ultimoPicoResistenciaFueraZi.getT(), Calendar.MINUTE);
			if(minutosDiferencia <= 12) {//en el minuto 13 se hace el corte, es decir que directamente se mira el valor de la divisa y se determina como hacer la limpieza, esto esta en otra parte
				limpiarPicosResistencia(picoSoporte);
				ultimoPicoResistenciaFueraZi = null;				
			}
			
		}
	}

	private void limpiarPicosResistencia(ChartCandlePointDTO picoReferencia) {
		//limpio los picos mas chicos o muy viejos (por ahora pongo 12 horas?, como para que no se llene de forma indefinida)
		for (Iterator<Entry<BigDecimal, List<ChartCandlePointDTO>>> entries = resistenciasZI.entrySet().iterator(); entries.hasNext(); ) {
		    Entry<BigDecimal, List<ChartCandlePointDTO>> entry = entries.next();
		    if(entry.getKey().compareTo(picoReferencia.getH()) < 0) {//toda resistencia que este abajo del nuevo se limpia
		    	entries.remove();
			}else if(FechaUtil.diferencia(picoReferencia.getT(), entry.getValue().get(entry.getValue().size() - 1).getT(), Calendar.HOUR)>12) {//se limpian picos viejos! IMPORTANTE: se puede dar el caso que las lista cresca mucho, si se dan picos cada menos de 12 horas y nunca hay otro pico menor, entonces la lista crece y nunca se limpia! Creo que seria algo muy raro!
				entries.remove();
			}
		}
	}
	
	private void limpiarPicosSoporte(ChartCandlePointDTO picoReferencia) {
		//limpio los picos mas chicos o muy viejos (por ahora pongo 12 horas?, como para que no se llene de forma indefinida)
		for (Iterator<Entry<BigDecimal, List<ChartCandlePointDTO>>> entries = soportesZI.entrySet().iterator(); entries.hasNext(); ) {
		    Entry<BigDecimal, List<ChartCandlePointDTO>> entry = entries.next();
		    if(entry.getKey().compareTo(picoReferencia.getL()) > 0) {//todo soporte que este arriba del nuevo se limpia
		    	entries.remove();
			}else if(FechaUtil.diferencia(picoReferencia.getT(), entry.getValue().get(entry.getValue().size() - 1).getT(), Calendar.HOUR)>12) {//se limpian picos viejos! IMPORTANTE: se puede dar el caso que las lista cresca mucho, si se dan picos cada menos de 12 horas y nunca hay otro pico menor, entonces la lista crece y nunca se limpia! Creo que seria algo muy raro!
				entries.remove();
			}
		}
	}

	/**
	 * Promedio de las ultimas velas.
	 * ultimasVelas si o si tiene que tener minimo una vela para llamar a este metodo!
	 * @return
	 */
	private BigDecimal promedioCuerpoConMechaUltimasVelas() {
		BigDecimal acumulado = BigDecimal.ZERO;
		Collection<ChartCandlePointDTO> candles = ultimasVelas.values();
		int index = candles.size() - 1;
		int indexLimit = Math.max(0, index - 10);//interesa el promedio de las ultimas 10 nomas
		int count = 0;
		int countAcumuladas = 0;
		for (ChartCandlePointDTO candle : candles) {
			if(count >= indexLimit){
				acumulado = acumulado.add(candle.getCuerpoConMechaAbsoluto());
				++countAcumuladas;
			}
			++count;
		}
		return acumulado.divide(new BigDecimal(countAcumuladas), RoundingMode.DOWN);
	}

	@Override
	public List<ChartCandlePointDTO> getCandlesResistenciasZiMayorQue(BigDecimal zi) {
		//Busco la zi mayor que zi, pero mas cerca de zi
		BigDecimal ziBuscada = null;
		for (BigDecimal ziKey : resistenciasZI.keySet()) {
			if(ziBuscada == null) {
				if(ziKey.compareTo(zi) > 0) {
					ziBuscada = ziKey;
				}
			}else if(ziKey.compareTo(ziBuscada) < 0 && ziKey.compareTo(zi) > 0) {
				ziBuscada = ziKey;
			}
		}
		if(ziBuscada != null) {
			return resistenciasZI.get(ziBuscada);
		}else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<ChartCandlePointDTO> getCandlesSoportesZiMenorQue(BigDecimal zi) {
		//Busco la zi menor que zi, pero mas cerca de zi
		BigDecimal ziBuscada = null;
		for (BigDecimal ziKey : soportesZI.keySet()) {
			if(ziBuscada == null) {
				if(ziKey.compareTo(zi) < 0) {
					ziBuscada = ziKey;
				}
			}else if(ziKey.compareTo(ziBuscada) > 0 && ziKey.compareTo(zi) < 0) {
				ziBuscada = ziKey;
			}
		}
		if(ziBuscada != null) {
			return soportesZI.get(ziBuscada);
		}else {
			return Collections.emptyList();
		}
	}
}
