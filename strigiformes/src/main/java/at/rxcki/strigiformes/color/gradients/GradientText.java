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

package at.rxcki.strigiformes.color.gradients;

import lombok.Data;
import lombok.NoArgsConstructor;
import at.rxcki.strigiformes.color.ColoredText;
import at.rxcki.strigiformes.color.TextFormat;
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
            ColoredText text = new ColoredText();
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
