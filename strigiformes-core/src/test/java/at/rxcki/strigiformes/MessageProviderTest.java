package at.rxcki.strigiformes;

import at.rxcki.strigiformes.color.ColorRegistry;
import at.rxcki.strigiformes.component.ClickEvent;
import at.rxcki.strigiformes.text.ResourceBundleTextProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Patrick Zdarsky / Rxcki
 */
class MessageProviderTest {

    private static MessageProvider messageProvider;

    @BeforeAll
    static void setUp() {
        ResourceBundleTextProvider textProvider = new ResourceBundleTextProvider(MessageProviderTest.class.getClassLoader(), "strigiformes");
        textProvider.addResourceBundle(Locale.ENGLISH);
        messageProvider = new MessageProvider(textProvider);

        ResourceBundleTextProvider provider2 = new ResourceBundleTextProvider(MessageProviderTest.class.getClassLoader(), "strigiformes2");
        provider2.addResourceBundle(Locale.ENGLISH);
    }

    @Test
    void testSimpleMessage() {
        Message message = messageProvider.getMessage("test1", Locale.ENGLISH);

        Assertions.assertEquals("sadasdasd", message.getComponents().get(0).getTextList().get(0).getText());
    }

    @Test
    void testSimpleMessageArgDifference() {
        Message message = messageProvider.getMessage("test1", Locale.ENGLISH);
        Message message2 = messageProvider.getMessage("test1", Locale.ENGLISH, "ignoredArg");

        assertEquals(message, message2);
    }

    @Test
    void testSimpleMessageJson() {
        Message message = messageProvider.getMessage("test1", Locale.ENGLISH);

        assertEquals("[{\"text\":\"sadasdasd\"}]", message.toJson().toString());
    }

    @Test
    void testVarMessage() {
        Message message = messageProvider.getMessage("test2", Locale.ENGLISH);

        Assertions.assertEquals("Hello World!", message.getComponents().get(0).getTextList().get(0).getText());
    }

    @Test
    void testVarMessageJson() {
        Message message = messageProvider.getMessage("test2", Locale.ENGLISH);

        assertEquals("[{\"text\":\"Hello World!\"}]", message.toJson().toString());
    }

    @Test
    void testColorMessage() {
        Message message = messageProvider.getMessage("testColored", Locale.ENGLISH);

        Assertions.assertEquals("This is gold", message.getComponents().get(0).getTextList().get(0).getText());
        Assertions.assertEquals(ColorRegistry.getColor("6"), message.getComponents().get(0).getTextList().get(0).getColor());
    }

    @Test
    void testColorMessageJson() {
        Message message = messageProvider.getMessage("testColored", Locale.ENGLISH);

        assertEquals("[{\"color\":\"#ffaa00\",\"text\":\"This is gold\"}]", message.toJson().toString());
    }

    @Test
    void testForeignVar() {
        Message message = messageProvider.getMessage("testForeignVar", Locale.ENGLISH);

        Assertions.assertEquals("test", message.getComponents().get(0).getTextList().get(0).getText());
    }

    @Test
    void testCommandRunComponent() {
        //%{§cRxcki|run_command:/pi Rxcki}
        Message message = messageProvider.getMessage("testCommandRunComponent", Locale.ENGLISH);
        Assertions.assertEquals("Rxcki", message.getComponents().get(0).getTextList().get(0).getText());
        Assertions.assertEquals(ColorRegistry.getColor("c"), message.getComponents().get(0).getTextList().get(0).getColor());
        Assertions.assertEquals(ClickEvent.ClickAction.RUN_COMMAND, message.getComponents().get(0).getClickEvent().getClickAction());
        Assertions.assertEquals("/pi Rxcki", message.getComponents().get(0).getClickEvent().getValue());
    }

    @Test
    void testFormat() {
        Message message = messageProvider.getMessage("testFormat", Locale.ENGLISH, 365, "year");
        Assertions.assertEquals("There are ", message.getComponents().get(0).getTextList().get(0).getText());
    }
}