package net.nextvizion.strigiformes.parser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.nextvizion.strigiformes.Message;
import net.nextvizion.strigiformes.component.ChatComponent;
import net.nextvizion.strigiformes.parser.token.BaseToken;
import net.nextvizion.strigiformes.parser.token.ComponentToken;
import net.nextvizion.strigiformes.parser.token.Tokenizer;
import java.util.stream.Collectors;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@RequiredArgsConstructor
public class Parser {

    @NonNull
    private final String input;

    //At this point all variables are already resolved
    public Message parse() {
        if (input.isEmpty())
            return null;

        var message = new Message();
        var tokenizer = new Tokenizer(input);
        tokenizer.tokenize();
        //Position up to when we have parsed the input
        int index = 0;

        // All tokens except VariableTokens since these should have been already resolved
        var tokens = tokenizer.getTokens().stream()
                .filter(baseToken -> (baseToken instanceof ComponentToken))
                .collect(Collectors.toList());

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
