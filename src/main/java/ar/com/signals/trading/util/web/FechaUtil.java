package ar.com.signals.trading.util.web;

import java.util.Calendar;
import java.util.Date;

public class FechaUtil {
	/**
	 * Calcula la diferencia en field entre las fechas
	 * La primera es fecha fin, la segunda es fecha ini
	 * Fiel puede ser Calendar.DATE, Calendar.HOUR, Calendar.MINUTE
	 * @param fin
	 * @param inicio
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static long diferencia(Date fin, Date inicio, int field) throws RuntimeException {
		switch (field) {
		case Calendar.DATE:
			return (long) ((fin.getTime() - inicio.getTime()) / (1000 * 60 * 60 * 24));
		case Calendar.HOUR:
			return (long) ((fin.getTime() - inicio.getTime()) / (1000 * 60 * 60));
		case Calendar.MINUTE:
			return (long) ((fin.getTime() - inicio.getTime()) / (1000 * 60));
		case Calendar.SECOND:
			return (long) ((fin.getTime() - inicio.getTime()) / (1000));
		default:
			throw new RuntimeException("No se implemento calculo de diferencia para field " + field);
		}
	}
	
	public static int obtener(Date fecha, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		return calendar.get(field);
	}
}
