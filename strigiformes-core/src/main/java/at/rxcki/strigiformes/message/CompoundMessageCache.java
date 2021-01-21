package at.rxcki.strigiformes.message;

import at.rxcki.strigiformes.MessageProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Patrick Zdarsky / Rxcki
 *
 * Using this cache one can send messages composed of multiple translation strings to many different players
 */
@RequiredArgsConstructor
public class CompoundMessageCache implements IMessageCache{

    private final MessageProvider messageProvider;
    private final List<CompoundMessageEntry> messageEntries = new ArrayList<>();

    private final Map<Locale, Message> cache = new HashMap<>(5);


    public CompoundMessageCache addEntry(String key, Object... arguments) {
        messageEntries.add(new CompoundMessageEntry(key, arguments));

        return this;
    }

    public CompoundMessageCache addSimpleEntry(String message) {
        messageEntries.add(new CompoundMessageEntry(message));

        return this;
    }

    public CompoundMessageCache clearCache() {
        cache.clear();

        return this;
    }

    public Message getMessage(Locale locale) {
        return cache.computeIfAbsent(locale, this::generateMessage);
    }

    private Message generateMessage(Locale locale) {
        var compound = new StringBuilder();

        for (CompoundMessageEntry entry : messageEntries) {
            if (entry.getValue() != null)
                compound.append(entry.getValue());
            else
                compound.append(
                        messageProvider.getTextProvider().format(entry.getKey(), locale, entry.getArguments()));
        }

        return messageProvider.getParser().parse(compound.toString());
    }


    @Getter
    private static class CompoundMessageEntry {
        private String key;
        private Object[] arguments;

        private String value;

        public CompoundMessageEntry(String key, Object[] arguments) {
            this.key = key;
            this.arguments = arguments;
        }

        public CompoundMessageEntry(String value) {
            this.value = value;
        }
    }
}
