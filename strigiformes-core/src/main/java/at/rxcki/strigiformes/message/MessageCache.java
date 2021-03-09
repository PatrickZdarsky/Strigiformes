package at.rxcki.strigiformes.message;

import at.rxcki.strigiformes.MessageProvider;
import lombok.Getter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class MessageCache implements IMessageCache {

    @Getter
    private final MessageProvider messageProvider;
    @Getter
    private final String key;
    @Getter
    private final Object[] arguments;

    private final Map<Locale, Message> cache = new HashMap<>(5);

    public MessageCache(MessageProvider messageProvider, String key, Object... arguments) {
        this.messageProvider = messageProvider;
        this.key = key;
        this.arguments = arguments;
    }

    public Message getMessage(Locale locale) {
        return cache.computeIfAbsent(locale, (l) -> messageProvider.getMessage(key, locale, arguments));
    }
}
