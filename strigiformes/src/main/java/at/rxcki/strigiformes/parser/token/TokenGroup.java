package at.rxcki.strigiformes.parser.token;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This tokengroup is used to group colortokens in order to provide them to the ChatComponent which uses them to parse the formatted text.
 *
 * After all tokens are collected, they can be transformed to represent their position relative to the index of this TokenGroup.
 */
public class TokenGroup extends BaseToken {

    @Getter
    private final List<BaseToken> tokens;

    public TokenGroup(int index) {
        this.index = index;
        tokens = new ArrayList<>();
    }

    public void addToken(BaseToken token) {
        if (token instanceof ComponentToken || token instanceof TokenGroup)
            throw new RuntimeException();

        tokens.add(token);
    }

    public void normalizeIndices() {
        int offset = index;

        for (BaseToken token : tokens) {
            token.index -= offset;
            token.end -= offset;
        }
    }

    @Override
    public void addChildren(BaseToken baseToken) {
        throw new RuntimeException();
    }
}
