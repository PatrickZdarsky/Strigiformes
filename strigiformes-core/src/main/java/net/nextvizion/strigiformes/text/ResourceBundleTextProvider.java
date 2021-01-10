package net.nextvizion.strigiformes.text;

import lombok.Getter;
import lombok.Setter;
import net.nextvizion.strigiformes.cache.NOPCache;
import net.nextvizion.strigiformes.util.ResourceBundleCache;

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
public class ResourceBundleTextProvider extends TextProvider{

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
	protected String resolveString(String key, Locale locale) {
		if (!loadedLocales.containsKey(locale)) {
			if (Objects.isNull(getDefaultLocale())) {
				throw new IllegalArgumentException("No default locale has been set");
			}

			//Fallback to default locale
			locale = getDefaultLocale();
		}

		ResourceBundle resourceBundle = loadedLocales.get(locale).getResourceBundle();
		if (Objects.isNull(resourceBundle)) {
			throw new IllegalArgumentException("Default locale isn't loaded");
		}

		return resourceBundle.getString(key);
	}

	public ResourceBundleCache addResourceBundle(Locale locale) {
		ResourceBundleCache resourceBundleCache = new ResourceBundleCache(getNamespace(), locale,
				classLoader);
		loadedLocales.put(locale, resourceBundleCache);
		return resourceBundleCache;
	}
}