package at.rxcki.strigiformes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import at.rxcki.strigiformes.parser.Parser;
import at.rxcki.strigiformes.text.TextProvider;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class MessageProvider {

    @Getter
    private final TextProvider textProvider;

    @Getter
    private Parser parser;

    public MessageProvider(TextProvider textProvider) {
        this(textProvider, new Parser());
    }

    public MessageProvider(TextProvider textProvider, Parser parser) {
        this.textProvider = textProvider;
        this.parser = parser;
    }

    public Message getMessage(String key, Locale locale, Object... arguments) {
        String rawString = textProvider.format(key, locale, arguments);

        return parser.parse(rawString);
    }

    public Message getMessage(String key, Locale locale) {
        String rawString = textProvider.getString(key, locale);

        return parser.parse(rawString);
    }

    public MessageCache getCache(String key, Object... arguments) {
        return new MessageCache(this, key, arguments);
    }
}
