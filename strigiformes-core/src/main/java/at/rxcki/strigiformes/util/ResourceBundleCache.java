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

package at.rxcki.strigiformes.util;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ResourceBundleCache {

    private static final Utf8Control UTF_8_CONTROL = new Utf8Control();

	private final String baseName;
	private final Locale locale;
	private final ClassLoader classLoader;
	private ResourceBundle resourceBundle;

	public ResourceBundleCache(String baseName, Locale locale, ClassLoader classLoader) {
		this.baseName = baseName;
		this.locale = locale;
		this.classLoader = classLoader;
	}

	public ResourceBundle getResourceBundle() {
		if (Objects.isNull(resourceBundle)) {
			resourceBundle = ResourceBundle.getBundle(baseName, locale, classLoader, UTF_8_CONTROL);
		}

		return resourceBundle;
	}
}
