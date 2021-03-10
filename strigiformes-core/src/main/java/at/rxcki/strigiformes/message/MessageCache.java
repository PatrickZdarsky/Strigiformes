package at.rxcki.strigiformes.message;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.text.TextData;
import lombok.Getter;
import lombok.Setter;

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
    private final TextData textData;

    private final Map<Locale, Message> cache = new HashMap<>(5);
    @Getter @Setter
    private boolean enableCache = true;

    public MessageCache(MessageProvider messageProvider, String key, Object... arguments) {
        this.messageProvider = messageProvider;
        this.textData = new TextData(key, arguments);
    }

    public MessageCache(MessageProvider messageProvider, TextData textData) {
        this.messageProvider = messageProvider;
        this.textData = textData;
    }

    public Message getMessage(Locale locale) {
        if (enableCache)
            return cache.computeIfAbsent(locale,
                    (l) -> messageProvider.getMessage(textData.getKey(), locale, textData.getArguments()));
        else
            return messageProvider.getMessage(textData.getKey(), locale, textData.getArguments());
    }
}
