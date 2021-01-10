package net.nextvizion.strigiformes.color;

import net.nextvizion.strigiformes.color.gradients.LinearRgbGradientGenerator;
import net.nextvizion.strigiformes.color.gradients.RandomGradientGenerator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public final class GradientRegistry {

    private static Map<String, GradientGenerator> gradientGenerators = new HashMap<>();

    static {
        addGenerator("linear", new LinearRgbGradientGenerator());
        addGenerator("random", new RandomGradientGenerator());
    }

    public static GradientGenerator getGenerator(String name) {
        return gradientGenerators.get(name.toLowerCase(Locale.ROOT));
    }

    public static void addGenerator(String name, GradientGenerator color) {
        if (gradientGenerators.containsKey(name.toLowerCase(Locale.ROOT))) {
            throw new IllegalStateException("The generator " + name + " is already defined!");
        }
        gradientGenerators.put(name.toLowerCase(Locale.ROOT), color);
    }

}
