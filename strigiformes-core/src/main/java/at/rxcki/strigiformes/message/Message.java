package at.rxcki.strigiformes.message;

import lombok.Getter;
import at.rxcki.strigiformes.component.ChatComponent;
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
