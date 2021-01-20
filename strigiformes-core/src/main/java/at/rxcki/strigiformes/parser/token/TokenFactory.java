package at.rxcki.strigiformes.parser.token;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class TokenFactory {

    public static BaseToken getToken(String indicator, BaseToken parent, int index) {
        switch (indicator) {
            case "$":
                return new VariableToken(parent,index);
            case "%":
                return new ComponentToken(parent,index);
            case "§":
                return new ColorToken(parent,index);
        }
        return null;
    }
}
