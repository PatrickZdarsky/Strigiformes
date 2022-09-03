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

import at.rxcki.strigiformes.component.ChatComponent;
import lombok.Getter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Getter
public class Message {

    private final List<ChatComponent> components;

    public Message(List<ChatComponent> components) {
        this.components = components;
    }

    public Message() {
        components = new ArrayList<>();
    }

    public Message addComponent(ChatComponent component) {
        components.add(component);

        return this;
    }

    public JSONArray toJson() {
        JSONArray array = new JSONArray();
        for (ChatComponent component : components)
            array.putAll(component.toJson());

        return array;
    }


    @Override
    public String toString() {
        return toJson().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(components, message.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }
}
