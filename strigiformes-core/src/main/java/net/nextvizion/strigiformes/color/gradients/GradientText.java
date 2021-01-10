package net.nextvizion.strigiformes.color.gradients;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.nextvizion.strigiformes.color.ColoredText;
import net.nextvizion.strigiformes.color.TextFormat;
import org.json.JSONObject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Data
@NoArgsConstructor
public class GradientText extends ColoredText {

    private Color endColor;
    private String gradientType;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GradientText)) return false;
        if (!super.equals(o)) return false;
        GradientText that = (GradientText) o;
        return Objects.equals(endColor, that.endColor) && Objects.equals(gradientType, that.gradientType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), endColor, gradientType);
    }

    @Override
    public JSONObject toJson() {
        throw new IllegalStateException();
    }

    public List<ColoredText> toColoredTexts() {
        List<Color> colors = GradientRegistry.getGenerator(gradientType).getGradient(getText(), getColor(), getEndColor());
        List<ColoredText> texts = new ArrayList<>();

        for (int i = 0; i < getText().length(); i++) {
            var text = new ColoredText();
            text.setText(String.valueOf(getText().charAt(i)));
            text.setColor(colors.get(i));
            if (getFormats() != null)
                for (TextFormat textFormat : getFormats())
                    text.addFormat(textFormat);
            texts.add(text);
        }
        return texts;
    }
}
