package at.rxcki.strigiformes.cache;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author Patrick Zdarsky / Rxcki
 *
 * A simple no-operation cache implementation
 */
public class NOPCache implements ILocaleCache {

    @Override
    public String getLocaledString(Locale locale, String key, Supplier<String> supplier) {
        return supplier.get();
    }
}
