package ar.com.signals.trading.telegram.support;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
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
public class StartCommand extends BotCommand {
	   public static final String LOGTAG = "STARTCOMMAND";

	    public StartCommand() {
	        super("start", "With this command you can start the Bot");
	    }

	    @Override
	    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
	        StringBuilder messageBuilder = new StringBuilder();

	        String userName = user.getFirstName() + (user.getLastName()!= null?" " + user.getLastName():"");

	        messageBuilder.append("Hola ").append(userName).append("\n");
	        messageBuilder.append("Para asociar su cuenta de Telegram con el Sistema Campos deberá enviar el Token que figura en la pagina 'Configuracion de Usuario'");
	        

	        SendMessage answer = new SendMessage();
	        answer.setChatId(chat.getId().toString());
	        answer.setText(messageBuilder.toString());

	        try {
	            absSender.execute(answer);
	        } catch (TelegramApiException e) {}
	    }
}
