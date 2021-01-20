package at.rxcki.strigiformes.examples;

import at.rxcki.strigiformes.MessageProvider;
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
        setup();

        System.out.println(messageProvider.getMessage("testString", Locale.ENGLISH).toJson());
        System.out.println(messageProvider.getMessage("currentDate", Locale.ENGLISH, Calendar.getInstance().getTime().getDay()).toJson());
    }

    private static void setup() {
        //Getting the TextProvider
        ResourceBundleTextProvider textProvider = new ResourceBundleTextProvider(ResourceBundleTextProvider.class.getClassLoader(), "test");

        //Adding the english locale, this will load the file "Test_en.properties" from the resources
        textProvider.addResourceBundle(Locale.ENGLISH);


        //Creating the MessageProvider with the TextProvider
        messageProvider = new MessageProvider(textProvider);
    }
}
