/*
 *  This file is part of Strigiformes, licensed under the MIT License.
 *
 *   Copyright (c) 2021 Patrick Zdarsky
 *   Copyright (c) contributors
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ChatComponent {

    private static final Pattern PIPE_SPLIT_PATTERN = Pattern.compile("(?<!\\\\)\\|");
    private static final Pattern COLON_PATTERN = Pattern.compile(":");

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
     * This parse method can parse a single declared {@link ChatComponent} or a normal color-coded text
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
            String[] split = PIPE_SPLIT_PATTERN.split(innerPart);
            chatComponent.setTextList(parseString(split[0]));
            if (split.length > 1) {
                for (int i = 1; i < split.length; i++) {
                    String[] dottedSplit = COLON_PATTERN.split(split[i]);
                    ClickEvent.ClickAction clickAction = ClickEvent.ClickAction.getAction(dottedSplit[0]);
                    debug(clickAction+"");
                    if (clickAction != null) {
                        chatComponent.click(clickAction, split[i].substring(dottedSplit[0].length()+1));
                        continue;
                    }
                    HoverEvent.HoverAction hoverAction = HoverEvent.HoverAction.getAction(dottedSplit[0]);
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
                .map(baseToken -> (ColorToken) baseToken)
                .collect(Collectors.toList());
        debug("Tokens: "+tokens.stream().map(colorToken -> colorToken.toString()).collect(Collectors.joining(", ")));

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
                if (format == TextFormat.RESET) {
                    if (currentText == null) {
                        debug("     Ignore Reset??? " + currentText);
                        ColoredText newCurr = new ColoredText();
                        currentText = newCurr;
                    } else {
                        currentText.setText(input.substring(index, colorToken.getIndex()));
                        texts.add(currentText);
                        debug("     Closed due to reset and created new, old: " + currentText);

                        ColoredText newCurr = new ColoredText();
                        currentText = newCurr;
                    }
                    index = colorToken.getEnd();
                    continue;
                }

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
                    if (gradientType.equals(""))
                        gradientType = "linear";

                    //This token is a gradient start
                    if (currentText != null) {
                        debug("    Current is not null");
                        if (currentText instanceof GradientText) {
                            debug("     Current is gradient, setting end color");
                            ((GradientText) currentText).setEndColor(currColor);
                        }
                        currentText.setText(input.substring(index, colorToken.getIndex()));
                        texts.add(currentText);
                        //Todo: Check if this double assignment of index messes things up
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

            if (index < input.length()) {
                currentText.setText(input.substring(index));
                texts.add(currentText);
                debug("Closed last " + currentText);
            } else {
                debug("Ignored last empty text");
            }
        }

        return texts;
    }

    private static void debug(String s) {
        //This is only for debugging purposes
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

        //Convert the coloredtexts to MessageJson format
        for (int i = 0; i < coloredTexts.size(); i++) {
            ColoredText coloredText = coloredTexts.get(i);
            JSONObject json = coloredText.toJson();
            jsonArray.put(json);

            //Properly disable formats
            if (jsonArray.length() == 1) {
                //This is the first array, so reset all other formats
                //Todo: Maybe check the old component for the proper last formats
                for (TextFormat format : TextFormat.VALUES) {
                    if (format != TextFormat.RESET &&
                            coloredText.getFormats() != null && !coloredText.getFormats().contains(format)) {
                        json.put(format.name().toLowerCase(), false);
                    }
                }
            } else {
                //Check the difference to the last text
                ColoredText last = coloredTexts.get(i-1);
                if (last.getFormats() != null) {
                    for (TextFormat format : last.getFormats()) {
                        if (coloredText.getFormats() == null || !coloredText.getFormats().contains(format)) {
                            json.put(format.name().toLowerCase(), false);
                        }
                    }
                }
            }

            //Add the ClickEvent
            if (clickEvent != null) {
                JSONObject clickJson = new JSONObject();
                clickJson.put("action", clickEvent.getClickAction().name().toLowerCase());
                clickJson.put("value", clickEvent.getValue());
                json.put("clickEvent", clickJson);
            }

            //Add the HoverEvent
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
