package com.evil.devreminder.domain;

public enum CryptoFearGreedType {
    EXTREME_FEAR("\uD83D\uDE31"),
    FEAR("\uD83D\uDE28"),
    NEUTRAL("\uD83D\uDE10"),
    GREED("\uD83D\uDE0B"),
    EXTREME_GREED("\uD83E\uDD2A");

    private final String emoji;

    CryptoFearGreedType(final String emoji) {
        this.emoji = emoji;
    }

    public static CryptoFearGreedType from(String type) {
        switch (type) {
            case "Extreme Fear":
                return EXTREME_FEAR;
            case "Fear":
                return FEAR;
            case "Greed":
                return GREED;
            case "Extreme Greed":
                return EXTREME_GREED;
            default:
                return NEUTRAL;
        }
    }

    public String getEmoji() {
        return emoji;
    }
}
