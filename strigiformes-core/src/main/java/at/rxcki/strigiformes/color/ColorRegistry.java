package at.rxcki.strigiformes.color;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.Color;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public final class ColorRegistry {

    public static final Pattern HEX_PATTERN = Pattern.compile("#[0-9a-fA-F]{6}");

    private static final Map<String, Color> colors = new HashMap<>();
    private static final Map<Color, String> legacyColors = new HashMap<>();

    public static boolean useLegacyColors = false;

    static {
        addColor("0", new Color(0));
        addColor("black", new Color(0), true);
        addColor("1", new Color(170));
        addColor("dark_blue", new Color(170), true);
        addColor("2", new Color(43520));
        addColor("dark_green", new Color(43520), true);
        addColor("3", new Color(43690));
        addColor("dark_aqua", new Color(43690), true);
        addColor("4", new Color(11141120));
        addColor("dark_red", new Color(11141120), true);
        addColor("5", new Color(11141290));
        addColor("dark_purple", new Color(11141290), true);
        addColor("6", new Color(16755200));
        addColor("gold", new Color(16755200), true);
        addColor("7", new Color(11184810));
        addColor("gray", new Color(11184810), true);
        addColor("8", new Color(5592405));
        addColor("dark_gray", new Color(5592405), true);
        addColor("9", new Color(5592575));
        addColor("blue", new Color(5592575), true);
        addColor("a", new Color(5635925));
        addColor("green", new Color(5635925), true);
        addColor("b", new Color(5636095));
        addColor("aqua", new Color(5636095), true);
        addColor("c", new Color(16733525));
        addColor("red", new Color(16733525), true);
        addColor("d", new Color(16733695));
        addColor("light_purple", new Color(16733695), true);
        addColor("e", new Color(16777045));
        addColor("yellow", new Color(16777045), true);
        addColor("f", new Color(16777215));
        addColor("white", new Color(16777215), true);
    }

    public static Color getColor(String name) {
        return colors.getOrDefault(name.toLowerCase(Locale.ROOT), Color.getColor(name));
    }

    public static void addColor(String name, Color color) {
        addColor(name, color, false);
    }

    public static void addColor(String name, Color color, boolean legacy) {
        if (colors.containsKey(name.toLowerCase(Locale.ROOT))) {
            throw new IllegalStateException("The color " + name + " is already defined!");
        }
        colors.putIfAbsent(name.toLowerCase(Locale.ROOT), color);

        if (legacy)
            legacyColors.put(color, name);
    }

    public static String getLegacyColor(Color color) {
        return legacyColors.get(color);
    }

    public static Color parse(String value) {
        if (useLegacyColors && value.charAt(0) != '§')
            throw new IllegalArgumentException("When using legacy colors the usage of the colortag is forbidden");

        if (value.startsWith("§{")) {
            value = value.substring(2, value.length() - 1);
        }

        //Check if value is a hex-value
        if (HEX_PATTERN.matcher(value).matches()) {
            //Substring to delete #
            return Color.decode(value.substring(1));
        }
        return ColorRegistry.getColor(value.startsWith("§") ? value.substring(1) : value);
    }

    @Getter
    @RequiredArgsConstructor
    static class ColorRegistryEntry {
        private final String name;
        private final Color color;
        private final boolean legacy;
    }
}
