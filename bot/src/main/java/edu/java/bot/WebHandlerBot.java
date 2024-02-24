package edu.java.bot;

import edu.java.bot.util.URLValidator;
import lombok.Getter;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;


public class WebHandlerBot extends AbilityBot {
    private final int creatorId;
    @Getter
    private final RegistrationManager registrationManager;

    private final Map<Long, List<String>> trackedLinks;
    public WebHandlerBot(String token, String botName, int creatorId ) {
        super(token, botName);
        this.creatorId = creatorId;
        this.registrationManager = new RegistrationManager(this);
        this.trackedLinks = new HashMap<>();
    }

    @Override
    public long creatorId() {
        return this.creatorId;
    }



    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String messageText = update.getMessage().getText();
        Long userId = update.getMessage().getChatId();

        if (messageText.startsWith("/") && !messageText.startsWith("/start") && !messageText.startsWith("/help") &&
            !messageText.startsWith("/track") && !messageText.startsWith("/untrack") && !messageText.startsWith("/list")) {
            silent.send("This command is not recognized. Use /help to see the list of available commands.", userId);
            return;
        }

        super.onUpdateReceived(update);
    }

    /**
     * This is needed for our tests, to provide a mocked SilentSender
     *
     * @param silentSender
     */
    public void setSilentSender(SilentSender silentSender) {
        silent = silentSender;
    }

    public Ability startCommand() {
        return Ability
            .builder()
            .name("start")
            .info("Register yourself")
            .locality(USER)
            .privacy(PUBLIC)
            .action(ctx -> {
                Long userId = ctx.user().getId();
                if (!registrationManager.isUserRegistered(userId)) {
                    registrationManager.registerUser(userId);
                    silent.send( "You are now registered!", ctx.chatId());
                } else {
                    silent.send( "You are already registered!", ctx.chatId());
                }
            })
            .build();
    }

    public Ability helpCommand() {
        return Ability
            .builder()
            .name("help")
            .info("I need more help")
            .locality(USER)
            .privacy(PUBLIC)
            .action(ctx -> silent.send("/start -- зарегистрировать пользователя\n" +
                "/help -- вывести окно с командами\n" +
                "/track URL -- начать отслеживание ссылки\n" +
                "/untrack URL -- прекратить отслеживание ссылки\n" +
                "/list -- показать список отслеживаемых ссылок", ctx.chatId()))
            .build();
    }

    public Ability trackCommand() {
        return Ability
            .builder()
            .name("track")
            .info("Start tracking a link")
            .locality(USER)
            .privacy(PUBLIC)
            .action(ctx -> {
                String messageText = ctx.update().getMessage().getText();
                String[] parts = messageText.split(" ");
                if (parts.length < 2) {
                    silent.send( "Please provide a link to track after the command /track.", ctx.chatId());
                    return;
                }
                String link = parts[1];
                Long userId = ctx.user().getId();
                if (URLValidator.isValidUrl(link)) {
                    if (!trackedLinks.containsKey(userId)) {
                        trackedLinks.put(userId, new ArrayList<>());
                    }
                    List<String> userTrackedLinks = trackedLinks.get(userId);
                    if (userTrackedLinks.contains(link)) {
                        silent.send( "You are already tracking this link.", ctx.chatId());
                    } else {
                        userTrackedLinks.add(link);
                        silent.send( "Link tracking started for: " + link, ctx.chatId());
                    }
                } else {
                    silent.send( "The provided argument is not a valid URL.", ctx.chatId());
                }
            })
            .build();
    }


    public Ability untrackCommand() {
        return Ability
            .builder()
            .name("untrack")
            .info("Stop tracking a link")
            .locality(USER)
            .privacy(PUBLIC)
            .action(ctx -> {
                String messageText = ctx.update().getMessage().getText();
                String[] parts = messageText.split(" ");
                if (parts.length < 2) {
                    silent.send( "Please provide a link to untrack after the command /untrack.", ctx.chatId());
                    return;
                }
                String linkToRemove = parts[1];
                Long userId = ctx.user().getId();
                if (trackedLinks.containsKey(userId)) {
                    List<String> userTrackedLinks = trackedLinks.get(userId);
                    if (userTrackedLinks.contains(linkToRemove)) {
                        userTrackedLinks.remove(linkToRemove);
                        silent.send( "Link tracking stopped for: " + linkToRemove, ctx.chatId());
                    } else {
                        silent.send( "You are not tracking the provided link.", ctx.chatId());
                    }
                } else {
                    silent.send( "You are not currently tracking any link.", ctx.chatId());
                }
            })
            .build();
    }

    public Ability listCommand() {
        return Ability
            .builder()
            .name("list")
            .info("Show list of tracked links")
            .locality(USER)
            .privacy(PUBLIC)
            .action(ctx -> {
                Long userId = ctx.user().getId();
                if (trackedLinks.containsKey(userId)) {
                    List<String> userTrackedLinks = trackedLinks.get(userId);
                    StringBuilder reply = new StringBuilder("Your tracked links:\n");
                    for (String link : userTrackedLinks) {
                        reply.append(link).append("\n");
                    }
                    silent.send( reply.toString(), ctx.chatId());
                } else {
                    silent.send( "You are not currently tracking any link.", ctx.chatId());
                }
            })
            .build();
    }

    public Ability undefinedCommand() {
        return Ability
            .builder()
            .name(DEFAULT)
            .locality(USER)
            .privacy(PUBLIC)
            .action(ctx -> {
                silent.send( "Undefined command - use /help to get a list of available commands", ctx.chatId());
            })
            .build();
    }

    public Ability sayHelloWorld() {
        return Ability
            .builder()
            .name("hello")
            .info("says hello world!")
            .input(0)
            .locality(USER)
            .privacy(PUBLIC)
            .action(ctx -> silent.send("Hello world!", ctx.chatId()))
            .post(ctx -> silent.send("Bye world!", ctx.chatId()))
            .build();
    }
}
