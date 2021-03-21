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

package at.rxcki.strigiformes.parser.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import at.rxcki.strigiformes.exception.TokenizerException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 *
 * This class is part of the terrible workaround to make the MessageFormat work...
 */
public class MessageFormatTokenizer {

    //Extract MessageFormat tokens but ignore our tokens
    public static final Pattern TOKEN_EXTRACTOR = Pattern.compile("(?<![§$%\\\\])\\{|}");

    public static List<FormatToken> tokenize(String input) {
        List<FormatToken> tokens = new ArrayList<>();
        FormatToken activeToken = null;
        int depth = 0;

        Iterator<MatchResult> tokenIterator = TOKEN_EXTRACTOR.matcher(input).results().iterator();
        while (tokenIterator.hasNext()) {
            MatchResult matchResult = tokenIterator.next();

            if (matchResult.group().equals("}")) {
                if (activeToken == null) {
                    //Ignore since this could very likely be the closing bracket of one of our tokens
                    continue;
                }
                depth--;
                if (depth > 0)
                    continue;

                //Close the active token
                activeToken.setEnd(matchResult.start() + 1);
                tokens.add(activeToken);
                activeToken = null;

            } else {
                depth++;

                if (activeToken == null)
                    activeToken = new FormatToken(matchResult.start());
            }
        }
        return tokens;
    }

    @RequiredArgsConstructor
    public static class FormatToken {
        @Getter
        private final int index;
        @Getter @Setter
        private int end;

        @Override
        public String toString() {
            return "FormatToken{" +
                    "index=" + index +
                    ", end=" + end +
                    '}';
        }
    }

}
