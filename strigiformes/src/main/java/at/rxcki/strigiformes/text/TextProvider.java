/*
 *  This file is part of Strigiformes, licensed under the MIT License.
 *
 *   Copyright (c) 2021 Patrick Zdarsky
 *   Copyright (c) contributors
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package at.rxcki.strigiformes.text;

import at.rxcki.strigiformes.cache.ILocaleCache;
import at.rxcki.strigiformes.cache.MapLocaleCache;
import at.rxcki.strigiformes.parser.VariableTag;
import at.rxcki.strigiformes.parser.token.BaseToken;
import at.rxcki.strigiformes.parser.token.MessageFormatTokenizer;
import lombok.Getter;
import lombok.NonNull;
import at.rxcki.strigiformes.parser.token.Tokenizer;
import at.rxcki.strigiformes.parser.token.VariableToken;

import java.text.MessageFormat;
import java.util.List;
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

    String resolveString(@NonNull String key, @NonNull Locale locale) {
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
        String s = resolveString(key, locale);

        return resolveVariables(locale, s);
    }

    public String format(String key, Locale locale, Object... arguments) {
        String s = localeCache.getLocaledString(locale, key, () -> resolveString(key, locale));
        s =  applyMessageFormat(s, locale, arguments);

        return resolveVariables(locale, s);
    }

    public String format(TextData textData, Locale locale) {
        return format(textData.getKey(), locale, textData.getArguments());
    }

    private String resolveVariables(Locale locale, String s) {
        //Resolve variables
        //Todo: Check for StackOverflowErrors

        //Todo: Cache tokenizer result
        List<BaseToken> tokens = Tokenizer.tokenize(s);
        for (BaseToken baseToken : tokens) {
            if (!(baseToken instanceof VariableToken))
                continue;

            VariableTag variableTag = VariableTag.parse(s.substring(baseToken.getIndex(), baseToken.getEnd()));
            if (variableTag == null)
                continue;   //Todo add debug logging?

            if (variableTag.getNamespace() == null || variableTag.getNamespace().equalsIgnoreCase(namespace)) {
                s = replace(s, baseToken.getIndex(), baseToken.getEnd(), getString(variableTag.getName(), locale));
            } else {
                TextProvider textProvider = TextProviderRegistry.getProviderByNamespace(variableTag.getNamespace());
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

    public static String applyMessageFormat(String s, Locale locale, Object... arguments) {
        if (arguments.length == 0)
            return s;

        //We need this offset because if we are replacing the MessageFormat tokens,
        // the length of the initial token may not be the same as the inserted string
        var offset = 0;
        for (MessageFormatTokenizer.FormatToken formatToken : MessageFormatTokenizer.tokenize(s)) {
            if (formatToken.getEnd() == 0)
                continue; //Just ignore it,(this should be handled.... ---> for the future dev)

            String part = s.substring(formatToken.getIndex()+offset, formatToken.getEnd()+offset);

            String formatted = new MessageFormat(part, locale).format(arguments);
            s = replace(s, formatToken.getIndex()+offset, formatToken.getEnd()+offset, formatted);
            offset += formatted.length()-part.length();
        }

        return s;
    }
}
