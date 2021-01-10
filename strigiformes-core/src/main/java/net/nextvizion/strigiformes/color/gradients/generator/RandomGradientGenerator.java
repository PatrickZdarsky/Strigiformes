package net.nextvizion.strigiformes.color.gradients.generator;

import net.nextvizion.strigiformes.color.gradients.GradientGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class RandomGradientGenerator implements GradientGenerator {
    @Override
    public List<Color> getGradient(String string, Color from, Color to) {
        List<Color> colors = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < string.length(); i++) {
            colors.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }

        return colors;
    }
}
