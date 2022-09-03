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
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Tokenizer implements Iterable<BaseToken> {
    public static final Pattern TOKEN_EXTRACTOR = Pattern.compile("[§$%]\\{|}|§[0-9a-fA-Fk-oK-OrR]");

    @Getter
    private final List<BaseToken> tokens;

    private final int textLength;

    public Tokenizer(String input) {
        tokens = new ArrayList<>();
        textLength = input.length();

        tokenize(input);
    }

    private void tokenize(String input) {
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
    }

    /**
     * Used to group ColorTokens into TokenGroups
     * Todo: Maybe leave single Colortokens alone in order to reduce waste object creations
     *
     * Example:
     *   |ColorToken|ColorToken|ColorToken|ComponentToken|ComponentToken|ColorToken|ComponentToken|ColorToken|
     * is collected into:
     *   |TokenGroup|ComponentToken|ComponentToken|TokenGroup|ComponentToken|TokenGroup|
     *
     * This operation modifies the existing tokens list, therefore is not creating an additional array.
     * While grouping the tokens it is also keeping track of the positions of the tokens in the input text
     * in order to re-use that data down the line
     */
    public void groupTokens() {
        TokenGroup currentGroup = null;

        int locationInText = 0;
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);

            if (token instanceof ComponentToken) {
                if (currentGroup != null) {
                    currentGroup.setEnd(token.getIndex());
                    tokens.add(i++, currentGroup);
                    currentGroup = null;
                }

                locationInText = token.getEnd();
                continue;
            }

            if (currentGroup == null)
                currentGroup = new TokenGroup(locationInText);

            currentGroup.addToken(token);

            //Remove the token from the list and reduce the index of the loop to not skip the next element
            tokens.remove(i--);
        }

        //If there is a group which has not been closed by a component => add it
        if (currentGroup != null) {
            currentGroup.setEnd(textLength);
            tokens.add(currentGroup);
        }
    }

    @Override
    public Iterator<BaseToken> iterator() {
        return tokens.iterator();
    }
}
