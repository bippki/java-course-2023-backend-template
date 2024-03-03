package edu.java.bot.dto;

import com.google.gson.Gson;
import java.util.List;

public record ApiErrorResponse(String description, String code, String exceptionName, String exceptionMessage, List<String> stacktrace) {
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static ApiErrorResponse fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ApiErrorResponse.class);
    }
}
