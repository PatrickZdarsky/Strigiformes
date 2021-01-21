package at.rxcki.strigiformes.message;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public interface IMessageCache {

    Message getMessage(Locale locale);
}
