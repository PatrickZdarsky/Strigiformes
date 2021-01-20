package at.rxcki.strigiformes.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClickEvent {

    private ClickAction clickAction;
    private String value;

    public enum ClickAction {
        OPEN_URL,
        OPEN_FILE,
        RUN_COMMAND,
        SUGGEST_COMMAND,
        CHANGE_PAGE,
        COPY_TO_CLIPBOARD;

        public static final ClickAction[] VALUES = values();

        public static ClickAction getAction(String name) {
            for (ClickAction clickAction : VALUES)
                if (clickAction.name().equalsIgnoreCase(name))
                    return clickAction;
            return null;
        }
    }
}