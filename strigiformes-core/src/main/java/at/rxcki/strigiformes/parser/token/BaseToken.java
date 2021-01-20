package at.rxcki.strigiformes.parser.token;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class BaseToken {

    @Getter
    private final BaseToken parent;

    @Getter
    private final int index;
    @Getter @Setter
    private int end;

    public BaseToken(BaseToken parent, int index) {
        this.parent = parent;
        this.index = index;
    }

    public abstract void addChildren(BaseToken baseToken);

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{index=" + index +
                ", end=" + end +
                '}';
    }
}
