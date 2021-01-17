package net.nextvizion.strigiformes.parser.token;

import net.nextvizion.strigiformes.exception.TokenizerException;

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
