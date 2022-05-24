/*
 *  This file is part of Strigiformes, licensed under the MIT License.
 *
 *   Copyright (c) 2021 Patrick Zdarsky
 *   Copyright (c) contributors
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package at.rxcki.strigiformes.color;

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

    public ColoredText(Color color) {
        this.color = color;
    }

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
        JSONObject json = new JSONObject();
        json.put("text", getText());
        if (getColor() != null) {
            if (ColorRegistry.useLegacyColors) {
                json.put("color", ColorRegistry.getLegacyColor(getColor()));
            } else {
                json.put("color", String.format("#%06x", getColor().getRGB() & 0x00FFFFFF));
            }
        }

        if (getFormats() != null)
            for (TextFormat textFormat : getFormats())
                json.put(textFormat.name().toLowerCase(), true);

        return json;
    }
}
