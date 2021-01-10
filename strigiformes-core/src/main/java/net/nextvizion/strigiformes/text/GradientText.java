package net.nextvizion.strigiformes.text;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.nextvizion.strigiformes.color.GradientType;

import java.awt.Color;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Data
@NoArgsConstructor
public class GradientText extends ColoredText {

    private Color endColor;
    private GradientType gradientType;

    public GradientText(Color color, String text) {
        super(color, text);
    }

    @Override
    public String toString() {
        return "GradientText{" +
                "endColor=" + endColor +
                ", gradientType=" + gradientType +
                ", super=" + super.toString() + "}";
    }
}
