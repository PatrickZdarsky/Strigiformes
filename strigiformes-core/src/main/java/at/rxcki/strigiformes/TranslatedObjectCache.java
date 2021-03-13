package at.rxcki.strigiformes;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@RequiredArgsConstructor
public class TranslatedObjectCache<T> {

    private final Function<Locale, T> supplier;

    private final Map<Locale, T> cache = new HashMap<>(5);

    public T get(Locale locale) {
        return cache.computeIfAbsent(locale, supplier);
    }

    public void clearCache() {
        cache.clear();
    }

    public void invalidate(Locale locale) {
        cache.remove(locale);
    }

    public Map<Locale, T> asMap() {
        return cache;
    }
}
