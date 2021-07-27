package ar.com.signals.trading.telegram.support;

import java.util.Date;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * This commands starts the conversation with the bot
 *
 * @author Timo Schulz (Mit0x2)
 */
public class MarcarVistoCommand extends BotCommand {
	   public static final String LOGTAG = "MARCARVISTOCOMMAND";
	   private final MyTelegramBotSrvImpl myTelegramBotSrvImpl;
	   
	    public MarcarVistoCommand(MyTelegramBotSrvImpl myTelegramBotSrvImpl) {
	        super("marcar_visto", "Para marcar como vistas las ultimas notificaciones recibidas por este medio");
	        this.myTelegramBotSrvImpl = myTelegramBotSrvImpl;
	    }

	    @Override
	    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
	        StringBuilder messageBuilder = new StringBuilder();
	        
	        int cantidad = myTelegramBotSrvImpl.marcarVistasNotificaciones(chat.getId());
	        if(cantidad == 0) {
	        	messageBuilder.append("No hay notificaciones pendientes de marcar como vistas");
	        }else {
	        	messageBuilder.append(cantidad + " notificaciones marcadas como vistas");    
	        }

	        SendMessage answer = new SendMessage();
	        answer.setChatId(chat.getId().toString());
	        answer.setText(messageBuilder.toString());

	        try {
	            absSender.execute(answer);
	        } catch (TelegramApiException e) {}
	    }
}
