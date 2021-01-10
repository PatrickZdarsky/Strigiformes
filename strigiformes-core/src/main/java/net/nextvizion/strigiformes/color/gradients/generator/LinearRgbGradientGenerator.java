package net.nextvizion.strigiformes.color.gradients.generator;

import net.nextvizion.strigiformes.color.gradients.GradientGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class LinearRgbGradientGenerator implements GradientGenerator {
    @Override
    public List<Color> getGradient(String string, Color from, Color to) {
        double[] red = interpolate(from.getRed(), to.getRed(), string.length());
        double[] green = interpolate(from.getGreen(), to.getGreen(), string.length());
        double[] blue = interpolate(from.getBlue(), to.getBlue(), string.length());

        List<Color> colors = new ArrayList<>(red.length);
        for (int i = 0; i < string.length(); i++) {
            colors.add(new Color(
                    (int) Math.round(red[i]),
                    (int) Math.round(green[i]),
                    (int) Math.round(blue[i])));
        }

        return colors;
    }

    private static double[] interpolate(double from, double to, int max) {
        final double[] res = new double[max];
        for (int i = 0; i < max; i++) {
            res[i] = from + i * ((to - from) / (max - 1));
        }
        return res;
    }
}
