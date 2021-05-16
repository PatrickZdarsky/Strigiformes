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

import at.rxcki.strigiformes.exception.TokenizerException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Tokenizer {

    public static final Pattern TOKEN_EXTRACTOR = Pattern.compile("[§$%]\\{|}|§[0-9a-fA-Fk-oK-OrR]");

    public static List<BaseToken> tokenize(String input) {
        List<BaseToken> tokens = new ArrayList<>();
        BaseToken activeToken = null;

        Iterator<MatchResult> tokenIterator = TOKEN_EXTRACTOR.matcher(input).results().iterator();
        while (tokenIterator.hasNext()) {
            MatchResult matchResult = tokenIterator.next();
            String result = matchResult.group();

            if (matchResult.group().equals("}")) {
                if (activeToken == null) {
                    throw new TokenizerException("Found closing bracket without opening one '"+input+"' at index "+matchResult.start());
                }
                //Close the active token
                activeToken.setEnd(matchResult.start()+1);
                activeToken = activeToken.getParent();

                //Legacy color-code logic
            } else if (result.charAt(0) == '§'
                    && matchResult.group().length() == 2
                    && result.charAt(result.length()-1) != '{') {
                ColorToken token = new ColorToken(null, matchResult.start());
                token.setEnd(matchResult.end());

                if (activeToken != null) {
                    activeToken.addChildren(token);
                    continue;
                }
                tokens.add(token);
            } else {
                char indicator = matchResult.group().substring(0, 1).charAt(0);
                if (activeToken == null) {
                    activeToken = TokenFactory.getToken(indicator, null, matchResult.start());
                    tokens.add(activeToken);
                    continue;
                }

                //Move one deeper
                BaseToken childToken = TokenFactory.getToken(indicator, activeToken, matchResult.start());
                activeToken.addChildren(childToken);
                activeToken = childToken;
            }
        }
        return tokens;
    }
}
