package com.evil.devreminder.bot;

public class CommandPattern {
    public static final String REGULAR_NOTE = "(get)#(SOFTWARE|MOTIVATION|PRACTICES|software|motivation|practices)";
    public static final String QOD = "(get)#(QOD|qod)";
    public static final String WEATHER = "(get)#(WEATHER|weather)#(\\w+)";
    public static final String TRIVIA = "(get)#(TRIVIA|trivia)";
    public static final String DEX_WOTD = "(get)#(DEX-WOTD|dex-wotd)";
    public static final String DEX_DEF = "(get)#(DEX|dex)#(\\w+)";
    public static final String ADD_NOTE = "(add)#(SOFTWARE|MOTIVATION|PRACTICES|software|motivation|practices)#([\\W\\w\\s]+)#([\\W\\w\\s]+)";
    public static final String COMPLEX_NOTE = "(get)#(COMPLEX|complex)";
}

