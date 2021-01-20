package at.rxcki.strigiformes.text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class TextProviderRegistry {

    private final static Map<String, TextProvider> providers = new HashMap<>();

    public static void registerProvider(TextProvider textProvider) {
        if (providers.containsKey(textProvider.getNamespace().toLowerCase())) {
            throw new IllegalStateException("Tried to register already existing namespace");
        }
        providers.put(textProvider.getNamespace(), textProvider);
    }

    public static TextProvider getProviderByNamespace(String namespace) {
        return providers.get(namespace);
    }

    public static Set<TextProvider> getProviders() {
        return new HashSet<>(providers.values());
    }
}
