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

package at.rxcki.strigiformes.text;

import lombok.Getter;
import lombok.Setter;
import at.rxcki.strigiformes.cache.NOPCache;
import at.rxcki.strigiformes.util.ResourceBundleCache;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

@Getter
public class ResourceBundleTextProvider extends TextProvider {

	protected static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{\\w+}");

	private final Map<Locale, ResourceBundleCache> loadedLocales;
	private final ClassLoader classLoader;

	@Setter
	private Locale defaultLocale = Locale.getDefault();

	public ResourceBundleTextProvider(ClassLoader classLoader, String namespace) {
		super(namespace, new NOPCache());
		loadedLocales = new HashMap<>();
		this.classLoader = classLoader;
	}

	@Override
	protected String resolveString0(String key, Locale locale) {
		if (!loadedLocales.containsKey(locale)) {
			if (Objects.isNull(getDefaultLocale())) {
				throw new IllegalStateException("No default locale has been set");
			}

			//Fallback to default locale
			locale = getDefaultLocale();
		}

		ResourceBundle resourceBundle = loadedLocales.get(locale).getResourceBundle();
		if (Objects.isNull(resourceBundle)) {
			throw new IllegalStateException("Default locale isn't loaded");
		}

		return resourceBundle.getString(key);
	}

	public void addResourceBundle(Locale locale) {
		ResourceBundleCache resourceBundleCache = new ResourceBundleCache(getNamespace(), locale,
				classLoader);
		loadedLocales.put(locale, resourceBundleCache);
	}
}