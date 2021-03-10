package at.rxcki.strigiformes.text;

import lombok.Data;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Data
public class TextData {
    private final String key;
    private final Object[] arguments;

    public TextData(String key, Object... arguments) {
        this.key = key;
        this.arguments = arguments;
    }
}
