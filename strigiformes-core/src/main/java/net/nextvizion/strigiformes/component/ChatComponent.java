package net.nextvizion.strigiformes.component;

import lombok.Getter;
import lombok.Setter;
import net.nextvizion.strigiformes.text.ColoredText;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ChatComponent {
    @Getter
    private List<ColoredText> textList = new ArrayList<>();

    /**
     * Marks if this chat-component was generated or if it was explicitly created
     */
    @Getter @Setter
    private boolean generated;


    /**
     * This parse method can parse a single declared chatcomponent or a normal color-coded text
     * @param input
     * @return
     */
    public static ChatComponent parse(String input) {
        return null;
    }
}
