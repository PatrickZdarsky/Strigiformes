package net.nextvizion.strigiformes.parser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.nextvizion.strigiformes.Message;
import net.nextvizion.strigiformes.color.ColorRegistry;
import net.nextvizion.strigiformes.component.ChatComponent;
import net.nextvizion.strigiformes.parser.token.BaseToken;
import net.nextvizion.strigiformes.parser.token.ColorToken;
import net.nextvizion.strigiformes.parser.token.Tokenizer;
import net.nextvizion.strigiformes.parser.token.VariableToken;
import net.nextvizion.strigiformes.text.ColoredText;

import java.awt.Color;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@RequiredArgsConstructor
public class Parser {

    /*


        ${sysPrefix}Du befindest dich auf §{grey}Server %{§{gold}Lobby-1|run_command:/server lobby1}


        %{§{gold}Lobby-§{green}1|run_command:/server lobby1}


        sadsdsad§7asddsda

    */

    @NonNull
    private final String input;




    //At this point all variables are already resolved
    public Message parse() {
        if (input.isEmpty())
            return null;

        var message = new Message();
        var tokenizer = new Tokenizer(input);
        tokenizer.tokenize();

        // All tokens except VariableTokens since these should have been already resolved
        var tokens = tokenizer.getTokens().stream()
                .filter(baseToken -> (!(baseToken instanceof VariableToken)))
                .collect(Collectors.toList());


        //Check if the first token is not at the beginning
        if (tokens.get(0).getIndex() > 0) {
            //This text does not have a color or anything associated
            String part = input.substring(0, tokens.get(0).getIndex());


            var chatComponent = new ChatComponent();
            chatComponent.getTextList().add(new ColoredText(part));
        }

        Iterator<BaseToken> tokenIterator = tokenizer.getTokens().iterator();

        ColoredText coloredText = null;
        while (tokenIterator.hasNext()) {
            BaseToken baseToken = tokenIterator.next();
            String tokenString = input.substring(baseToken.getIndex(), baseToken.getEnd()+1);

            /*
                §f§kasdsads §6asdsad §{grey}Server
             */
            if (baseToken instanceof ColorToken) {
                if (coloredText != null) {
                    //Remove §
                    Color color = ColorRegistry.parse(tokenString.substring(1));
                    if (color == null) {

                    }

                }




            }





        }









        return null;
    }
}
