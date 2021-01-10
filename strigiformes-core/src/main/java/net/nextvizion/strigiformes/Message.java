package net.nextvizion.strigiformes;

import lombok.Getter;
import net.nextvizion.strigiformes.component.ChatComponent;
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

    public JSONArray toJson() {
        var array = new JSONArray();
        for (ChatComponent component : components)
            array.putAll(component.toJson());

        return array;
    }

    @Override
    public String toString() {
        return "Message{" +
                "components=" + components +
                '}';
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
