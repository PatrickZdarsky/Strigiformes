package net.nextvizion.strigiformes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@AllArgsConstructor
public class MessageCache {

    @Getter
    private final MessageProvider messageProvider;
    @Getter
    private final String key;
    @Getter
    private final Object[] arguments;


    private final Map<Locale, Message> cache = new HashMap<>(5);


    public Message getMessage(Locale locale) {
        return cache.computeIfAbsent(locale, (l) -> messageProvider.getMessage(key, locale, arguments));
    }
}
