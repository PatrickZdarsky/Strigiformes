package at.rxcki.strigiformes.parser.token;

import at.rxcki.strigiformes.exception.TokenizerException;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ColorToken extends BaseToken {

    public ColorToken(BaseToken parent, int index) {
        super(parent, index);
    }

    @Override
    public void addChildren(BaseToken baseToken) {
        throw new TokenizerException("A ColorToken cannot have a child!");
    }
}
