package net.nextvizion.strigiformes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.nextvizion.strigiformes.parser.Parser;
import net.nextvizion.strigiformes.text.TextProvider;

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

        Parser parser = new Parser(rawString);
        return parser.parse();
    }

    public Message getMessage(String key, Locale locale) {
        String rawString = textProvider.getString(key, locale);

        Parser parser = new Parser(rawString);
        return parser.parse();
    }
}
