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

        System.out.println(messageProvider.getMessage("testString", Locale.ENGLISH).toJson());
        //Use the MessageFormat to insert values
        System.out.println(messageProvider.getMessage("currentDate", Locale.ENGLISH,
                Calendar.getInstance().getTime().getDay()).toJson());

        //This could be another plugin or module also registering a MessageProvider
        setupAnother();

        //To access translations from the submodule the namespace has to be added to the key.
        //For example if we want to get the "prefix" string from the other module using our MessageProvider we add the namespace of it:
        System.out.println(messageProvider.getMessage("submodule:prefix", Locale.ENGLISH).toJson());
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
