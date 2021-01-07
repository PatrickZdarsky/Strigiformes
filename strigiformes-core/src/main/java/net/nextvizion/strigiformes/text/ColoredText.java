package net.nextvizion.strigiformes.text;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ColoredText {

    @Getter @Setter
    private Color color;
    @Getter @Setter
    private String text;
    @Getter
    private List<TextFormat> formats;

    public ColoredText(String text) {
        this.text = text;
    }

    public ColoredText(Color color, String text) {
        this.color = color;
        this.text = text;
    }

    public void addFormat(TextFormat textFormat) {
        if (formats == null)
            formats = new ArrayList<>(2);
        if (!formats.contains(textFormat))
            formats.add(textFormat);
    }
}
