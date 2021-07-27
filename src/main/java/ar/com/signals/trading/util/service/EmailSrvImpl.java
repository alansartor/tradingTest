package ar.com.signals.trading.util.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ar.com.signals.trading.configuracion.service.PropiedadCategoriaSrv;
import ar.com.signals.trading.eventos.domain.RegistroEvento;
import ar.com.signals.trading.eventos.service.SuscripcionSrv;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;

@Service
public class EmailSrvImpl implements EmailSrv{
	@Resource private JavaMailSender mailSender;
	@Resource private SimpleMailMessage templateMessage;
	@Resource private SuscripcionSrv suscripcionSrv;
	@Resource private PropiedadCategoriaSrv propiedadCategoriaSrv;
	@Resource private OAuth2Srv oAuth2Srv;
	@Resource(name = "messageSource") private MessageSource messageSource;
	//@Autowired private ServidorUtil servidorUtil;
	//@Autowired private ReloadableResourceBundleMessageSource propiedades;
	//@Autowired private ServletContext servletContext;
	
	private Logger logger = LoggerFactory.getLogger(EmailSrv.class);
	
	/**
	 * Los adjuntos pueden ser o una lista de path completo de archivos que estan en el servidor List<String>
	 * o un map con el nombre de archivo que se debe mostrar en el correo y el file Map<String, File>
	 * @param from
	 * @param destinataries
	 * @param destinatariesCC
	 * @param destinatariesBCC
	 * @param asunto
	 * @param texto
	 * @param adjuntos
	 * @throws Exception
	 */
	private void sendMailNoCatch(String from, String[] destinataries, String [] destinatariesCC, String [] destinatariesBCC, String asunto, String texto, boolean isHtml, Object adjuntos) throws Exception {       
		try {
			MimeMessage message = mailSender.createMimeMessage();
			boolean tieneAdjuntos = adjuntos != null;
			List<String> adjuntosPath = null;
			Map<String, File> adjuntosFile = null;
			if(tieneAdjuntos){
				if(adjuntos instanceof Map<?, ?>){
					adjuntosFile = (Map<String, File>) adjuntos;
					tieneAdjuntos = !adjuntosFile.isEmpty();
				}else if(adjuntos instanceof List<?>){
					adjuntosPath = (List<String>) adjuntos;
					tieneAdjuntos = !adjuntosPath.isEmpty();
				}else{
					tieneAdjuntos = false;//No deberia entrar aca
				}
			}
			MimeMessageHelper helper = new MimeMessageHelper(message, tieneAdjuntos);

			helper.setFrom(StringUtils.isNotBlank(from)?from:templateMessage.getFrom());
			helper.setTo(destinataries);
			if(destinatariesCC != null && destinatariesCC.length > 0){
				//Si hay un solo correo y esta en blanco entonces tira error, por eso verifico
				if(destinatariesCC.length > 1 || StringUtils.isNotEmpty(destinatariesCC[0])){
					helper.setCc(destinatariesCC);
				}
			}
			if(destinatariesBCC != null && destinatariesBCC.length > 0){
				//Si hay un solo correo y esta en blanco entonces tira error, por eso verifico
				if(destinatariesBCC.length > 1 || StringUtils.isNotEmpty(destinatariesBCC[0])){
					helper.setBcc(destinatariesBCC);
				}
			}
			helper.setSubject(asunto);
			if(texto == null){
				texto = "";
			}else {
				if(isHtml) {
					texto = texto.replaceAll("\n", "<br>");
				}
			}
			String newLine = isHtml?"<br>":"\n";
			texto += newLine+newLine+"Sistema Campos" + newLine;
			texto += "Mail enviado de forma automatica por el sistema, no responder";
			helper.setText(isHtml?"<html>"+texto+"</html>":texto, isHtml);
			if(tieneAdjuntos){
				if(adjuntosFile != null){
					for (String fileName : adjuntosFile.keySet()) {
						helper.addAttachment(fileName, adjuntosFile.get(fileName));
					}
				}else if(adjuntosPath != null){
					FileSystemResource file;
					for (String fileName : adjuntosPath) {
						file = new FileSystemResource(fileName);
						helper.addAttachment(file.getFilename(), file);
					}
				}
			}	
			
			//seguimos el siguiente instructivo para poder usar OAuth2 autentification con Gmail!
			//https://chariotsolutions.com/blog/post/sending-mail-via-gmail-javamail/
			//(basado en https://developers.google.com/identity/protocols/oauth2/native-app#refresh)
			
			//el instructivo se debe hacer una sola vez, luego solo debemos usar y refrescar los token de acceso
			
			//***** ENTORNO TEST *****
			
			//la api la cree con adgrosario@gmail.com, luego ejecute el python script para darle acceso total a adgrosario@gmail.com
			//de los datos de abajo lo unico que sirven seria el: id_cliente, clien_secret y Refresh Token
			
			//id_cliente = 26003703562-0qs418a0k139k0qd446akflf2bdk7us8.apps.googleusercontent.com
			//clien_secret = hNW7_LEbQZjv7qjzZU2lXZWn
			
			//(instale python.x86_64 en linux de amazon Python 2.7.18)
			
			//python oauth2.py --user=adgrosario@gmail.com --client_id=26003703562-0qs418a0k139k0qd446akflf2bdk7us8.apps.googleusercontent.com --client_secret=hNW7_LEbQZjv7qjzZU2lXZWn --generate_oauth2_token
			//(esto te manda a una url para darle autorizacion a una cuenta determinada, ahi elejimos la de adgrosario para test)
			//verification code = 4/1AfDhmrjkfacsWaIKB9WgOyrSd9CVLgURFPj3NOj_TBPfIKp1ypvbLt5pirc
			
			//Refresh Token: 1//0fhmdIk83fUrrCgYIARAAGA8SNwF-L9Ir_xAVOkxxZt-t5zurUZK4V5Utg1HuOMfSZLoo4oxuvW8q80ayechQuvSdJry5s50UQ64
			//Access Token: ya29.A0AfH6SMB8G9A_-L6PfBBPT1E0mdLkpbXA1ZLStB_6_c9aaPbBMjcAX74Jg9VPp6s_WHyiSGRu_snmP5XrEKp2y7i5cb46ip0htoC_AqH_NeTqg24T6hmw716PTdawvOPhpUuGThcdSX843Ue1yywZ4Ztg_TssAtR9WzrSb6d0BAg
			//Access Token Expiration Seconds: 3599

			//***** ENTORNO PRODUCCION (semi produccion, el server de prueba de amazon) *****
			
			//la api la cree con sist.campos@gmail.com, luego ejecute el python script para darle acceso total a sist.campos@gmail.com
			//de los datos de abajo lo unico que sirven seria el: id_cliente, clien_secret y Refresh Token
			
			//id_cliente = 438435502007-j96a2rq10g2rm3kmk9dqik65tdbc5lme.apps.googleusercontent.com
			//clien_secret = ReMg2WyC2Hb5HX82O6ogMuj6
			
			//(instale python.x86_64 en linux de amazon Python 2.7.18)
			
			//python oauth2.py --user=sist.campos@gmail.com --client_id=438435502007-j96a2rq10g2rm3kmk9dqik65tdbc5lme.apps.googleusercontent.com --client_secret=ReMg2WyC2Hb5HX82O6ogMuj6 --generate_oauth2_token
			//(esto te manda a una url para darle autorizacion a una cuenta determinada, ahi elejimos la de adgrosario para test)
			//verification code = 4/6AHztx628Tg4lrIo7ynku2gkXLRdBcy_mEcG2AoPTyRZs_UXtCNYnzk
			
			//Refresh Token: 1//0fhUMCkSqMf8MCgYIARAAGA8SNwF-L9IrQHszx2aP1PNoKOtU86mYeihPF21BWopxTRY6HTKhPxRXSGX8GuszgytYtfmuhszjAWo
			//Access Token: ya29.A0AfH6SMDrete8LnROoy_JxUPIgbJ44IbKCnFocMCaT3wQF2H6Nvkau9lCFIGkMWTO6skV5TnKgHYJ2x6YUxzUSqJk3nqDQZnVuQoOiMum_xSVR2dDVbeoJTha2L_xg2K-D_7n5EbpEmoTk0Zig3vowgd-sp5T8nf4ioeURCcGtyA
			//Access Token Expiration Seconds: 3599

			((JavaMailSenderImpl)mailSender).setPassword(oAuth2Srv.getOAuth2Token());
			mailSender.send(message);
		} catch (MailException e) {
			throw new Exception(e.toString());
		}
	}
	
	private void sendMail(String from, String[] destinataries, String [] destinatariesCC, String [] destinatariesBCC, String asunto, String texto, Object adjuntos) {       
		try {
			sendMailNoCatch(from, destinataries, destinatariesCC, destinatariesBCC, asunto, texto, false, adjuntos);
		} catch (Exception e) {
			logger.error("No se pudo enviar mail '" + asunto + "'. " + e.getMessage());
		}
	}
	public void informarNoCatch(String from, String[] destinatarios, String[] destinatariosCC, String asunto, String texto, Object adjuntos) throws Exception {
		this.sendMailNoCatch(from, destinatarios, destinatariosCC , null, asunto, texto, false, adjuntos);
	}
	
	@Async
	public void informar(String from, String[] destinatarios, String[] destinatariosCC, String asunto, String texto, List<String> adjuntos) {
		this.sendMail(from, destinatarios, destinatariosCC , null, asunto, texto, adjuntos);
	}
	
	@Async
	public void informar(String[] destinatarios, String[] destinatariosCC, String asunto, String texto, List<String> adjuntos) {
		this.sendMail(null, destinatarios, destinatariosCC , null, asunto, texto, adjuntos);
	}
	
	@Async
	public void informar(String[] destinatarios, String asunto, String texto, List<String> adjuntos) {
		this.sendMail(null, destinatarios, null , null, asunto, texto, adjuntos);
	}
	
	@Async
	public void informarMap(String[] destinatarios, String[] destinatariosCC, String asunto, String texto, Map<String, File> adjuntos) {
		this.sendMail(null, destinatarios, destinatariosCC , null, asunto, texto, adjuntos);
	}
	
	@Async
	public void informarMap(String[] destinatarios, String asunto, String texto, Map<String, File> adjuntos) {
		this.sendMail(null, destinatarios, null , null, asunto, texto, adjuntos);
	}
	
	public void informarEvento(Privilegio evento, List<String> destinatarios, String asuntoSufijo, String cuerpo, Object adjuntos, RegistroEvento registroEvento, Usuario usuarioGenerador) throws Exception {
		if(!destinatarios.isEmpty()){					
			String[] destinatariosMail = destinatarios.toArray(new String[destinatarios.size()]);
			
			String puesto = usuarioGenerador!=null?"Usuario: " + usuarioGenerador.getUsername() + "\n":"";
			String fecha = "Fecha: " + DateFormatUtils.format(registroEvento.getFecha(), "dd/MM/yyyy HH:mm") + "\n";
			String asunto = messageSource.getMessage(evento.getMenuMessageSourceCode(), new Object[] {}, LocaleContextHolder.getLocale());
			this.sendMailNoCatch(null, destinatariosMail, null , null, asunto + (StringUtils.isNotBlank(asuntoSufijo)?" " + asuntoSufijo:""), puesto + fecha + cuerpo, true, adjuntos);
		}
	}

	/**
	 * Valida que el correo tenga un @
	 * @param codigoPostal
	 * @return
	 */
	public static boolean mailValido(String codigoPostal) {
		if(StringUtils.isBlank(codigoPostal))
			return false;
		String[] mails = codigoPostal.split(";");
		for (String mail : mails) {
			if(!mail.contains("@"))
				return false;
		}
		return true;
	}
}
