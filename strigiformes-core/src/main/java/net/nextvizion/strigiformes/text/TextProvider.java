package net.nextvizion.strigiformes.text;

import lombok.Getter;
import lombok.NonNull;
import net.nextvizion.strigiformes.cache.ILocaleCache;
import net.nextvizion.strigiformes.cache.MapLocaleCache;
import net.nextvizion.strigiformes.parser.VariableTag;
import net.nextvizion.strigiformes.parser.token.BaseToken;
import net.nextvizion.strigiformes.parser.token.Tokenizer;
import net.nextvizion.strigiformes.parser.token.VariableToken;

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
    }

    public TextProvider(@NonNull String namespace, @NonNull ILocaleCache cache) {
        this.namespace = namespace;
        this.localeCache = cache;
    }

    public String getString(String key, Locale locale) {
        var s = localeCache.getLocaledString(locale, key, () -> resolveString(key, locale));

        //Resolve variables
        //Todo: Check for StackOverflowErrors

        //TOdo: Cache tokenizer
        var tokenizer = new Tokenizer(s);
        tokenizer.tokenize();

        for (BaseToken baseToken : tokenizer.getTokens()) {
            if (!(baseToken instanceof VariableToken))
                continue;

            var variableTag = VariableTag.parse(s.substring(baseToken.getIndex(), baseToken.getEnd() + 1));
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


    protected abstract String resolveString(String key, Locale locale);
}
