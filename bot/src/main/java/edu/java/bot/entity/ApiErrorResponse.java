package edu.java.bot.entity;


public record ApiErrorResponse(
     String description,
     int code,
     String exceptionName,
     String exceptionMessage,
     String[] stacktrace)
{}
