package net.nextvizion.strigiformes.text;

import lombok.Getter;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public enum TextFormat {

    STRIKETHROUGH("m"),
    ITALIC("o"),
    BOLD("l"),
    UNDERLINED("n"),
    OBFUSCATED("k");

    @Getter
    private final String code;

    TextFormat(String code) {
        this.code = code;
    }

    public static TextFormat getFormatByCode(String code) {
        for (TextFormat textFormat : values())
            if (textFormat.getCode().equalsIgnoreCase(code))
                return textFormat;
        return null;
    }
}
