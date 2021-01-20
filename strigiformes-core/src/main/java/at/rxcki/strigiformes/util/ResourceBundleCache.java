package at.rxcki.strigiformes.util;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ResourceBundleCache {

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
			resourceBundle = ResourceBundle.getBundle(baseName, locale, classLoader, new Utf8Control());
		}

		return resourceBundle;
	}
}
