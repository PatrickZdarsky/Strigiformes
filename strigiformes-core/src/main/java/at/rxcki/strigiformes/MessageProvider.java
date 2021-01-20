package at.rxcki.strigiformes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import at.rxcki.strigiformes.parser.Parser;
import at.rxcki.strigiformes.text.TextProvider;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@RequiredArgsConstructor
public class MessageProvider {

    @Getter
    private final TextProvider textProvider;

    public Message getMessage(String key, Locale locale, Object... arguments) {
        String rawString = textProvider.format(key, locale, arguments);

        return Parser.parse(rawString);
    }

    public Message getMessage(String key, Locale locale) {
        String rawString = textProvider.getString(key, locale);

        return Parser.parse(rawString);
    }
}
