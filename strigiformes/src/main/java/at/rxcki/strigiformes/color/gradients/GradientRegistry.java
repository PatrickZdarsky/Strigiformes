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

import at.rxcki.strigiformes.color.gradients.generator.LinearRgbGradientGenerator;
import at.rxcki.strigiformes.color.gradients.generator.RandomGradientGenerator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public final class GradientRegistry {

    private static final Map<String, GradientGenerator> gradientGenerators = new HashMap<>();

    static {
        addGenerator("linear", new LinearRgbGradientGenerator());
        addGenerator("random", new RandomGradientGenerator());
    }

    public static GradientGenerator getGenerator(String name) {
        return gradientGenerators.get(name.toLowerCase(Locale.ROOT));
    }

    public static void addGenerator(String name, GradientGenerator color) {
        if (gradientGenerators.containsKey(name.toLowerCase(Locale.ROOT))) {
            throw new IllegalStateException("The generator " + name + " is already defined!");
        }
        gradientGenerators.put(name.toLowerCase(Locale.ROOT), color);
    }

}
