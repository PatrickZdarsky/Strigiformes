package net.nextvizion.strigiformes.parser.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.nextvizion.strigiformes.exception.TokenizerException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class MessageFormatTokenizer {

    //Extract MessageFormat tokens but ignore our tokens
    public static final Pattern TOKEN_EXTRACTOR = Pattern.compile("(?<![§$%\\\\])\\{|}");

    public static List<FormatToken> tokenize(String input) {
        List<FormatToken> tokens = new ArrayList<>();
        FormatToken activeToken = null;

        var tokenIterator = TOKEN_EXTRACTOR.matcher(input).results().iterator();
        while (tokenIterator.hasNext()) {
            var matchResult = tokenIterator.next();
            var result = matchResult.group();

            if (matchResult.group().equals("}")) {
                if (activeToken == null) {
                    //Ignore since this could very likely be the closing bracket of one of our tokens
                    continue;
                }
                //Close the active token
                activeToken.setEnd(matchResult.start()+1);
                tokens.add(activeToken);
                activeToken = null;

            } else {
                if (activeToken != null) {
                    throw new TokenizerException("Double opening of a MessageFormat Token!");
                }

                activeToken = new FormatToken(matchResult.start());
            }
        }
        return tokens;
    }

    @RequiredArgsConstructor
    public static class FormatToken {
        @Getter
        private final int index;
        @Getter @Setter
        private int end;

        @Override
        public String toString() {
            return "FormatToken{" +
                    "index=" + index +
                    ", end=" + end +
                    '}';
        }
    }

}
