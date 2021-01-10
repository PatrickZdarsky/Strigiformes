package net.nextvizion.strigiformes.component;

import lombok.Getter;
import lombok.Setter;
import net.nextvizion.strigiformes.color.ColorRegistry;
import net.nextvizion.strigiformes.parser.token.ColorToken;
import net.nextvizion.strigiformes.parser.token.Tokenizer;
import net.nextvizion.strigiformes.text.ColoredText;
import net.nextvizion.strigiformes.text.GradientText;
import net.nextvizion.strigiformes.text.TextFormat;
import org.json.JSONArray;
import org.json.JSONObject;

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
    @Getter
    private ClickEvent clickEvent;


    public ChatComponent click(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    public ChatComponent click(ClickEvent.ClickAction clickAction, String value) {
        this.clickEvent = new ClickEvent(clickAction, value);
        return this;
    }

    /**
     * This parse method can parse a single declared chatcomponent or a normal color-coded text
     * @param input
     * @return
     */
    public static ChatComponent parse(String input) {
        ChatComponent chatComponent = new ChatComponent();
        if (!input.startsWith("%{")) {
            chatComponent.setGenerated(true);
            parseString(input, chatComponent);
        } else {
            var innerPart = input.substring(2, input.length()-1);
            debug("Decoding declared component: "+innerPart);

            //Split by | but ignore | which are disabled by \
            //Todo: Precompile the pattern
            String[] split = innerPart.split("(?<!\\\\)\\|");
            parseString(split[0], chatComponent);
            if (split.length > 1) {
                for (int i = 1; i < split.length; i++) {
                    var dottedSplit = split[i].split(":");
                    var clickAction = ClickEvent.ClickAction.valueOf(dottedSplit[0].toUpperCase());
                    if (clickAction != null) {
                        chatComponent.click(clickAction, dottedSplit[1]);
                        continue;
                    }
                    var hoverAction = HoverEvent.HoverAction.valueOf(dottedSplit[0].toUpperCase());
                    if (hoverAction != null) {

                    }
                }
            }
        }


        return chatComponent;
    }

    private static void parseString(String input, ChatComponent chatComponent) {
        debug("Parsing \""+ input +"\"");
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
            debug("  Added beginning text up to "+index +" text=\""+ input.substring(0, end)+"\"");
        }


        ColoredText currentText = null;
        for (ColorToken colorToken : tokens) {
            var tokenPart = input.substring(colorToken.getIndex(), colorToken.getEnd());

            var format = TextFormat.getFormat(tokenPart);

            if (format != null) {
                debug("  Parsing format " + tokenPart);
                if (currentText == null) {
                    debug("   Current is null");
                    currentText = new ColoredText();
                    currentText.addFormat(format);
                    if (chatComponent.getTextList().size() > 0) {
                        debug("    Last is available");
                        //Set color and format from previous
                        var prevText = chatComponent.getTextList().get(chatComponent.getTextList().size() - 1);
                        currentText.setColor(prevText.getColor());
                        if (prevText.getFormats() != null)
                            for (TextFormat textFormat : prevText.getFormats())
                                currentText.addFormat(textFormat);
                        debug("    Created new with color and format from prev");
                        index = colorToken.getEnd();
                    }
                    continue;
                }
                if (index == colorToken.getIndex()) {
                    debug("   Current is not null but with empty text");
                    currentText.addFormat(format);
                    index = colorToken.getEnd();
                    continue;
                } else {
                    debug("   Current.text is not null");
                    if (currentText instanceof GradientText) {
                        debug("    Current is gradient");
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
                        debug("    Closed current: " + currentText);

                        currentText = newCurr;
                        index = colorToken.getEnd();
                        continue;
                    }
                }
            } else {
                debug("  Parsing colortag " + tokenPart);
                //Todo: Improve detection logic and stuff
                int tildeIndex = tokenPart.indexOf('~');
                if (tildeIndex != -1) {
                    debug("   Parsing gradient");
                    var currColor = ColorRegistry.parse(tokenPart.substring(2, tildeIndex));
                    var gradientType = tokenPart.substring(tildeIndex + 1, tokenPart.length() - 1);

                    //This token is a gradient start
                    if (currentText != null) {
                        debug("    Current is not null");
                        if (currentText instanceof GradientText) {
                            debug("     Current is gradient, setting end color");
                            ((GradientText) currentText).setEndColor(currColor);
                        }
                        currentText.setText(input.substring(index, colorToken.getIndex()));
                        chatComponent.getTextList().add(currentText);
                        index = colorToken.getIndex();
                        debug("     Closed current " + currentText);
                    }
                    var newCurr = new GradientText();
                    newCurr.setColor(currColor);
                    newCurr.setGradientType(gradientType);
                    index = colorToken.getEnd();
                    debug("   Created new gradient " + newCurr);

                    currentText = newCurr;
                    continue;
                } else {
                    debug("   Parsing color");
                    var currColor = ColorRegistry.parse(tokenPart);
                    if (currentText == null) {
                        debug("    CurrentText is null creating new");
                        currentText = new ColoredText();
                        currentText.setColor(currColor);
                        index = colorToken.getEnd();
                        continue;
                    } else {
                        debug("    CurrentText is not null");
                        if (currentText instanceof GradientText) {
                            ((GradientText) currentText).setEndColor(currColor);
                            //Todo: Maybe also create new color with the current?
                            //Close currentText
                            currentText.setText(input.substring(index, colorToken.getIndex()));
                            chatComponent.getTextList().add(currentText);
                            index = colorToken.getEnd();
                            debug("     Closed gradient " + currentText);
                            currentText = null;
                        } else {
                            currentText.setText(input.substring(index, colorToken.getIndex()));
                            chatComponent.getTextList().add(currentText);
                            debug("     Closed color and created new, old: " + currentText);

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
                debug("Unclosed gradient!");
                ((GradientText) currentText).setEndColor(currentText.getColor());
            }

            currentText.setText(input.substring(index));
            chatComponent.getTextList().add(currentText);
            debug("Closed last "+currentText);
        }
    }

    private static void debug(String s) {
        System.out.println(s);
    }

    public JSONArray toJson() {
        var jsonArray = new JSONArray();
        var coloredTexts = new ArrayList<ColoredText>();
        for (ColoredText coloredText : textList) {
            if (coloredText instanceof GradientText) {
                coloredTexts.addAll(((GradientText) coloredText).toColoredTexts());

            } else
                coloredTexts.add(coloredText);
        }

        for (ColoredText coloredText : coloredTexts) {
            var json = coloredText.toJson();
            jsonArray.put(json);

            if (clickEvent != null) {
                var clickJson = new JSONObject();
                clickJson.put("action", clickEvent.getClickAction().name().toLowerCase());
                clickJson.put("value", clickEvent.getValue());
                json.put("clickEvent", clickJson);
            }
        }

        return jsonArray;
    }

    @Override
    public String toString() {
        return "ChatComponent{" +
                "textList=" + textList +
                ", generated=" + generated +
                '}';
    }
}
