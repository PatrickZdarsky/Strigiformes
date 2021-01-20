package net.nextvizion.strigiformes.text;

import lombok.Getter;
import lombok.NonNull;
import net.nextvizion.strigiformes.cache.ILocaleCache;
import net.nextvizion.strigiformes.cache.MapLocaleCache;
import net.nextvizion.strigiformes.parser.VariableTag;
import net.nextvizion.strigiformes.parser.token.BaseToken;
import net.nextvizion.strigiformes.parser.token.MessageFormatTokenizer;
import net.nextvizion.strigiformes.parser.token.Tokenizer;
import net.nextvizion.strigiformes.parser.token.VariableToken;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class TextProvider {

    @Getter
    private final String namespace;
    protected final ILocaleCache localeCache;

    public TextProvider(@NonNull String namespace) {
        this.namespace = namespace;
        localeCache = new MapLocaleCache();
        TextProviderRegistry.registerProvider(this);
    }

    public TextProvider(@NonNull String namespace, @NonNull ILocaleCache cache) {
        this.namespace = namespace;
        this.localeCache = cache;
        TextProviderRegistry.registerProvider(this);
    }


    protected abstract String resolveString0(String key, Locale locale);

    String resolveString(String key, Locale locale) {
        int dottedIndex = key.indexOf(':');
        if (dottedIndex >= 0) {
            String namespace = key.substring(0, dottedIndex);
            String name = key.substring(dottedIndex+1);

            //Todo: Check if a provider with this namespace exists
            return TextProviderRegistry.getProviderByNamespace(namespace).resolveString(name, locale);
        }

        return localeCache.getLocaledString(locale, key, () -> resolveString0(key, locale));
    }


    public String getString(String key, Locale locale) {
        var s = resolveString(key, locale);

        return resolveVariables(locale, s);
    }

    public String format(String key, Locale locale, Object... arguments) {
        var s = localeCache.getLocaledString(locale, key, () -> resolveString0(key, locale));
        s =  applyMessageFormat(s, locale, arguments);
        return resolveVariables(locale, s);
    }

    private String resolveVariables(Locale locale, String s) {
        //Resolve variables
        //Todo: Check for StackOverflowErrors

        //Todo: Cache tokenizer result
        var tokens = Tokenizer.tokenize(s);
        for (BaseToken baseToken : tokens) {
            if (!(baseToken instanceof VariableToken))
                continue;

            var variableTag = VariableTag.parse(s.substring(baseToken.getIndex(), baseToken.getEnd()));
            if (variableTag == null)
                continue;   //Todo add debug logging?

            if (variableTag.getNamespace() == null || variableTag.getNamespace().equalsIgnoreCase(namespace)) {
                s = replace(s, baseToken.getIndex(), baseToken.getEnd(), getString(variableTag.getName(), locale));
            } else {
                var textProvider = TextProviderRegistry.getProviderByNamespace(variableTag.getNamespace());
                if (textProvider == null)
                    continue; //Todo add debug logging?
                s = replace(s, baseToken.getIndex(), baseToken.getEnd(), textProvider.getString(variableTag.getName(), locale));
            }
        }

        return s;
    }

    private static String replace(String string, int index, int endindex, String replacement) {
        String newString = string.substring(0, index);
        newString += replacement;
        newString += string.substring(endindex);

        return newString;
    }


    /**
     * Since MessageFormat also uses curly brackets for their tags it is clashing with our system.
     * As a quick workaround we extract the tags that are meant for MessageFormat and feed them to it one-by-one.
     * This has the disadvantage that we have to create a new MessageFormat object for each tag
     *
     * A better solution for this is needed!
     */
    private static String applyMessageFormat(String s, Locale locale, Object... arguments) {
        if (arguments.length == 0)
            return s;

        for (MessageFormatTokenizer.FormatToken formatToken : MessageFormatTokenizer.tokenize(s)) {
            if (formatToken.getEnd() == 0)
                continue; //Just ignore it,(this should be handled.... ---> for the future dev)

            String part = s.substring(formatToken.getIndex(), formatToken.getEnd());

            var formatted = new MessageFormat(part, locale).format(arguments);
            s = replace(s, formatToken.getIndex(), formatToken.getEnd(), formatted);
        }

        return s;
    }
}
