package ar.com.signals.trading.trading.support;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.com.signals.trading.configuracion.service.PropiedadCategoriaSrv;
import ar.com.signals.trading.configuracion.support.PropiedadCategoriaEnum;
import ar.com.signals.trading.eventos.service.RegistroEventoSrv;
import ar.com.signals.trading.eventos.support.Patron;
import ar.com.signals.trading.util.support.BusinessGenericException;
import ar.com.signals.trading.util.web.ChartCandlePointDTO;
import ar.com.signals.trading.util.web.ChartLineDataDTO;
import ar.com.signals.trading.util.web.FechaUtil;
import ar.com.signals.trading.util.web.Mensaje;

@Service
public class TrueFXSrvImpl implements TrueFXSrv{
	@Resource(name="trueFxHttpClient") private HttpClient httpClient; 
	private Logger logger = LoggerFactory.getLogger(TrueFXSrv.class);
	@Resource private RegistroEventoSrv registroEventoSrv;
	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	
	private ConcurrentMap<TrueFXDivisas, LinkedHashMap<Date,ChartCandlePointDTO>> colasCandles = new ConcurrentHashMap<TrueFXDivisas, LinkedHashMap<Date,ChartCandlePointDTO>>();
	
	/**
	 * Para no notificar lo mismo dos veces seguidas
	 * NEW 27/01/2021 Antes registraba por TipoAlertaTrading, ahora cambie ignorando eso, solo interesa una alerta sobre un zi, entonces solo me quedo con la ultima vela
	 * NEW 05/05/2021 Ahora se separa por Patron, ya que cada usario se suscribe a un patron distinto!
	 */
	private ConcurrentMap<TrueFXDivisas, Map<Patron,ChartCandlePointDTO>> ultimaNotificacion = new ConcurrentHashMap<TrueFXDivisas, Map<Patron,ChartCandlePointDTO>>();
	//private ConcurrentMap<TrueFXDivisas, Map<TipoAlertaTrading,ChartCandlePointDTO>> ultimaNotificacion = new ConcurrentHashMap<TrueFXDivisas, Map<TipoAlertaTrading,ChartCandlePointDTO>>();
	
	//estadisticas, para ir probando a detectar mas cosas
	private ConcurrentMap<TrueFXDivisas, IEstadisticas> estadisticasCandle = new ConcurrentHashMap<TrueFXDivisas, IEstadisticas>();

	private InternalThread hilo = null;
	private volatile boolean alive = false;//Para cortar el bucle
	
	public static final int MAX_ENTRIES = 100;
	
	@Override
	public Collection<ChartLineDataDTO<ChartCandlePointDTO>> getRandomData(Date date, int number) {
		ChartLineDataDTO<ChartCandlePointDTO> dto = new ChartLineDataDTO<ChartCandlePointDTO>();
		dto.setLabel(TrueFXDivisas.EUR_USD.getCodigo());
		dto.setBorderColor(null);
		dto.setBackgroundColor(null);
		List<ChartCandlePointDTO> datos = new ArrayList<>();
		int count = 1;
		BigDecimal lastClose = new BigDecimal(1.20000);
		while (datos.size() < number) {
			int randomNum = ThreadLocalRandom.current().nextInt(lastClose.multiply(new BigDecimal(100000)).multiply(new BigDecimal(0.95)).intValue(), lastClose.multiply(new BigDecimal(100000)).multiply(new BigDecimal(1.05)).intValue());
			BigDecimal open = new BigDecimal(randomNum).divide(new BigDecimal(100000), 5, RoundingMode.HALF_DOWN);
			randomNum = ThreadLocalRandom.current().nextInt(lastClose.multiply(new BigDecimal(100000)).multiply(new BigDecimal(0.95)).intValue(), lastClose.multiply(new BigDecimal(100000)).multiply(new BigDecimal(1.05)).intValue());
			BigDecimal close = new BigDecimal(randomNum).divide(new BigDecimal(100000), 5, RoundingMode.HALF_DOWN);
			randomNum = ThreadLocalRandom.current().nextInt(lastClose.multiply(new BigDecimal(100000)).multiply(new BigDecimal(0.95)).intValue(), lastClose.multiply(new BigDecimal(100000)).multiply(new BigDecimal(1.05)).intValue());
			BigDecimal high = new BigDecimal(randomNum).divide(new BigDecimal(100000), 5, RoundingMode.HALF_DOWN);
			randomNum = ThreadLocalRandom.current().nextInt(lastClose.multiply(new BigDecimal(100000)).multiply(new BigDecimal(0.95)).intValue(), lastClose.multiply(new BigDecimal(100000)).multiply(new BigDecimal(1.05)).intValue());
			BigDecimal low = new BigDecimal(randomNum).divide(new BigDecimal(100000), 5, RoundingMode.HALF_DOWN);
			datos.add(new ChartCandlePointDTO(DateUtils.addMinutes(date, count), open, high, low, close));
			count += 1;
		}
		dto.setData(datos);
		return Arrays.asList(dto);
	}

	@Override
	public Collection<ChartLineDataDTO<ChartCandlePointDTO>> getDatos(TrueFXDivisas trueFXDivisas, int number, List<Mensaje> mensajes) throws Exception {
		//devuelvo los datos
		LinkedHashMap<Date,ChartCandlePointDTO> pila = colasCandles.get(trueFXDivisas);
		if(pila != null) {
			//las lista puede tener hasta 100 valores, solo interesan los ultimos x
			ChartLineDataDTO<ChartCandlePointDTO> dto = new ChartLineDataDTO<ChartCandlePointDTO>();
			dto.setLabel(trueFXDivisas.getCodigo());
			int count = pila.size();
			for (ChartCandlePointDTO point : pila.values()) {
				if(count > number) {//ignoro los datos viejos, hasta llegar a la cantidad que se quiere mostrar, entonces ahi recien empiezo a meter en los datos que quiero
					--count;
				}else {
					--count;
					dto.getData().add(point);
					if(count == 0 && point.isNotificada() && !point.isNotificadaPorPantalla()) {//si es el ultimo, veo si tiene alguna observacion
						point.setNotificadaPorPantalla(true);
						mensajes.add(new Mensaje(Mensaje.INFO, point.getObservacion()));
					}
				}
			}
			return Arrays.asList(dto);
		}else {
			//mando algo sin nada, hasta que haya datos
			ChartLineDataDTO<ChartCandlePointDTO> dto = new ChartLineDataDTO<ChartCandlePointDTO>();
			dto.setLabel(trueFXDivisas.getCodigo());
			return Arrays.asList(dto);
		}
	}
	
	private void controlDeVelas() {
		LinkedHashMap<Date,ChartCandlePointDTO> pila = null;
		IEstadisticas estadisticas = null;
		Date fechaActual = new Date();
		for (TrueFXDivisas trueFXDivisas : TrueFXDivisas.values()) {
			pila = colasCandles.get(trueFXDivisas);
			estadisticas = estadisticasCandle.get(trueFXDivisas);
			controlDeVelas(trueFXDivisas, pila, estadisticas, ultimaNotificacion, fechaActual, false);
		}
	}

	private String controlDeVelas(TrueFXDivisas trueFXDivisas, LinkedHashMap<Date, ChartCandlePointDTO> pila, IEstadisticas estadisticas, ConcurrentMap<TrueFXDivisas, Map<Patron,ChartCandlePointDTO>> ultimaNotificacion, Date fechaActual, boolean simulacion) {
		if(pila != null && pila.size() > 2) {//tiene que haber al menos 3 velas
			Object[] keys = pila.keySet().toArray();
			ChartCandlePointDTO velaActual = pila.get(keys[keys.length -1]);
			//por las dudas verifico que sea una vela de la fecha actual, una vela del minuto anterior no interesa controlar, y si es una vela que ya se notifico no hace falta seguir controlandola
			if(FechaUtil.diferencia(fechaActual, velaActual.getT(), Calendar.MINUTE)==0 && !velaActual.isNotificada()) {//TODO verificar que la hora que viene del TrueFX coincida con la hora del servidor
				//si el ultmo dato de la vela actual ya habia sido controlado, entonces no hago nada
				TrueFxPreciosData ultimaCotizacion = velaActual.getPrecios().get(velaActual.getPrecios().size()-1);
				if(!ultimaCotizacion.isControlada()) {
					ultimaCotizacion.setControlada(true);//la marco controlada asi no se revisa mas
					BigDecimal v0 = velaActual.getCuerpo();
					if(v0.compareTo(BigDecimal.ZERO) != 0) {//una vela sin cuerpo no hace falta controlar
						//Primer alerta: si |V0| > |V1| > |V2|, no se usa mas, demasiadas alertas
						//NEW 08/12/2020: si (V0 y V1 y V2 > 0 o < 0) y |V0| > |V1| y |V0| > |V2| (estas se discriminan en PMG y XXL)
						//NEW 18/12/2020: si (V0 y V2 > 0 y V1 =< 0 o V0 y V2 < 0 y V1 >= 0)  y |V0| > |V1| y |V0| > |V2| entonces VRV o RVR
						//comparo el cuerpo con el de la ultima vela
						BigDecimal v1 = pila.get(keys[keys.length -2]).getCuerpo();
						BigDecimal v2 = pila.get(keys[keys.length -3]).getCuerpo();
						TipoAlertaTrading tipoAlerta = null;
						if((v0.compareTo(BigDecimal.ZERO) > 0 && v1.compareTo(BigDecimal.ZERO) > 0 && v2.compareTo(BigDecimal.ZERO) > 0)//si todas tienen el mismo color
								|| (v0.compareTo(BigDecimal.ZERO) < 0 && v1.compareTo(BigDecimal.ZERO) < 0 && v2.compareTo(BigDecimal.ZERO) < 0)//si todas tienen el mismo color
								|| (v0.compareTo(BigDecimal.ZERO) > 0 && v2.compareTo(BigDecimal.ZERO) > 0 && v1.compareTo(BigDecimal.ZERO) <= 0)//si la primera y la ultima tienen el mismo color
								|| (v0.compareTo(BigDecimal.ZERO) < 0 && v2.compareTo(BigDecimal.ZERO) < 0 && v1.compareTo(BigDecimal.ZERO) >= 0)) {//si la primera y la ultima tienen el mismo color
							if(v0.abs().compareTo(v1.abs()) > 0 && v0.abs().compareTo(v2.abs()) > 0 ) {
								//Verificar si se dio un patron, y verificar si termina en una zi para saber si se debe notificar
								String observaciones = trueFXDivisas.getCodigo() + " - " + DateFormatUtils.format(velaActual.getT(), "HH:mm") + "<br>";
								if(v0.compareTo(BigDecimal.ZERO) == v1.compareTo(BigDecimal.ZERO)) {//son todas del mismo color
									tipoAlerta = (v0.compareTo(BigDecimal.ZERO) > 0?(v1.abs().compareTo(v2.abs()) > 0?TipoAlertaTrading.PMG_ALCISTA:TipoAlertaTrading.XXL_ALCISTA):(v1.abs().compareTo(v2.abs()) > 0?TipoAlertaTrading.PMG_BAJISTA:TipoAlertaTrading.XXL_BAJISTA));
									observaciones += (v0.compareTo(BigDecimal.ZERO) > 0?"Alcista":"Bajista") + (v1.abs().compareTo(v2.abs()) > 0?" (PMG)":" (XXL)");
								}else {//NEW 28/01/2021 si es un vrv o un rvr entonces se tiene que dar una condicion mas que es que la vela 0 tiene que ser 5 veces mas grande que la vela del medio! (esto es porque este patron se da seguido cuando el precio sigsague, pero interesa solo cuando la vela q es chica y la vela 0 es grande!)
									if(v0.abs().compareTo(v1.abs().multiply(new BigDecimal(5))) > 0) {
										tipoAlerta = (v0.compareTo(BigDecimal.ZERO) > 0?TipoAlertaTrading.VRV_ALCISTA:TipoAlertaTrading.RVR_BAJISTA);
										observaciones += (v0.compareTo(BigDecimal.ZERO) > 0?"Alcista (VRV)":"Bajista (RVR)");
									}
								}
								if(tipoAlerta != null) {
									Set<Patron> patronesToInform = new HashSet<>();
									Map<Patron, String> patronObservaciones = new HashMap<>();
									
									//determinar la zi, y luego buscar en las estadisticas para ver si la zi es una Resistencia y desde hace cuanto tiempo, ej: R0 R1 ..
									BigDecimal maxZi = null;
									BigDecimal minZi = null; 
									if(velaActual.getC().compareTo(velaActual.getO()) > 0) {//vela verde, busco maxZi
										maxZi = velaActual.getMaxZI();//esto setea la candleZi en velaActual
									}else if(velaActual.getC().compareTo(velaActual.getO()) < 0) {//vela roja, busco minZi
										minZi = velaActual.getMinZI();//esto setea la candleZi en velaActual
									}//una vela con cuerpo cero por ahora no interesa (ya que la vela anterior o siguiente es la que generaria una alerta segu los patrones que estamos buscando)
																		
									//Pmg("PMG y XXL en ZI ..00 (no se tiene en cuenta antiguedad ni toques)
									if((TipoAlertaTrading.PMG_ALCISTA.equals(tipoAlerta) || TipoAlertaTrading.XXL_ALCISTA.equals(tipoAlerta))
											&& maxZi != null) {
										if(maxZi.toString().endsWith("00")) {
											patronesToInform.add(Patron.Pmg);
											patronObservaciones.put(Patron.Pmg, trueFXDivisas.getCodigo() + " " + tipoAlerta + " en " + maxZi.toString());
										}
									}else if((TipoAlertaTrading.PMG_BAJISTA.equals(tipoAlerta) || TipoAlertaTrading.XXL_BAJISTA.equals(tipoAlerta))
											&& minZi != null) {
										if(minZi.toString().endsWith("00")) {
											patronesToInform.add(Patron.Pmg);
											patronObservaciones.put(Patron.Pmg, trueFXDivisas.getCodigo() + " " + tipoAlerta + " en " + minZi.toString());
										}
									}
									
									if(estadisticas != null) {
										if(maxZi != null) {//verifico si la zi de la vela coincide con alguna zi de resistencia
											//verificar si maxZi es una retencion en las estadisticas!
											List<ChartCandlePointDTO> resistencias = estadisticas.getCandlesResistenciasZI(maxZi);
											if(resistencias.size() >=2) {//para considerarse resistencia se tiene que dar 2 veces al menos
												long horasAntiguedad = FechaUtil.diferencia(velaActual.getT(), resistencias.get(0).getT(), Calendar.HOUR);
												if(horasAntiguedad >= 2) {//NEW 28/01/2021 Informar solo si el primer pico fue hace al menos 2 hora (lo cambie de 1 a 2)
													observaciones += " (R"+horasAntiguedad+" en ZI:"+maxZi+")";					
													patronesToInform.add(Patron.Pmg_en_rango);
													patronObservaciones.put(Patron.Pmg_en_rango, observaciones);
												}else if(horasAntiguedad == 1){
													long minutosAntiguedad = FechaUtil.diferencia(velaActual.getT(), resistencias.get(0).getT(), Calendar.MINUTE);
													if(minutosAntiguedad >= 90) {//NEW 09/022/2021 Se agrego que informe con zi de hasta 90 minutos de antiguedad, ya que hay casos que se pueden operar
														observaciones += " (R"+horasAntiguedad+" en ZI:"+maxZi+")";
														patronesToInform.add(Patron.Pmg_en_rango);
														patronObservaciones.put(Patron.Pmg_en_rango, observaciones);
													}else {//si el zi tiene entre 60 y 90 minutos, y el zi anterior (mayor) tiene mas de 90 minutos, entonces alertarlo!
														List<ChartCandlePointDTO> resistenciasMayor = estadisticas.getCandlesResistenciasZiMayorQue(maxZi);
														if(!resistenciasMayor.isEmpty()) {
															long minutosAntiguedadZiAnterior = FechaUtil.diferencia(velaActual.getT(), resistenciasMayor.get(resistenciasMayor.size() -1).getT(), Calendar.MINUTE);
															if(minutosAntiguedadZiAnterior >= 90) {
																observaciones += " (R"+horasAntiguedad+" en ZI:"+maxZi+")";
																patronesToInform.add(Patron.Pmg_en_rango);
																patronObservaciones.put(Patron.Pmg_en_rango, observaciones);
															}
														}
													}
												}
											}else {
												//IMPORTANTE:  por ahora no interesa los rompimientos del las zi, ver mas adelante si se puede buscar algun patron en estos eventos y como alertarlos!
	/*											BigDecimal ultimaResistenciaZi = estadisticas.getUltimaResistenciaZi();
												if(ultimaResistenciaZi != null && maxZi.compareTo(ultimaResistenciaZi) > 0) {//no creo que se de, ya que seria el caso que la vela supera la zi y alcanza una nueva zi!
													observaciones += " (Ruptura Resistencia:"+ultimaResistenciaZi+")";
													tipoAlerta = TipoAlertaTrading.RUPTURA_RESISTENCIA;
													informar = true;
												}*/
											}							
										}else if(minZi != null) {//verifico si la zi de la vela coincide con el soporte actual
											//verificar si minZi es un soporte en las estadisticas!
											List<ChartCandlePointDTO> soportes = estadisticas.getCandlesSoportesZI(minZi);
											if(soportes.size() >=2 ) {//para considerarse soporte se tiene que dar 2 veces al menos
												long horasAntiguedad = FechaUtil.diferencia(velaActual.getT(), soportes.get(0).getT(), Calendar.HOUR);
												if(horasAntiguedad >= 2) {//NEW 28/01/2021 Informar solo si el primer pico fue hace al menos 2 hora (lo cambie de 1 a 2)
													observaciones += " (S"+horasAntiguedad+" en ZI:"+minZi+")";
													patronesToInform.add(Patron.Pmg_en_rango);
													patronObservaciones.put(Patron.Pmg_en_rango, observaciones);
												}else if(horasAntiguedad == 1){
													long minutosAntiguedad = FechaUtil.diferencia(velaActual.getT(), soportes.get(0).getT(), Calendar.MINUTE);
													if(minutosAntiguedad >= 90) {//NEW 09/022/2021 Se agrego que informe con zi de hasta 90 minutos de antiguedad, ya que hay casos que se pueden operar
														observaciones += " (S"+horasAntiguedad+" en ZI:"+minZi+")";
														patronesToInform.add(Patron.Pmg_en_rango);
														patronObservaciones.put(Patron.Pmg_en_rango, observaciones);
													}else {//si el zi tiene entre 60 y 90 minutos, y el zi anterior (menor) tiene mas de 90 minuitos, entonces alertarlo!
														List<ChartCandlePointDTO> soportesMenor = estadisticas.getCandlesSoportesZiMenorQue(maxZi);
														if(!soportesMenor.isEmpty()) {
															long minutosAntiguedadZiAnterior = FechaUtil.diferencia(velaActual.getT(), soportesMenor.get(soportesMenor.size() -1).getT(), Calendar.MINUTE);
															if(minutosAntiguedadZiAnterior >= 90) {
																observaciones += " (S"+horasAntiguedad+" en ZI:"+minZi+")";
																patronesToInform.add(Patron.Pmg_en_rango);
																patronObservaciones.put(Patron.Pmg_en_rango, observaciones);
															}
														}
													}
												}
											}else {
												//IMPORTANTE:  por ahora no interesa los rompimientos del las zi, ver mas adelante si se puede buscar algun patron en estos eventos y como alertarlos!
	/*											BigDecimal ultimaSoporteZi = estadisticas.getUltimaSoporteZi();
												if(ultimaSoporteZi != null && minZi.compareTo(ultimaSoporteZi) < 0) {//no creo que se de, ya que seria el caso que la vela supera la zi y alcanza una nueva zi!
													observaciones += " (Ruptura Soporte:"+ultimaSoporteZi+")";
													tipoAlerta = TipoAlertaTrading.RUPTURA_SOPORTE;
													informar = true;
												}*/
											}																						
										}else {//si no estoy en una zona intitucional, entonces verificar si no me pase de una zona de resistencia o soporte
											//IMPORTANTE:  por ahora no interesa los rompimientos del las zi, ver mas adelante si se puede buscar algun patron en estos eventos y como alertarlos!
	/*										if(velaActual.getC().compareTo(velaActual.getO()) > 0) {//vela verde, verifico si pase una resistencia
												//verifico si hay una resistencia en este momento
												BigDecimal ultimaResistenciaZi = estadisticas.getUltimaResistenciaZi();
												if(ultimaResistenciaZi != null && velaActual.getO().compareTo(ultimaResistenciaZi) > 0) {//si la apertura de la vela es mayor que la zi de resistencia, entonces es probable una ruptura
													observaciones += " (Ruptura Resistencia:"+ultimaResistenciaZi+")";
													tipoAlerta = TipoAlertaTrading.RUPTURA_RESISTENCIA;
													informar = true;
												}
											}else if(velaActual.getC().compareTo(velaActual.getO()) < 0) {//vela roja, verifico si pase un soporte
												//verifico si hay un soporte en este momento
												BigDecimal ultimaSoporteZi = estadisticas.getUltimaSoporteZi();
												if(ultimaSoporteZi != null && velaActual.getO().compareTo(ultimaSoporteZi) < 0) {//si la apertura de la vela es menor que la zi de soporte, entonces es probable una ruptura
													observaciones += " (Ruptura Soporte:"+ultimaSoporteZi+")";
													tipoAlerta = TipoAlertaTrading.RUPTURA_SOPORTE;
													informar = true;
												}
											}*/										
										}
									}

									if(!patronesToInform.isEmpty()){										
										velaActual.setNotificada(true);//esto es para saber que ya se hizo una notificacion sobre esta vela, no es necesario volver a notificar
										//27/01/2021 antes guardaba las notificaciones por TipAlertaTrading, ahora no importa, solo interesa no alertar muy seguido cosas sobre una zi
										//Map<TipoAlertaTrading,ChartCandlePointDTO> notificaciones = ultimaNotificacion.get(trueFXDivisas);
										//05/05/2021 Guardar las notificaciones por Patron
										Map<Patron,ChartCandlePointDTO> notificaciones = ultimaNotificacion.get(trueFXDivisas);
										for (Iterator<Patron> iterator = patronesToInform.iterator(); iterator.hasNext();) {
											Patron patron = iterator.next();
											velaActual.addObservacion(patronObservaciones.get(patron));
											if(notificaciones != null) {
												ChartCandlePointDTO velaUltimaNotificacion = notificaciones.get(patron);
												if(velaUltimaNotificacion != null) {
													//no volver a notificar lo mismo si pasa dentro de los 15 minutos, es decir si dentro de poco tiempo pasa el mismo evento sobre la misma zi, entonces no informo
													if(FechaUtil.diferencia(velaActual.getT(), velaUltimaNotificacion.getT(), Calendar.MINUTE) <= 15
															&& velaUltimaNotificacion.getCandleZi() != null) {
														if(velaUltimaNotificacion.getCandleZi().equals(velaActual.getCandleZi())) {//no repetir alertas sobre la misma zi en corto tiempo
															iterator.remove();
															continue;
														}
													}
													notificaciones.put(patron, velaActual);//esto hace que en los siguientes 15 minutos tampoco se vuelva a informar sobre la misma zi!
												}else {
													notificaciones.put(patron, velaActual);
												}
											}else {
												notificaciones = new HashMap<>();
												notificaciones.put(patron, velaActual);
												ultimaNotificacion.put(trueFXDivisas, notificaciones);
											}
										}

										if(!patronesToInform.isEmpty()) {
											if(simulacion) {
												return observaciones;
											}else {
												registroEventoSrv.tradingAlertInmediata(trueFXDivisas, patronObservaciones, patronesToInform);
											}
										}
									}									
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	class InternalThread extends Thread{
		public void run(){
			try {
				alive = true;
				HttpClientContext context = new HttpClientContext();
				String user = propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_USUARIO_TRUEFX);//usuario en test el dle ejemplo de la pagina (jsTrader), y en produccion usar=asartor
				String url = propiedadCategoriaSrv.getString(PropiedadCategoriaEnum.PROPIEDAD_URL_TRUEFX);
				HttpGet httpGetSession = new HttpGet(url+"?u="+user+"&p=anystring&q=ozrates&c="+TrueFXDivisas.getCodes()+"&f=csv&s=y");
				HttpGet httpGet = null;
				HttpResponse response = null;
				HttpEntity entity = null;
				Scanner sc = null;
				String[] partes = null;
				TrueFXDivisas trueFXDivisas = null;
				Timestamp timestamp = null;
				BigDecimal bid = null;//vendedor
				BigDecimal ask = null;//comprador
				Date minuto = null;
				LinkedHashMap<Date,ChartCandlePointDTO> pila = null;
				ChartCandlePointDTO minutoDatos = null; 
				ChartCandlePointDTO minutoAnteriorDatos = null;
				IEstadisticas estadistica = null;
				long iterations = 0;
				long fallasControl = 0;
				long fallas = 0;
				long start = 0;
				while (alive) {
					try {
						++iterations;
						start = System.currentTimeMillis();
						//si no hay session, entonces consugo una, luego llamo a buscar datos
						if(httpGet == null) {
							response = httpClient.execute(httpGetSession, context);
							entity = response.getEntity();
							String respuesta = EntityUtils.toString(entity);
							if(StringUtils.isNotEmpty(respuesta)) {
								respuesta = StringUtils.chomp(respuesta);//LIMPIO EL NEW LINE DEL FINAL!
								if("not authorized".equalsIgnoreCase(respuesta)) {
									//por lo que probe, para que de este error hay que enviar un nombre de usuario desconocido! Es la unica forma
									throw new BusinessGenericException("La pagina de TrueFx devolvio rta 'not authorized' al querer obtener una session id con el usuario: " + user + ". Verificar si el usuario es todavia valido para la pagina. El Hilo no se podra iniciar hasta no solucionarlo!");
								}else {									
									httpGet = new HttpGet(url+"?id="+respuesta);//error: illegal character in query
									//httpGet = new HttpGet(url+"?id="+URLEncoder.encode(respuesta, "UTF-8"));
								}
							}
						}else {
							//llamar a truefx					 
							response = httpClient.execute(httpGet, context);
							entity = response.getEntity();
							sc = new Scanner(entity.getContent());
							boolean firstLine = true;
				            while (sc.hasNext()) {
				            	partes = sc.nextLine().split(",", 7);
				            	if(firstLine) {
				            		firstLine = false;
				            		//Si la respuesta tiene formato por defecto (concatenated string) significa que la session se perdio, hay que volver a gestionarla
				            		if(partes.length == 1) {
				            			httpGet = null;//esto genera que se tenga que solicitar una nueva session
				            			break;
				            		}
				            	}
				            	if(partes.length >= 6) {//solo interesan las primeras 6 partes (EUR/USD,1607091879471,1.21,626,1.21,630,1.21253,1.21792,1.21424) 
				            		trueFXDivisas = TrueFXDivisas.getByCode(partes[0]);
				            		if(trueFXDivisas != null) {
				            			timestamp = new Timestamp(Long.parseLong(partes[1]));
				            			bid = new BigDecimal(partes[2] + partes[3]);//1.21626
				            			ask = new BigDecimal(partes[4] + partes[5]);
				            			minuto = DateUtils.truncate(timestamp, Calendar.MINUTE);//se agrupan por minuto
				            			pila = colasCandles.get(trueFXDivisas);
				            			estadistica = estadisticasCandle.get(trueFXDivisas);
				            			minutoDatos = pila!=null?pila.get(minuto):null;
				            			if(minutoDatos == null) {
				            				//buscar el minuto anterior el valor del cierre
				            				minutoAnteriorDatos = pila!=null?pila.get(DateUtils.addMinutes(minuto, -1)):null;
				            				minutoDatos = new ChartCandlePointDTO(minuto, minutoAnteriorDatos!=null?minutoAnteriorDatos.getC():null, new TrueFxPreciosData(timestamp, bid, ask));
				            				if(pila == null) {
				            					pila = new LinkedHashMap<Date,ChartCandlePointDTO>(){
				            						protected boolean removeEldestEntry(Map.Entry<Date,ChartCandlePointDTO> eldest) {
				            					        return size() > MAX_ENTRIES;
				            					    }
				            					};
				            					colasCandles.put(trueFXDivisas, pila);
				            				}
				            				if(estadistica == null) {
				            					//creo la clase donde vamos a guardar las estadisticas
				            					estadistica = new CandleEstadisticasDTO();
				            					estadisticasCandle.put(trueFXDivisas, estadistica);
				            				}			       
				            				pila.put(minuto, minutoDatos);
				            				if(minutoAnteriorDatos != null) {
					            				estadistica.addClosedCandle(minutoAnteriorDatos);//esta vela ya cerro, entonces la guardo en las estadisticas (no se guarda, solo se recalculan estadisticas)
				            				}
				            			}else {
				            				minutoDatos.addNewPrecio(new TrueFxPreciosData(timestamp, bid, ask));
				            			}
				            		}
				            	}
				            }
				            sc.close();
				            //agrego el control para ver si hay que arrojar alertas
				            //por ahora es sincronico, ver, porque si tarda mucho hay que pasarlo a asyncronico?
				            //TODO solo controlo si hace mas de 3 minutos que no tengo casi nada de fallas de conexion, ya que si hay muchas fallas los datos no serian reales
				            //quiza no tanto los micro cortes, lo que si interesa que no haya un hueco de falta de informacion, es decir una perdida de 15 segundo de datos
				            try {
					            controlDeVelas();
				            }catch (Exception e) {
								++fallasControl;
								if(fallasControl % 20 == 0) {
									logger.info("Fallo 20 veces control de datos (ultimo error: "+e.getMessage()+")");
								}
							}
		
				            if(iterations % 1200 == 0) {
				            	logger.info("tiempo obtencion datos tradeFx: " + (System.currentTimeMillis()-start));
				            }
				            //la respuesta de la pagina e interpretar los datos demora unos 200 milisegundos, mas los 250 que duermo, dan unos 450 milisegundos por iteracion
				            //entonces por segundo se llama dos veces al truefx
				            //la pantalla se actualiza cada 1 segundo
				            //NEW 04/12/2020 En amazon, la llamada a tradefx y procesar los datos tarda unos 45 milisegundos
				            Thread.sleep(Math.max(0,500-(System.currentTimeMillis()-start)));//duermo para completar 500 ms entre que llamo a trufx y que realizo lso demas procesos							
						}
					} catch (BusinessGenericException be) {
						throw be;//esto detiene el hilo!
					} catch (Exception e) {
						++fallas;
						if(fallas > 100) {//TODO ver como manejar esto cuando se deje un proceso todo el dia, o en el rango de horarios que interesa!
							if(httpGet == null) {
								logger.warn("El hilo que busca datos de trueFX genero 100 fallas de conexion al intentar gestionar session id (ultimo error: "+e.getMessage()+")");
							}else {
								logger.warn("El hilo que busca datos de trueFX genero 100 fallas de conexion o interpretacion de datos (ultimo error: "+e.getMessage()+")");
							}
							//TODO mandar notificacion al administrador!!!!
						}
						//si falla la conexion duermo un segundo antes de la proxima llamada
						Thread.sleep(1000);
					}
				}
			} catch (BusinessGenericException be) {	
				logger.warn("Se detiene el hilo debido al error: " + be.getMessage());
			} catch (Exception e) {
				logger.warn("Se detiene el hilo debido a error inesperado en la conexion con trueFX. " + e.getMessage());
			} finally {
				hilo = null;
				colasCandles.clear();//limpio todos los valores viejos!
				estadisticasCandle.clear();
			}			
		}
	}

	@Override
	public Collection<ChartLineDataDTO<ChartCandlePointDTO>> getDatosHistoricos(Integer fltHoraIndex) throws FileNotFoundException, IOException, ParseException {
		LinkedHashMap<Date,ChartCandlePointDTO> pila = null;	
		DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Timestamp timestamp = null;
		BigDecimal bid = null;//vendedor
		BigDecimal ask = null;//comprador
		Date minuto = null;
		ChartCandlePointDTO minutoDatos = null;
		ChartCandlePointDTO minutoAnteriorDatos = null;
		Timestamp firstDate = null;
		//Busco los datos de un csv en una carpeta
		String source = "/HOME/TOMCAT/Trading/EURGBP.csv";
		try (BufferedReader br = new BufferedReader(new FileReader(source))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        timestamp = new Timestamp(df.parse(values[1]).getTime());//20201102 10:00:00.187
		        if(firstDate == null) {
		        	firstDate = timestamp;
		        }
    			bid = new BigDecimal(values[2]);//1.16401
    			ask = new BigDecimal(values[3]);//1.16404
    			minuto = DateUtils.truncate(timestamp, Calendar.MINUTE);//se agrupan por minuto
    			minutoDatos = pila!=null?pila.get(minuto):null;
    			if(minutoDatos == null) {
    				//buscar el minuto anterior el valor del cierre
    				minutoAnteriorDatos = pila!=null?pila.get(DateUtils.addMinutes(minuto, -1)):null;
    				minutoDatos = new ChartCandlePointDTO(minuto, minutoAnteriorDatos!=null?minutoAnteriorDatos.getC():null, new TrueFxPreciosData(timestamp, bid, ask));
    				if(pila == null) {
    					pila = new LinkedHashMap<Date,ChartCandlePointDTO>();
    				}		       
    				pila.put(minuto, minutoDatos);
    			}else {
    				minutoDatos.addNewPrecio(new TrueFxPreciosData(timestamp, bid, ask));
    			}
		    }
		}
		ChartLineDataDTO<ChartCandlePointDTO> dto = new ChartLineDataDTO<ChartCandlePointDTO>();
		dto.setLabel(TrueFXDivisas.EUR_USD.getCodigo());
		if(!pila.isEmpty()) {
			firstDate = new Timestamp(DateUtils.addHours(firstDate, fltHoraIndex).getTime());
			Date lastHora = DateUtils.addHours(firstDate, 3);//muestro 3 horas
			for (ChartCandlePointDTO candle : pila.values()) {
				if(candle.getT().after(firstDate)) {
					if(candle.getT().before(lastHora)) {
						dto.getData().add(candle);
					}else {
						break;
					}
				}
			}
		}		
		return Arrays.asList(dto);
	}

	@Override
	public List<Mensaje> simularControlDatos() throws FileNotFoundException, IOException, ParseException {
		List<Mensaje> mensajes = new ArrayList<>();
		String source = "/HOME/TOMCAT/Trading/EURGBP.csv";
		Mensaje mensaje = new Mensaje(Mensaje.INFO, "Simulacion archivo "+source+" finaliza con exito, alertas halladas:");
		mensajes.add(mensaje);
		DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Timestamp timestamp = null;
		BigDecimal bid = null;//vendedor
		BigDecimal ask = null;//comprador
		Date minuto = null;
		LinkedHashMap<Date,ChartCandlePointDTO> pila = new LinkedHashMap<Date,ChartCandlePointDTO>();
		ConcurrentMap<TrueFXDivisas, Map<Patron,ChartCandlePointDTO>> ultimaNotificacion = new ConcurrentHashMap<TrueFXDivisas, Map<Patron,ChartCandlePointDTO>>();
		ChartCandlePointDTO minutoDatos = null; 
		ChartCandlePointDTO minutoAnteriorDatos = null;
		IEstadisticas estadistica = new CandleEstadisticasDTO();
		try (BufferedReader br = new BufferedReader(new FileReader(source))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        timestamp = new Timestamp(df.parse(values[1]).getTime());//20201102 10:00:00.187
    			bid = new BigDecimal(values[2]);//1.16401
    			ask = new BigDecimal(values[3]);//1.16404
    			minuto = DateUtils.truncate(timestamp, Calendar.MINUTE);//se agrupan por minuto			
    			minutoDatos = pila!=null?pila.get(minuto):null;
    			if(minutoDatos == null) {
    				//buscar el minuto anterior el valor del cierre
    				minutoAnteriorDatos = pila!=null?pila.get(DateUtils.addMinutes(minuto, -1)):null;
    				minutoDatos = new ChartCandlePointDTO(minuto, minutoAnteriorDatos!=null?minutoAnteriorDatos.getC():null, new TrueFxPreciosData(timestamp, bid, ask));   				 
    				pila.put(minuto, minutoDatos);
    				if(minutoAnteriorDatos != null) {
        				estadistica.addClosedCandle(minutoAnteriorDatos);//esta vela ya cerro, entonces la guardo en las estadisticas (no se guarda, solo se recalculan estadisticas)
    				}
    			}else {
    				minutoDatos.addNewPrecio(new TrueFxPreciosData(timestamp, bid, ask));
    			}
    			String alerta = controlDeVelas(TrueFXDivisas.EUR_USD, pila, estadistica, ultimaNotificacion, timestamp, true);
    			if(alerta != null) {
    				mensaje.addLine(alerta);
    			}
		    }
		}
		return mensajes;
	}

	@Override
	public void arrancarHilo() {
		//IMPORTANTE VERIFICAR QUE EL HILO SEA NULL, PARA NO LLAMAR 2 VECES!
		if(propiedadCategoriaSrv.getBoolean(PropiedadCategoriaEnum.PROPIEDAD_INICIAR_THREAD_TRUEFX) && hilo == null) {
			logger.info("Arrancando hilo busqueda datos trueFX");
			//disparo para que inicie la busqueda de datos
			hilo = new InternalThread();
			hilo.start();
		}
	}
	
	@Override
	public void detenerHilo() {
		if(alive) {
			logger.info("Se inicia la detencion del Hilo");
			alive = false;
		}else {
			logger.info("Se llamo al metodo para detener el Hilo, pero no estaba vivo");
		}
	}
}
