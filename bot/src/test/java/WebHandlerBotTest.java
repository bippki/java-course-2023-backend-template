import edu.java.bot.WebHandlerBot;
import edu.java.bot.configuration.ApplicationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;
import static org.mockito.Mockito.*;
@SpringBootTest(classes = ApplicationConfig.class)
class WebHandlerBotTest {
    public static final long USER_ID = 1337L;
    public static final long CHAT_ID = 1337L;

    private WebHandlerBot bot;
    private SilentSender silent;
    @Autowired
    private ApplicationConfig config;

    @BeforeEach
    public void setUp() {
        bot = new WebHandlerBot(
            config.getTelegramToken(),
            config.getBotName(),
            config.getCreatorId()
        );
        bot.onRegister();
        silent = mock(SilentSender.class);
        bot.setSilentSender(silent);
    }



    @Test
    public void canHelp() {
        Update upd = new Update();
        User user = new User();
        user.setId(USER_ID);
        user.setFirstName("Susus");
        user.setLastName("Amogus");
        user.setIsBot(false);

        MessageContext context = MessageContext.newContext(upd, user, CHAT_ID, bot);

        bot.helpCommand().action().accept(context);

        Mockito.verify(silent, times(1)).send("/start -- зарегистрировать пользователя\n" +
            "/help -- вывести окно с командами\n" +
            "/track URL -- начать отслеживание ссылки\n" +
            "/untrack URL -- прекратить отслеживание ссылки\n" +
            "/list -- показать список отслеживаемых ссылок", CHAT_ID);
    }

    @Test
    public void testTrackCommandWithValidLink() throws IOException {
        String URL = "http://example.com";
        Update upd = new Update();
        User user = new User();
        user.setId(USER_ID);
        user.setFirstName("Afrus");
        user.setLastName("Magus");
        user.setIsBot(false);

        Message message = new Message();
        message.setText("/track " + URL);

        upd.setMessage(message);

        MessageContext context = MessageContext.newContext(upd, user, CHAT_ID, bot);
        bot.trackCommand().action().accept(context);

        Mockito.verify(silent, Mockito.times(1)).send("Link tracking started for: " + URL, CHAT_ID);
        bot.db().close();
    }


    @Test
    public void testTrackCommandWithInvalidLink() throws IOException {
        String notURL = "server-shizophrenia";
        Update upd = new Update();
        User user = new User();
        user.setId(USER_ID);
        user.setFirstName("Susus");
        user.setLastName("Amogus");
        user.setIsBot(false);

        Message message = new Message();
        message.setText("/track " + notURL);

        upd.setMessage(message);

        MessageContext context = MessageContext.newContext(upd, user, CHAT_ID, bot);

        bot.trackCommand().action().accept(context);
        Mockito.verify(silent, Mockito.times(1)).send("The provided argument is not a valid URL.", CHAT_ID);
        bot.db().close();
    }

    @Test
    public void testTrackEmpty() throws IOException {
        Update upd = new Update();
        User user = new User();
        user.setId(USER_ID);
        user.setFirstName("Susus");
        user.setLastName("Amogus");
        user.setIsBot(false);

        Message message = new Message();
        message.setText("/track ");

        upd.setMessage(message);

        MessageContext context = MessageContext.newContext(upd, user, CHAT_ID, bot);
        bot.trackCommand().action().accept(context);

        Mockito.verify(silent, Mockito.times(1)).send("Please provide a link to track after the command /track.", CHAT_ID);
        bot.db().close();
    }

    @Test
    public void testTrackDublicateUntrack() throws IOException {
        String URL = "http://example.com";
        Update upd = new Update();
        User user = new User();
        user.setId(USER_ID);
        user.setFirstName("Susus");
        user.setLastName("Amogus");
        user.setIsBot(false);

        Message message = new Message();
        message.setText("/track " + URL);
        upd.setMessage(message);
        MessageContext context = MessageContext.newContext(upd, user, CHAT_ID, bot);
        bot.trackCommand().action().accept(context);

        Mockito.verify(silent, Mockito.times(1)).send("Link tracking started for: " + URL, CHAT_ID);

        message.setText("/track " + URL);
        upd.setMessage(message);
        bot.trackCommand().action().accept(context);

        Mockito.verify(silent, Mockito.times(1)).send("You are already tracking this link.", CHAT_ID);

        message.setText("/untrack " + URL);
        upd.setMessage(message);
        bot.untrackCommand().action().accept(context);

        Mockito.verify(silent, Mockito.times(1)).send("Link tracking stopped for: " + URL, CHAT_ID);

        bot.db().close();

    }




}
