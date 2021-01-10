package net.nextvizion.strigiformes.component;

import lombok.Getter;
import lombok.Setter;
import net.nextvizion.strigiformes.color.ColorRegistry;
import net.nextvizion.strigiformes.color.GradientType;
import net.nextvizion.strigiformes.parser.token.ColorToken;
import net.nextvizion.strigiformes.parser.token.Tokenizer;
import net.nextvizion.strigiformes.text.ColoredText;
import net.nextvizion.strigiformes.text.GradientText;
import net.nextvizion.strigiformes.text.TextFormat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ChatComponent {
    @Getter
    private List<ColoredText> textList = new ArrayList<>();

    /**
     * Marks if this chat-component was generated or if it was explicitly created
     */
    @Getter @Setter
    private boolean generated;


    /**
     * This parse method can parse a single declared chatcomponent or a normal color-coded text
     * @param input
     * @return
     */
    public static ChatComponent parse(String input) {
        ChatComponent chatComponent = new ChatComponent();
        if (!input.substring(0, 2).equals("%{")) {
            chatComponent.setGenerated(true);
            System.out.println("Parsing \""+input+"\"");
            //We are parsing a normal text
           var tokens = Tokenizer.tokenize(input).stream()
                   .filter(baseToken -> baseToken instanceof ColorToken)
                   .map(baseToken -> (ColorToken) baseToken)
                   .collect(Collectors.toList());

           int index = 0;
           //Check if there is text before the first color code
           if (tokens.size() == 0 || tokens.get(0).getIndex() > 0) {
               //Todo: Maybe insert the color from the last ChatComponent?
               int end = tokens.size() > 0 ? tokens.get(0).getIndex() : input.length();
               chatComponent.getTextList().add(
                       new ColoredText(input.substring(0, end)));
               index = end;
               System.out.println("  Added beginning text up to "+index +" text=\""+input.substring(0, end)+"\"");
           }


           ColoredText currentText = null;
            for (int i = 0; i < tokens.size(); i++) {
                var colorToken = tokens.get(i);
                var tokenPart = input.substring(colorToken.getIndex(), colorToken.getEnd());

                var format = TextFormat.getFormat(tokenPart);

                if (format != null) {
                    System.out.println("  Parsing format "+tokenPart);
                    if (currentText == null) {
                        System.out.println("   Current is null");
                        currentText = new ColoredText();
                        currentText.addFormat(format);
                        if (chatComponent.getTextList().size() > 0) {
                            System.out.println("    Last is available");
                            //Set color and format from previous
                            var prevText = chatComponent.getTextList().get(chatComponent.getTextList().size() - 1);
                            currentText.setColor(prevText.getColor());
                            if (prevText.getFormats() != null)
                                for (TextFormat textFormat : prevText.getFormats())
                                    currentText.addFormat(textFormat);
                            System.out.println("    Created new with color and format from prev");
                            index = colorToken.getEnd();
                        }
                        continue;
                    }
                    if (index == colorToken.getIndex()) {
                        System.out.println("   Current is not null but with empty text");
                        currentText.addFormat(format);
                        index = colorToken.getEnd();
                        continue;
                    } else {
                        System.out.println("   Current.text is not null");
                        if (currentText instanceof GradientText) {
                            System.out.println("    Current is gradient");
                            currentText.addFormat(format);
                            index = colorToken.getEnd();
                            continue;
                        } else {
                            var text = input.substring(index, colorToken.getIndex());
                            currentText.setText(text);
                            chatComponent.getTextList().add(currentText);

                            var newCurr = new ColoredText();
                            newCurr.addFormat(format);
                            newCurr.setColor(currentText.getColor());
                            if (currentText.getFormats() != null)
                                for (TextFormat textFormat : currentText.getFormats())
                                    newCurr.addFormat(textFormat);
                            System.out.println("    Closed current: "+currentText);

                            currentText = newCurr;
                            index = colorToken.getEnd();
                            continue;
                        }
                    }
                } else {
                    System.out.println("  Parsing colortag "+tokenPart);
                    //Todo: Improve detection logic and stuff
                    int tildeIndex = tokenPart.indexOf('~');
                    if (tildeIndex != -1) {
                        System.out.println("   Parsing gradient");
                        var currColor = ColorRegistry.parse(tokenPart.substring(2, tildeIndex));
                        var gradientType = GradientType.getType(tokenPart.substring(tildeIndex+1, tokenPart.length()-1));

                        //This token is a gradient start
                        if (currentText != null) {
                            System.out.println("    Current is not null");
                            if (currentText instanceof GradientText) {
                                System.out.println("     Current is gradient, setting end color");
                                ((GradientText) currentText).setEndColor(currColor);
                            }
                            currentText.setText(input.substring(index, colorToken.getIndex()));
                            chatComponent.getTextList().add(currentText);
                            index = colorToken.getIndex();
                            System.out.println("     Closed current "+currentText);
                        }
                        var newCurr = new GradientText();
                        newCurr.setColor(currColor);
                        newCurr.setGradientType(gradientType);
                        index = colorToken.getEnd();
                        System.out.println("   Created new gradient " +newCurr);

                        currentText = newCurr;
                        continue;
                    } else {
                        System.out.println("   Parsing color");
                        var currColor = ColorRegistry.parse(tokenPart);
                        if (currentText == null) {
                            System.out.println("    CurrentText is null creating new");
                            currentText = new ColoredText();
                            currentText.setColor(currColor);
                            index = colorToken.getEnd();
                            continue;
                        } else {
                            System.out.println("    CurrentText is not null");
                            if (currentText instanceof GradientText) {
                                ((GradientText) currentText).setEndColor(currColor);
                                //Todo: Maybe also create new color with the current?
                                //Close currentText
                                currentText.setText(input.substring(index, colorToken.getIndex()));
                                chatComponent.getTextList().add(currentText);
                                index = colorToken.getEnd();
                                System.out.println("     Closed gradient "+currentText);
                                currentText = null;
                            } else {
                                currentText.setText(input.substring(index, colorToken.getIndex()));
                                chatComponent.getTextList().add(currentText);
                                System.out.println("     Closed color and created new, old: "+currentText);

                                var newCurr = new ColoredText();
                                newCurr.setColor(currColor);
                                currentText = newCurr;
                                index = colorToken.getEnd();
                                continue;
                            }
                        }
                    }
                }
            }
            if (currentText != null) {
                if (currentText instanceof GradientText) {
                    System.out.println("Unclosed gradient!");
                    ((GradientText) currentText).setEndColor(currentText.getColor());
                }

                currentText.setText(input.substring(index));
                chatComponent.getTextList().add(currentText);
                System.out.println("Closed last "+currentText);
            }
        }


        return chatComponent;
    }

    @Override
    public String toString() {
        return "ChatComponent{" +
                "textList=" + textList +
                ", generated=" + generated +
                '}';
    }
}
