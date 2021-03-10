package at.rxcki.strigiformes.message;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public interface IMessageCache {

    /**
     * Get the fully parsed Message
     * @param locale The locale to use
     * @return The fully parsed message
     */
    Message getMessage(Locale locale);

}
