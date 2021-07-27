package ar.com.signals.trading.telegram.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ar.com.signals.trading.eventos.domain.RegistroNotificacion;
import ar.com.signals.trading.eventos.service.RegistroNotificacionSrv;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.dto.TokenTemporalDTO;
import ar.com.signals.trading.seguridad.service.UsuarioSrv;
import ar.com.signals.trading.seguridad.support.SeguridadUtil;
import ar.com.signals.trading.support.ScheduledTasks;

@Component
public class MyTelegramBotSrvImpl extends TelegramLongPollingCommandBot {
	private String botToken;
	private String botUsername;
	
	private List<BotCommand> commands = new ArrayList<>();
	
	static {
		ApiContextInitializer.init();//esto se tiene que llamar una solavez, y se tiene que hacer antes de crear la entidad MyTelegramBotSrvImpl que la crea spring cuando inicializa el context
	}
	
	@Resource UsuarioSrv usuarioSrv;
	@Resource RegistroNotificacionSrv registroNotificacionSrv;
	
	private Logger logger = LoggerFactory.getLogger(MyTelegramBotSrvImpl.class);
	
	public MyTelegramBotSrvImpl() {
		super(ApiContext.getInstance(DefaultBotOptions.class), false);
		register(new StartCommand());
		register(new MarcarVistoCommand(this));
	    HelpCommand helpCommand = new HelpCommand();
	    register(helpCommand);
	    
        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setParseMode(ParseMode.HTML);//IMPORTANTE: ojo con lo que queremos enviar, porque telegram va a tirar error si ponemos un tag que no reconoce!
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help " + TelegramEmojiEnum.AMBULANCE);
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
            	logger.error(e.getMessage());
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        });
        
        for (IBotCommand botCommand : this.getRegisteredCommands()) {
        	commands.add(new BotCommand(botCommand.getCommandIdentifier(), botCommand.getDescription()));
		}
	}

	@Override
	protected boolean filter(Message message) {
		return !message.getChat().isUserChat();//true para ignorar comandos (pero el update se procesa igual como un mensaje de texto en processNonCommandUpdate)
	}

	@Override
	public void processNonCommandUpdate(Update update) {
		//aca hay que procesar los mensajes que se manden luego de un comando!
		//Para eso habria que armar un registro temporal de chats!
		
		//por ahora solo proceso el codigo de vinculacion
        if (update.hasMessage()) {
            if (update.getMessage().hasText() && update.getMessage().getChat() != null) {
            	if(!update.getMessage().getChat().isUserChat()) {
    				//responder que solo puede enviar mensajes privados al bot
    		        try {
    					 SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
    				                .setChatId(update.getMessage().getChatId())
    				                .setParseMode(ParseMode.HTML)//IMPORTANTE: ojo con lo que queremos enviar, porque telegram va a tirar error si ponemos un tag que no reconoce!
    				                .setText("Los mensajes al Bot del Sistema debe hacerlos por un chat privado, no en grupos");
    		            execute(message); // Call method to send the message
    		        } catch (TelegramApiException e) {}
    		        return;
    			}
    			//primero verificar si la cuenta de telegram esta asociada a un usuario, y luego recien analizar comandos
    			//por ahora buscamos directamente en la base de datos, lo que hay que armar despues es un Map de los Chats actuales, ya que los usuarios van a mandar comandos y es probable que se necesite mas info, entonces es un ida y vuelta de un par de mensajes (armar una ChatDTO que arranca con un comando y finaliza con una respuesta correcta o finaliza or expiration o por enviar un nuevo comando)
    			Usuario telegramUser = usuarioSrv.getByTelegramId(update.getMessage().getChat().getId().toString());
    			if(telegramUser == null) {
    				//esto quiere decir que es una cuenta nueva de telegram, entonces tiene que enviar el token
					String token = update.getMessage().getText().trim();
					//verifico si existe el token y si es valido
					Entry<Long, TokenTemporalDTO> tokenData = SeguridadUtil.getTokenTemporal(token);
					if(tokenData == null) {//envio cualquier cosa, entonces avisa
						try {
	    					 SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
	    				                .setChatId(update.getMessage().getChatId())
	    				                .setParseMode(ParseMode.HTML)//IMPORTANTE: ojo con lo que queremos enviar, porque telegram va a tirar error si ponemos un tag que no reconoce!
	    				                .setText("El token enviado es incorrecto. Debe enviar el token que figura en el Sistema");
	    		            execute(message); // Call method to send the message
	    		        } catch (TelegramApiException e) {}
						return;
					}else {
						if(tokenData.getValue().getExpirationTime().before(new Date())) {
							try {
		    					 SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
		    				                .setChatId(update.getMessage().getChatId())
		    				                .setParseMode(ParseMode.HTML)//IMPORTANTE: ojo con lo que queremos enviar, porque telegram va a tirar error si ponemos un tag que no reconoce!
		    				                .setText("El token que envio esta vencido. Vuelva a ingresar a la pantalla 'Configuracion de Usuario' del Sistema Campos para obtener un nuevo Token (debe refrescar la pantalla para obtener un nuevo token)");
		    		            execute(message); // Call method to send the message
		    		        } catch (TelegramApiException e) {}
							return;
						}else {
							//asociar chat_id (que seria igual al usuario_id, ya que es un chat privado) al usuario del sistema
							usuarioSrv.vincularConTelegram(tokenData.getKey(), update.getMessage().getChat().getId());
							SeguridadUtil.removeToken(tokenData.getKey());
							try {
		    					 SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
		    				                .setChatId(update.getMessage().getChatId())
		    				                .setParseMode(ParseMode.HTML)//IMPORTANTE: ojo con lo que queremos enviar, porque telegram va a tirar error si ponemos un tag que no reconoce!
		    				                .setText("La vinculacion de su cuenta Telegram con el Sistema Campos se realizó con exito! Ya puede dar de alta Suscripciones para empezar a recibir alertas y notificaciones del Sistema por Telegram");
		    		            execute(message); // Call method to send the message
		    		        } catch (TelegramApiException e) {}
							return;
						}
					}					
    			}else {
    				try {
   					 SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
   				                .setChatId(update.getMessage().getChatId())
   				                .setParseMode(ParseMode.HTML)//IMPORTANTE: ojo con lo que queremos enviar, porque telegram va a tirar error si ponemos un tag que no reconoce!
   				                .setText("No se implemento logica para recibir comandos");
   		            execute(message); // Call method to send the message
   		        } catch (TelegramApiException e) {}
    				return;
    			}
            }        
        }
	}
	
	public void setBotToken(String botToken) {
		this.botToken = botToken;
	}

	public void setBotUsername(String botUsername) {
		this.botUsername = botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	public void actualizarComandos() throws TelegramApiException {
		this.execute(new SetMyCommands(this.commands));
	}

	public int marcarVistasNotificaciones(Long telegram_id) {
		return registroNotificacionSrv.marcarVistasNotificaciones(telegram_id);
	}
}
