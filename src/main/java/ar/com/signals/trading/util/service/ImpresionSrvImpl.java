package ar.com.signals.trading.util.service;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import ar.com.signals.trading.util.support.ImpresionException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

@Service
public class ImpresionSrvImpl implements ImpresionSrv{
	@Resource private EmailSrv emailSrv;
	
	/**
	 * Devuelve el nombre el print service de la impresora a utilizar, si no se
	 * encuentra se tira error.
	 * 
	 * @param usuario
	 * @param nombreImpresora
	 * @return
	 * @throws Exception
	 */
	private PrintService getPrintService(String nombreImpresora) throws ImpresionException {
		if (StringUtils.isEmpty(nombreImpresora)){
			throw new ImpresionException("El nombre de la impresora no puede ser null");
		}
		PrintService impresora = null;
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService printService : services) {
			// PREGUNTA POR LA IMPRESORA QUE TENGA EL PUESTO SETEADO.
			if (printService.getName().equalsIgnoreCase(nombreImpresora)) {
				impresora = printService;
				break;
			}
		}
		if (impresora == null){
			throw new ImpresionException("La impresora '" + nombreImpresora + "' no esta disponible");
		}
		return impresora;
	}
	
	@Override
	public void send(String nombreImpresora, JasperPrint js, int copias, String mail) throws ImpresionException, PrinterException, IOException, JRException {
		if(ENVIAR_AL_CORREO.equalsIgnoreCase(nombreImpresora)){
			if(StringUtils.isEmpty(mail)){
				throw new ImpresionException("No se pudo enviar comprobante por correo debido a que el usuario no tiene cuanta de correo configurado en el sistema");
			}
			//JasperPrint js = obtenerReporte(null, params, reporteCompiled, copias, dataSource, tray, papel, false);
			File file = File.createTempFile("documento"+ js.getName(), ".pdf");
			if(file == null)
				throw new ImpresionException("Error al generar el archivo pdf");
			PrintStream ostream = new PrintStream(file.getAbsolutePath());
			JasperExportManager.exportReportToPdfStream(js, ostream);
			ostream.close();
			
			String[] destinatariosMail = mail.split(";"); 
			List<String> adjuntos = new ArrayList<String>();
			adjuntos.add(file.getAbsolutePath());
			emailSrv.informar(destinatariosMail, "PDF " + js.getName(), "Se adjunta PDF con " + js.getName(), adjuntos);
		}else{
			PrintService impresora = getPrintService(nombreImpresora);
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintService(impresora);
			
			PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
			printRequestAttributeSet.add(new Copies(copias));
			
			//printRequestAttributeSet.add(papel);
			JRPrintServiceExporter exporter = new JRPrintServiceExporter();
			exporter.setExporterInput(new SimpleExporterInput(js));
			
			SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
			configuration.setPrintService(impresora);
			configuration.setDisplayPageDialog(false);
			configuration.setDisplayPrintDialog(false);
			configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		}
	}

}
