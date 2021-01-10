package net.nextvizion.strigiformes;

import lombok.Getter;
import net.nextvizion.strigiformes.component.ChatComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Getter
public class Message {

    private List<ChatComponent> components;

    public Message(List<ChatComponent> components) {
        this.components = components;
    }

    public Message() {
        components = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Message{" +
                "components=" + components +
                '}';
    }
}
