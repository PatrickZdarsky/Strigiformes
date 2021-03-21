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

package at.rxcki.strigiformes.message;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.text.TextData;
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
        return addEntry(new TextData(key, arguments));
    }

    public CompoundMessageCache addEntry(TextData textData) {
        messageEntries.add(new CompoundMessageEntry(textData));

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
        return cache.computeIfAbsent(locale, locale1 -> messageProvider.getParser().parse(generateMessage(locale1)));
    }

    public String generateMessage(Locale locale) {
        var compound = new StringBuilder();

        for (CompoundMessageEntry entry : messageEntries) {
            if (entry.getValue() != null)
                compound.append(entry.getValue());
            else
                compound.append(messageProvider.getTextProvider().format(entry.getTextData(), locale));
        }

        return compound.toString();
    }


    @Getter
    private static class CompoundMessageEntry {
        private TextData textData;

        private String value;

        public CompoundMessageEntry(TextData textData) {
            this.textData = textData;
        }

        public CompoundMessageEntry(String value) {
            this.value = value;
        }
    }
}
