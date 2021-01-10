package net.nextvizion.strigiformes.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoverEvent {

    private HoverAction hoverAction;
    private List<ChatComponent> components;

    public enum HoverAction {
        SHOW_TEXT
    }
}
