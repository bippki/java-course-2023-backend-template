package edu.java.bot.dto;

import com.google.gson.Gson;
import java.net.URI;
import java.util.List;

public record LinkManager(int id, URI url,String description, List<Long> tgChatIds) {
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static LinkManager fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, LinkManager.class);
    }
}
