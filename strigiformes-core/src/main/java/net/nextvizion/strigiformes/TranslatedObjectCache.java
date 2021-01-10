package net.nextvizion.strigiformes;

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
}
