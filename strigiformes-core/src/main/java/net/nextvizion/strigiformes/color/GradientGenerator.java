package net.nextvizion.strigiformes.color;

import java.awt.Color;
import java.util.List;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public interface GradientGenerator {
    List<Color> getGradient(String string, Color from, Color to);
}
