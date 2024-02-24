package edu.java.bot;

import org.telegram.abilitybots.api.bot.AbilityBot;
import java.util.Map;

public class RegistrationManager {
    private final AbilityBot bot;

    public RegistrationManager(AbilityBot bot) {
        this.bot = bot;
    }

    public void registerUser(Long userId) {
        Map<String, Boolean> registeredUsers = bot.db().getMap("registered_users");
        registeredUsers.put(String.valueOf(userId), true);
    }

    public boolean isUserRegistered(Long userId) {
        Map<String, String> registeredUsers = bot.db().getMap("registered_users");
        return registeredUsers.containsKey(String.valueOf(userId));
    }
}
