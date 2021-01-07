package net.nextvizion.strigiformes.text;

import net.nextvizion.strigiformes.color.GradientType;

import java.awt.Color;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GradientText extends ColoredText {

    private Color endColor;
    private GradientType gradientType;

    public GradientText(Color color, String text) {
        super(color, text);
    }


}
