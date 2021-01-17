package net.nextvizion.strigiformes.parser.token;

import net.nextvizion.strigiformes.exception.TokenizerException;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class VariableToken extends BaseToken {

    public VariableToken(BaseToken parent, int index) {
        super(parent, index);
    }

    @Override
    public void addChildren(BaseToken baseToken) {
        throw new TokenizerException("A VariableToken cannot have a child!");
    }
}
