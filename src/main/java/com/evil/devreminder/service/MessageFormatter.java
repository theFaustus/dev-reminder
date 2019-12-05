package com.evil.devreminder.service;

public interface MessageFormatter {
    String bold(String text);
    String italic(String text);
    String codified(String text);
    String strikethrough(String text);
    String quote(String text);
}
