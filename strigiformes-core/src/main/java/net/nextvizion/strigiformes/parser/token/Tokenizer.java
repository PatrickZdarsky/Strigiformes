package net.nextvizion.strigiformes.parser.token;

import lombok.Getter;
import net.nextvizion.strigiformes.parser.VariableTag;
import net.nextvizion.strigiformes.exception.TokenizerException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Tokenizer {

    public static final Pattern TOKEN_EXTRACTOR = Pattern.compile("[§$%]\\{|}|§[0-9a-fA-F]");

    private final String input;
    @Getter
    private final List<BaseToken> tokens = new ArrayList<>();
    private BaseToken activeToken = null;

    public Tokenizer(String input) {
        System.out.println(input);
        this.input = input;
    }

    public void tokenize() {
        TOKEN_EXTRACTOR.matcher(input).results().forEach(matchResult -> tokenize(matchResult));





        System.out.println("RESULT");
        for (BaseToken baseToken : tokens) {
            System.out.println(baseToken);
            if (baseToken instanceof VariableToken) {
                System.out.println(VariableTag.parse(input.substring(baseToken.getIndex(), baseToken.getEnd()+1)));
            }
        }
    }

    private void tokenize(MatchResult matchResult) {
        String result = matchResult.group();

        if (matchResult.group().equals("}")) {
            if (activeToken == null) {
                throw new TokenizerException("Found closing bracket without opening one");
            }
            activeToken.setEnd(matchResult.start());
            activeToken = activeToken.getParent();

        } else if (result.charAt(0) == '§'
                && matchResult.group().length() == 2
                && result.charAt(result.length()-1) != '{') {
            var token = new ColorToken(null, matchResult.start());
            token.setEnd(matchResult.end());

            if (activeToken != null) {
                activeToken.addChildren(token);
                return;
            }
            tokens.add(token);
        } else {
          var indicator = matchResult.group().substring(0, 1);
            if (activeToken == null) {
                activeToken = TokenFactory.getToken(indicator, null, matchResult.start());
                tokens.add(activeToken);
                return;
            }

            //Move one deeper
            var childToken = TokenFactory.getToken(indicator, activeToken, matchResult.start());
            activeToken.addChildren(childToken);
            activeToken = childToken;
        }
    }
}
