package net.nextvizion.strigiformes.parser.token;

import net.nextvizion.strigiformes.exception.TokenizerException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Tokenizer {

    public static final Pattern TOKEN_EXTRACTOR = Pattern.compile("[§$%]\\{|}|§[0-9a-fA-Fk-oK-O]");

    public static List<BaseToken> tokenize(String input) {
        List<BaseToken> tokens = new ArrayList<>();
        BaseToken activeToken = null;

        var tokenIterator = TOKEN_EXTRACTOR.matcher(input).results().iterator();
        while (tokenIterator.hasNext()) {
            var matchResult = tokenIterator.next();
            var result = matchResult.group();

            if (matchResult.group().equals("}")) {
                if (activeToken == null) {
                    throw new TokenizerException("Found closing bracket without opening one");
                }
                //Close the active token
                activeToken.setEnd(matchResult.start()+1);
                activeToken = activeToken.getParent();

                //Legacy color-code logic
            } else if (result.charAt(0) == '§'
                    && matchResult.group().length() == 2
                    && result.charAt(result.length()-1) != '{') {
                var token = new ColorToken(null, matchResult.start());
                token.setEnd(matchResult.end());

                if (activeToken != null) {
                    activeToken.addChildren(token);
                    continue;
                }
                tokens.add(token);
            } else {
                var indicator = matchResult.group().substring(0, 1);
                if (activeToken == null) {
                    activeToken = TokenFactory.getToken(indicator, null, matchResult.start());
                    tokens.add(activeToken);
                    continue;
                }

                //Move one deeper
                var childToken = TokenFactory.getToken(indicator, activeToken, matchResult.start());
                activeToken.addChildren(childToken);
                activeToken = childToken;
            }
        }
        return tokens;
    }
}
