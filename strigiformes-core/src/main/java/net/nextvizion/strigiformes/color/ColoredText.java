package net.nextvizion.strigiformes.color;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@NoArgsConstructor
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

    public ColoredText addFormat(TextFormat textFormat) {
        if (formats == null)
            formats = new ArrayList<>(2);
        if (!formats.contains(textFormat))
            formats.add(textFormat);

        return this;
    }

    @Override
    public String toString() {
        return "ColoredText{" +
                "color=" + color +
                ", text='" + text + '\'' +
                ", formats=" + formats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColoredText)) return false;
        ColoredText that = (ColoredText) o;
        return Objects.equals(color, that.color) && Objects.equals(text, that.text) && Objects.equals(formats, that.formats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, text, formats);
    }

    public JSONObject toJson() {
        var json = new JSONObject();
        json.put("text", getText());
        if (getColor() != null)
            json.put("color", String.format("#%06x", getColor().getRGB() & 0x00FFFFFF));

        if (getFormats() != null)
            for (TextFormat textFormat : getFormats())
                json.put(textFormat.name().toLowerCase(), true);

        return json;
    }
}
