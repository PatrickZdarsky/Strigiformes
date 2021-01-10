package net.nextvizion.strigiformes.text;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.nextvizion.strigiformes.color.GradientRegistry;
import org.json.JSONObject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
