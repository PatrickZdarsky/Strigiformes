package net.nextvizion.strigiformes.parser.token;

import net.nextvizion.strigiformes.exception.ParserException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ComponentToken extends BaseToken {

    private final List<BaseToken> children;

    public ComponentToken(BaseToken parent, int index) {
        super(parent, index);
        children = new ArrayList<>();
    }

    @Override
    public void addChildren(BaseToken baseToken) {
        if (baseToken instanceof ComponentToken) {
            throw new ParserException("Tried to add ComponentTag as Children for a ComponentTag");
        }

        children.add(baseToken);
    }

    @Override
    public String toString() {
        return "ComponentToken{" +
                "children=" + children +
                ", super=" + super.toString() + "}";
    }
}
