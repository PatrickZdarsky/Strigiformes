package net.nextvizion.strigiformes.cache;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class MapLocaleCache implements ILocaleCache {

    protected Map<Locale, Map<String, String>> localeCache = new HashMap<>();

    @Override
    public String getLocaledString(Locale locale, String key, Supplier<String> supplier) {
        Map<String, String> keyMap = localeCache.computeIfAbsent(locale, k -> new HashMap<>());

        return keyMap.computeIfAbsent(key, s1 -> supplier.get());
    }
}
