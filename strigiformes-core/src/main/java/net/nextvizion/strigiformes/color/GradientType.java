package net.nextvizion.strigiformes.color;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public enum GradientType {
    //Todo: Maybe this will be its own class // placeholder

    LINEAR();

    public static GradientType getType(String name) {
        System.out.println("   Gradienttype retrieval for "+name);
        return LINEAR;
    }
}
