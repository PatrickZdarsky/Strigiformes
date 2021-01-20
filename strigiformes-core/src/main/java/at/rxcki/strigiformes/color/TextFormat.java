package at.rxcki.strigiformes.color;

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

    public static final TextFormat[] VALUES = values();

    @Getter
    private final String code;

    TextFormat(String code) {
        this.code = code;
    }

    public static TextFormat getFormat(String code) {
        //Parse simple legacy formatting code
        if (code.startsWith("§")) {
            code = code.substring(1); //Remove leading §
            for (TextFormat textFormat : VALUES)
                if (textFormat.getCode().equalsIgnoreCase(code))
                    return textFormat;
            return null;
        }
        //Parse color-tag
        if (code.startsWith("§{")) {
            String format = code.substring(2, code.length() - 2);
            return valueOf(format.toUpperCase());
        }
        return null;
    }
}
