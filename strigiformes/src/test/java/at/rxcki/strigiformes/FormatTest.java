package at.rxcki.strigiformes;

import at.rxcki.strigiformes.message.Message;
import at.rxcki.strigiformes.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Patrick Zdarsky / Rxcki
 */

public class FormatTest {

    @Test
    public void testSimpleReset() {
        Parser parser = new Parser();
        Message message = parser.parse("§6Test §rnormal");

        assertEquals(2, message.getComponents().get(0).getTextList().size());
        assertNull( message.getComponents().get(0).getTextList().get(0).getFormats());
        assertNull(message.getComponents().get(0).getTextList().get(1).getFormats());
    }

    @Test
    void testInitialReset() {
        Parser parser = new Parser();
        Message message = parser.parse("§rthis was reset");

        assertEquals(1, message.getComponents().get(0).getTextList().size());
        assertEquals("this was reset", message.getComponents().get(0).getTextList().get(0).getText());
    }

    @Test
    void testEndReset() {
        Parser parser = new Parser();
        Message message = parser.parse("§6Gold§r");

        assertEquals(1, message.getComponents().get(0).getTextList().size());
    }
}
