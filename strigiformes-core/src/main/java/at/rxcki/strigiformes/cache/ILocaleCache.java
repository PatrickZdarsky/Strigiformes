package at.rxcki.strigiformes.cache;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public interface ILocaleCache {

    String getLocaledString(Locale locale, String key, Supplier<String> supplier);

}
