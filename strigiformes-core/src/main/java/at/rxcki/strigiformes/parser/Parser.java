package at.rxcki.strigiformes.parser;

import at.rxcki.strigiformes.Message;
import at.rxcki.strigiformes.parser.token.BaseToken;
import at.rxcki.strigiformes.component.ChatComponent;
import at.rxcki.strigiformes.parser.token.ComponentToken;
import at.rxcki.strigiformes.parser.token.Tokenizer;
import java.util.stream.Collectors;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Parser {

    //At this point all variables are already resolved
    public static Message parse(String input) {
        if (input.isEmpty())
            return null;

        var message = new Message();

        // Get all tokens except VariableTokens since these should have been already resolved
        var tokens = Tokenizer.tokenize(input).stream()
                .filter(baseToken -> (baseToken instanceof ComponentToken))
                .collect(Collectors.toList());

        //Position up to when we have parsed the input
        int index = 0;

        for (BaseToken baseToken : tokens) {
            //Parse text in front of component
            if (baseToken.getIndex() > index) {
                String part = input.substring(index, baseToken.getIndex());

                message.getComponents().add(ChatComponent.parse(part));
            }
            //Parse component
            String part = input.substring(baseToken.getIndex(), baseToken.getEnd());
            message.getComponents().add(ChatComponent.parse(part));

            index = baseToken.getEnd();
        }

        //Check if we are at the end of the input, if not -> parse it
        if (index < input.length()-1) {
            String part = input.substring(index);

            message.getComponents().add(ChatComponent.parse(part));
        }

        return message;
    }
}
