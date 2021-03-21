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
