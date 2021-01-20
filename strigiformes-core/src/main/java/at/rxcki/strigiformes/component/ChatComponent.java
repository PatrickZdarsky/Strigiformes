package at.rxcki.strigiformes.component;

import at.rxcki.strigiformes.color.ColorRegistry;
import at.rxcki.strigiformes.color.ColoredText;
import at.rxcki.strigiformes.color.TextFormat;
import at.rxcki.strigiformes.color.gradients.GradientText;
import lombok.Getter;
import lombok.Setter;
import at.rxcki.strigiformes.parser.token.ColorToken;
import at.rxcki.strigiformes.parser.token.Tokenizer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @Getter
    private HoverEvent hoverEvent;


    public ChatComponent click(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    public ChatComponent click(ClickEvent.ClickAction clickAction, String value) {
        this.clickEvent = new ClickEvent(clickAction, value);
        return this;
    }

    public ChatComponent hover(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        return this;
    }

    protected void setTextList(List<ColoredText> list) {
        this.textList = list;
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
            chatComponent.setTextList(parseString(input));
        } else {
            String innerPart = input.substring(2, input.length()-1);
            debug("Decoding declared component: "+innerPart);

            //Split by | but ignore | which are disabled by \
            //Todo: Precompile the pattern
            String[] split = innerPart.split("(?<!\\\\)\\|");
            chatComponent.setTextList(parseString(split[0]));
            if (split.length > 1) {
                for (int i = 1; i < split.length; i++) {
                    String[] dottedSplit = split[i].split(":");
                    ClickEvent.ClickAction clickAction = ClickEvent.ClickAction.getAction(dottedSplit[0].toUpperCase());
                    debug(clickAction+"");
                    if (clickAction != null) {
                        chatComponent.click(clickAction, dottedSplit[1]);
                        continue;
                    }
                    HoverEvent.HoverAction hoverAction = HoverEvent.HoverAction.getAction(dottedSplit[0].toUpperCase());
                    if (hoverAction != null) {
                        HoverEvent hoverEvent = new HoverEvent(hoverAction);
                        hoverEvent.setText(parseString(split[i].substring(dottedSplit[0].length()+1)));
                        chatComponent.hover(hoverEvent);
                    }
                }
            }
        }

        return chatComponent;
    }

    private static List<ColoredText> parseString(String input) {
        List<ColoredText> texts = new ArrayList<>();

        debug("Parsing \""+ input +"\"");
        //We are parsing a normal text
        List<ColorToken> tokens = Tokenizer.tokenize(input).stream()
                .filter(baseToken -> baseToken instanceof ColorToken)
                .map(baseToken -> (ColorToken) baseToken)
                .collect(Collectors.toList());

        int index = 0;
        //Check if there is text before the first color code
        if (tokens.size() == 0 || tokens.get(0).getIndex() > 0) {
            //Todo: Maybe insert the color from the last ChatComponent?
            int end = tokens.size() > 0 ? tokens.get(0).getIndex() : input.length();
            texts.add(
                    new ColoredText(input.substring(0, end)));
            index = end;
            debug("  Added beginning text up to "+index +" text=\""+ input.substring(0, end)+"\"");
        }


        ColoredText currentText = null;
        for (ColorToken colorToken : tokens) {
            String tokenPart = input.substring(colorToken.getIndex(), colorToken.getEnd());

            TextFormat format = TextFormat.getFormat(tokenPart);

            if (format != null) {
                debug("  Parsing format " + tokenPart);
                if (currentText == null) {
                    debug("   Current is null");
                    currentText = new ColoredText();
                    currentText.addFormat(format);
                    if (texts.size() > 0) {
                        debug("    Last is available");
                        //Set color and format from previous
                        ColoredText prevText = texts.get(texts.size() - 1);
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
                        String text = input.substring(index, colorToken.getIndex());
                        currentText.setText(text);
                        texts.add(currentText);

                        ColoredText newCurr = new ColoredText();
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
                    Color currColor = ColorRegistry.parse(tokenPart.substring(2, tildeIndex));
                    String gradientType = tokenPart.substring(tildeIndex + 1, tokenPart.length() - 1);

                    //This token is a gradient start
                    if (currentText != null) {
                        debug("    Current is not null");
                        if (currentText instanceof GradientText) {
                            debug("     Current is gradient, setting end color");
                            ((GradientText) currentText).setEndColor(currColor);
                        }
                        currentText.setText(input.substring(index, colorToken.getIndex()));
                        texts.add(currentText);
                        index = colorToken.getIndex();
                        debug("     Closed current " + currentText);
                    }
                    GradientText newCurr = new GradientText();
                    newCurr.setColor(currColor);
                    newCurr.setGradientType(gradientType);
                    index = colorToken.getEnd();
                    debug("   Created new gradient " + newCurr);

                    currentText = newCurr;
                    continue;
                } else {
                    debug("   Parsing color");
                    Color currColor = ColorRegistry.parse(tokenPart);
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
                            texts.add(currentText);
                            index = colorToken.getEnd();
                            debug("     Closed gradient " + currentText);
                            currentText = null;
                        } else {
                            currentText.setText(input.substring(index, colorToken.getIndex()));
                            texts.add(currentText);
                            debug("     Closed color and created new, old: " + currentText);

                            ColoredText newCurr = new ColoredText();
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
            texts.add(currentText);
            debug("Closed last "+currentText);
        }

        return texts;
    }

    private static void debug(String s) {
    }

    public JSONArray toJson() {
        JSONArray jsonArray = new JSONArray();
        List<ColoredText> coloredTexts = new ArrayList<>();
        //Improve this logic, so that we don't have to iterate over it twice
        for (ColoredText coloredText : textList) {
            if (coloredText instanceof GradientText) {
                coloredTexts.addAll(((GradientText) coloredText).toColoredTexts());

            } else
                coloredTexts.add(coloredText);
        }

        for (ColoredText coloredText : coloredTexts) {
            JSONObject json = coloredText.toJson();
            jsonArray.put(json);

            if (clickEvent != null) {
                JSONObject clickJson = new JSONObject();
                clickJson.put("action", clickEvent.getClickAction().name().toLowerCase());
                clickJson.put("value", clickEvent.getValue());
                json.put("clickEvent", clickJson);
            }

            if (hoverEvent != null) {
                JSONObject hoverJson = new JSONObject();
                hoverJson.put("action", hoverEvent.getHoverAction().name().toLowerCase());
                List<ColoredText> hoverTexts = new ArrayList<>();
                for (ColoredText text : hoverEvent.getText()) {
                    if (text instanceof GradientText) {
                        hoverTexts.addAll(((GradientText) text).toColoredTexts());

                    } else
                        hoverTexts.add(text);
                }

                JSONArray contents = new JSONArray();
                //Why mojang? just why?
                contents.put("");

                for(ColoredText coloredText1 : hoverTexts) {
                    contents.put(coloredText1.toJson());
                }
                hoverJson.put("contents", contents);

                json.put("hoverEvent", hoverJson);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatComponent)) return false;
        ChatComponent that = (ChatComponent) o;
        return generated == that.generated && Objects.equals(textList, that.textList) && Objects.equals(clickEvent, that.clickEvent) && Objects.equals(hoverEvent, that.hoverEvent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textList, generated, clickEvent, hoverEvent);
    }
}
