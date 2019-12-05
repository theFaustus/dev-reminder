package com.evil.devreminder.service.impl;

import com.evil.devreminder.service.MessageFormatter;
import org.springframework.stereotype.Service;

@Service
public class SlackMessageFormatterImpl implements MessageFormatter {

    public static final String BOLD_CHAR = "*";
    private static final String ITALIC_CHAR = "_";
    private static final String CODE_CHAR = "```";
    private static final String STRIKETHROUGH_CHAR = "~";
    private static final String QUOTE_CHAR = ">";

    @Override
    public String bold(String text) {
        return BOLD_CHAR + text + BOLD_CHAR;
    }

    @Override
    public String italic(String text) {
        return ITALIC_CHAR + text + ITALIC_CHAR;
    }

    @Override
    public String codified(String text) {
        return CODE_CHAR + text + CODE_CHAR;
    }

    @Override
    public String strikethrough(String text) {
        return STRIKETHROUGH_CHAR + text + STRIKETHROUGH_CHAR;

    }

    @Override
    public String quote(String text) {
        return QUOTE_CHAR + text;
    }
}
