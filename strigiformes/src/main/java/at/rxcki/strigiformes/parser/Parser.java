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

package at.rxcki.strigiformes.parser;

import at.rxcki.strigiformes.component.ChatComponent;
import at.rxcki.strigiformes.message.Message;
import at.rxcki.strigiformes.parser.token.BaseToken;
import at.rxcki.strigiformes.parser.token.ColorToken;
import at.rxcki.strigiformes.parser.token.ComponentToken;
import at.rxcki.strigiformes.parser.token.TokenGroup;
import at.rxcki.strigiformes.parser.token.Tokenizer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Parser {

    //At this point all variables are already resolved
    public Message parse(String input) {
        if (input.isEmpty())
            return null;

        Message message = new Message();

        Tokenizer tokenizer = new Tokenizer(input);
        tokenizer.groupTokens();
        //Position up to when we have parsed the input
        int index = 0;

        for (BaseToken baseToken : tokenizer) {
            //Parse text in front of component, this text does not contain other components
            if (baseToken.getIndex() > index) {
                String part = input.substring(index, baseToken.getIndex());

                message.getComponents().add(ChatComponent.parse(part, Collections.emptyList()));
            }

            //Parse component
            String part = input.substring(baseToken.getIndex(), baseToken.getEnd());
            List<ColorToken> containedTokens = null;
            if (baseToken instanceof ComponentToken) {
                ((ComponentToken) baseToken).normalizeIndices();
                containedTokens = ((ComponentToken) baseToken).getChildren().stream().map(ColorToken.class::cast).collect(Collectors.toList());
            } else if (baseToken instanceof TokenGroup) {
                ((TokenGroup) baseToken).normalizeIndices();
                containedTokens = ((TokenGroup) baseToken).getTokens().stream().map(ColorToken.class::cast).collect(Collectors.toList());
            }


            message.getComponents().add(ChatComponent.parse(part, containedTokens));

            index = baseToken.getEnd();
        }

        //Check if we are at the end of the input, if not -> parse it
        if (index < input.length()-1) {
            String part = input.substring(index);

            message.getComponents().add(ChatComponent.parse(part, Collections.emptyList()));
        }

        return message;
    }
}
