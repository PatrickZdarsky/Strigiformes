package at.rxcki.strigiformes.parser;

import lombok.Getter;
import lombok.NonNull;
import at.rxcki.strigiformes.exception.ParserException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Getter
public class VariableTag {

    private final static Pattern VARIABLE_COMPONENT_BRACE_PATTERN = Pattern.compile("^\\$\\{(\\w*:)?(\\w*)}$");

    private final String namespace;
    private final String name;

    public VariableTag(String namespace, @NonNull String name) {
        this.namespace = namespace;
        this.name = name;
    }

    public VariableTag(@NonNull String name) {
        this.name = name;
        this.namespace = null;
    }

    @Override
    public String toString() {
        return "VariableComponent{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariableTag)) return false;
        VariableTag that = (VariableTag) o;
        return Objects.equals(namespace, that.namespace) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name);
    }

    public static VariableTag parse(String s) {
        Matcher matcher = VARIABLE_COMPONENT_BRACE_PATTERN.matcher(s);
        if (!matcher.find())
            return null;

        String namespace = matcher.group(1);
        if (namespace != null) {
            //Remove trailing : from capture-group
            namespace = namespace.substring(0, namespace.length() - 1);
        }

        String name = matcher.group(2);
        if (name == null)
            throw new ParserException("Could not retrieve variable name from variable tag");

        return new VariableTag(namespace, name);
    }
}
