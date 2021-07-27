package ar.com.signals.trading.util.service;

import ar.com.signals.trading.util.support.ImpresionException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import java.awt.print.PrinterException;
import java.io.IOException;

public interface ImpresionSrv {
	public static String ENVIAR_AL_CORREO = "Enviar PDF al Correo";
	
	public void send(String nombreImpresora, JasperPrint js, int copias, String mail) throws ImpresionException, PrinterException, IOException, JRException;
}
