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

package at.rxcki.strigiformes.color.gradients.generator;

import at.rxcki.strigiformes.color.gradients.GradientGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class RandomGradientGenerator implements GradientGenerator {

    private final Random random = new Random();

    @Override
    public List<Color> getGradient(String string, Color from, Color to) {
        List<Color> colors = new ArrayList<>();

        for (int i = 0; i < string.length(); i++) {
            colors.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }

        return colors;
    }
}
