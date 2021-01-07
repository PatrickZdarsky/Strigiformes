package net.nextvizion.strigiformes.color;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ColorRegistry {

    public static final Pattern HEX_PATTERN = Pattern.compile("#[0-9a-fA-F]{6}");
    private static Map<String, Color> colors;


    static {
        addColor("f", Color.WHITE);
        //Todo: add all color-codes
    }

    public static Color getColor(String name) {
        if (colors.containsKey(name.toLowerCase(Locale.ROOT)))
            return colors.get(name.toLowerCase(Locale.ROOT));
        return Color.getColor(name);
    }

    public static void addColor(String name, Color color) {
        if (colors.containsKey(name.toLowerCase(Locale.ROOT))) {
            throw new IllegalStateException("The color " + name + " is already defined!");
        }
        colors.put(name.toLowerCase(Locale.ROOT), color);
    }

    public static Color parse(String value) {
        //Check if value is a hex-value
        if (HEX_PATTERN.matcher(value).matches()) {
            //Substring to delete #
            return Color.decode(value.substring(1));
        }
        return ColorRegistry.getColor(value);
    }
}
