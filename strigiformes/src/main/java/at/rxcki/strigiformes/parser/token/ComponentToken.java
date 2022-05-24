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
import java.util.List;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ComponentToken extends BaseToken {

    @Getter
    private final List<BaseToken> children;

    public ComponentToken(BaseToken parent, int index) {
        super(parent, index);
        children = new ArrayList<>();
    }

    @Override
    public void addChildren(BaseToken baseToken) {
        if (baseToken instanceof ComponentToken) {
            throw new TokenizerException("Tried to add ComponentTag as children for a ComponentTag");
        }

        children.add(baseToken);
    }

    public void normalizeIndices() {
        int offset = index;
        index = 0;
        end = end - offset;

        for (BaseToken baseToken : children) {
            baseToken.index -= offset;
            baseToken.end -= offset;
        }
    }

    @Override
    public String toString() {
        return "ComponentToken{" +
                "children=" + children +
                ", super=" + super.toString() + "}";
    }
}
