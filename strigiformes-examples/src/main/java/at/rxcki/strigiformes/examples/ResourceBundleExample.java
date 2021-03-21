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

package at.rxcki.strigiformes.examples;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.color.ColorRegistry;
import at.rxcki.strigiformes.text.ResourceBundleTextProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ResourceBundleExample {

    public static MessageProvider messageProvider;

    public static void main(String[] args) {
        /*
        To use the legacy colors simply add:

        ColorRegistry.useLegacyColors = true;
         */

        setup();

        System.out.println(messageProvider.getMessage("testString", Locale.ENGLISH));
        //Use the MessageFormat to insert values
        System.out.println(messageProvider.getMessage("currentDate", Locale.ENGLISH,
                Calendar.getInstance().getTime().getDay()).toString());

        //This could be another plugin or module also registering a MessageProvider
        setupAnother();

        //To access translations from the submodule the namespace has to be added to the key.
        //For example if we want to get the "prefix" string from the other module using our MessageProvider we add the namespace of it:
        System.out.println(messageProvider.getMessage("submodule:prefix", Locale.ENGLISH).toString());
    }

    private static void setup() {
        //Getting the TextProvider
        ResourceBundleTextProvider textProvider = new ResourceBundleTextProvider(
                ResourceBundleTextProvider.class.getClassLoader(), "test");

        //Adding the english locale, this will load the file "Test_en.properties" from the resources
        textProvider.addResourceBundle(Locale.ENGLISH);


        //Creating the MessageProvider with the TextProvider
        messageProvider = new MessageProvider(textProvider);
    }

    private static void setupAnother() {
        ResourceBundleTextProvider textProvider = new ResourceBundleTextProvider(
                ResourceBundleTextProvider.class.getClassLoader(), "submodule");

        //This will load the translations from "submodule_en.properties"
        textProvider.addResourceBundle(Locale.ENGLISH);


        MessageProvider provider = new MessageProvider(textProvider);
    }
}
