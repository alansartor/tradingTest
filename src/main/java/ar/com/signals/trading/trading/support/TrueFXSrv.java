package ar.com.signals.trading.trading.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import ar.com.signals.trading.util.web.ChartCandlePointDTO;
import ar.com.signals.trading.util.web.ChartLineDataDTO;
import ar.com.signals.trading.util.web.Mensaje;

public interface TrueFXSrv {

	Collection<ChartLineDataDTO<ChartCandlePointDTO>> getRandomData(Date date, int number);

	Collection<ChartLineDataDTO<ChartCandlePointDTO>> getDatos(TrueFXDivisas fltTrueFXDivisas, int i, List<Mensaje> mensajes) throws Exception;

	Collection<ChartLineDataDTO<ChartCandlePointDTO>> getDatosHistoricos(Integer fltHoraIndex) throws FileNotFoundException, IOException, ParseException;

	List<Mensaje> simularControlDatos() throws FileNotFoundException, IOException, ParseException;
	
	void arrancarHilo();
	
	void detenerHilo();
}
