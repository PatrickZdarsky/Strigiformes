package at.rxcki.strigiformes.component;

import lombok.Data;
import lombok.NoArgsConstructor;
import at.rxcki.strigiformes.color.ColoredText;

import java.util.List;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Data
@NoArgsConstructor
public class HoverEvent {

    private HoverAction hoverAction;
    private List<ColoredText> text;

    public HoverEvent(HoverAction hoverAction) {
        this.hoverAction = hoverAction;
    }

    public enum HoverAction {

        SHOW_TEXT;

        public static final HoverAction[] VALUES = values();

        public static HoverAction getAction(String name) {
            for (HoverAction hoverAction : VALUES)
                if (hoverAction.name().equalsIgnoreCase(name))
                    return hoverAction;
            return null;
        }
    }
}
