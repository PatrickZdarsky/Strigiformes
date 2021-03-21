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

import lombok.Getter;
import lombok.NonNull;
import at.rxcki.strigiformes.exception.ParserException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Getter
public class VariableTag {

    private final static Pattern VARIABLE_COMPONENT_BRACE_PATTERN = Pattern.compile("^\\$\\{(\\w*:)?(\\w*)}$");

    private final String namespace;
    private final String name;

    public VariableTag(String namespace, @NonNull String name) {
        this.namespace = namespace;
        this.name = name;
    }

    public VariableTag(@NonNull String name) {
        this.name = name;
        this.namespace = null;
    }

    @Override
    public String toString() {
        return "VariableComponent{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariableTag)) return false;
        VariableTag that = (VariableTag) o;
        return Objects.equals(namespace, that.namespace) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name);
    }

    public static VariableTag parse(String s) {
        Matcher matcher = VARIABLE_COMPONENT_BRACE_PATTERN.matcher(s);
        if (!matcher.find())
            return null;

        String namespace = matcher.group(1);
        if (namespace != null) {
            //Remove trailing : from capture-group
            namespace = namespace.substring(0, namespace.length() - 1);
        }

        String name = matcher.group(2);
        if (name == null)
            throw new ParserException("Could not retrieve variable name from variable tag");

        return new VariableTag(namespace, name);
    }
}
