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

package at.rxcki.strigiformes;

import at.rxcki.strigiformes.message.Message;
import at.rxcki.strigiformes.message.MessageCache;
import lombok.Getter;
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
