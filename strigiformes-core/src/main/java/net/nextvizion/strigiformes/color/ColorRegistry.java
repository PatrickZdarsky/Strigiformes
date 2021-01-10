package net.nextvizion.strigiformes.color;

import java.awt.Color;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ColorRegistry {

    public static final Pattern HEX_PATTERN = Pattern.compile("#[0-9a-fA-F]{6}");
    private static Map<String, Color> colors = new HashMap<>();


    static {
        addColor("0", new Color(0));
        addColor("black", new Color(0));
        addColor("1", new Color(170));
        addColor("dark_blue", new Color(170));
        addColor("2", new Color(43520));
        addColor("dark_green", new Color(43520));
        addColor("3", new Color(43690));
        addColor("dark_aqua", new Color(43690));
        addColor("4", new Color(11141120));
        addColor("dark_red", new Color(11141120));
        addColor("5", new Color(11141290));
        addColor("dark_purple", new Color(11141290));
        addColor("6", new Color(16755200));
        addColor("gold", new Color(16755200));
        addColor("7", new Color(11184810));
        addColor("gray", new Color(11184810));
        addColor("8", new Color(5592405));
        addColor("dark_gray", new Color(5592405));
        addColor("9", new Color(5592575));
        addColor("blue", new Color(5592575));
        addColor("a", new Color(5635925));
        addColor("green", new Color(5635925));
        addColor("b", new Color(5636095));
        addColor("aqua", new Color(5636095));
        addColor("c", new Color(16733525));
        addColor("red", new Color(16733525));
        addColor("d", new Color(16733695));
        addColor("light_purple", new Color(16733695));
        addColor("e", new Color(16777045));
        addColor("yellow", new Color(16777045));
        addColor("f", new Color(16777215));
        addColor("white", new Color(16777215));
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
        return ColorRegistry.getColor(value.startsWith("§") ? value.substring(1) : value);
    }
}
