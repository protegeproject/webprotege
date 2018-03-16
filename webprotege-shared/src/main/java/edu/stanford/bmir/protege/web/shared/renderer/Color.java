package edu.stanford.bmir.protege.web.shared.renderer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.padStart;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.toHexString;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2018
 *
 * A simple utility class for representing colors (since Java Color is not GWT compatible)
 */
public class Color implements IsSerializable {

    private static final int MIN = 0;

    private static final int MAX = 255;

    private int red;

    private int green;

    private int blue;

    /**
     * Constructs a Color using RBG values.
     * @param red The red component between 0-255 inclusive
     * @param green The green component between 0-255 inclusive
     * @param blue The blue component between 0-255 inclusive
     */
    public Color(int red, int green, int blue) {
        checkArgument(red >= MIN && red <= MAX, "Invalid value specified for red component");
        this.red = red;
        checkArgument(green >= MIN && green <= MAX, "Invalid value specified for red component");
        this.green = green;
        checkArgument(blue >= MIN && blue <= MAX, "Invalid value specified for red component");
        this.blue = blue;
    }

    @GwtSerializationConstructor
    private Color() {
    }

    /**
     * Gets a color corresponding to the RGB values.
     * @param red The red value, 0-255 inclusive.
     * @param green The green value, 0-255 inclusive.
     * @param blue The blue value 0-255 inclusive.
     * @return The color corresponding to the specified RBG values
     * @throws IllegalArgumentException if any values are out of range.
     */
    public static Color getRGB(int red, int green, int blue) {
        return new Color(red, green, blue);
    }

    /**
     * Gets the color corresponding to the hex value.
     * @param hexValue A string that is 7 characters long, starting with the # character.  All other
     *                 characters must be in the range if 0-f.
     * @return The hex color.
     */
    @JsonCreator
    @Nonnull
    public static Color getHex(@Nonnull String hexValue) {
        checkNotNull(hexValue);
        checkArgument(hexValue.length() == 7, "Invalid hex value (incorrect length)");
        checkArgument(hexValue.startsWith("#"), "Invalid hex value (values should start with #)");
        for(int i = 1; i < hexValue.length(); i++) {
            char ch = hexValue.charAt(i);
            checkArgument(ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f', "Invalid hex value (illegal character at index " + i + ")");
        }
        return getHex(hexValue.substring(1, 3),
                      hexValue.substring(3, 5),
                      hexValue.substring(5, 7));
    }

    public static Color getHex(@Nonnull String red,
                               @Nonnull String green,
                               @Nonnull String blue) {
        checkNotNull(red);
        checkNotNull(green);
        checkNotNull(blue);
        checkArgument(red.length() == 2, "Invalid length for red hex component");
        checkArgument(green.length() == 2, "Invalid length for green hex component");
        checkArgument(blue.length() == 2, "Invalid length for blue hex component");
        return new Color(parseInt(red, 16), parseInt(green, 16), parseInt(blue, 16));
    }

    /**
     * Gets the red component
     * @return An integer value representing the red component (0-255)
     */
    public int getRed() {
        return red;
    }
    
    /**
     * Gets the green component
     * @return An integer value representing the green component (0-255)
     */
    public int getGreen() {
        return green;
    }

    /**
     * Gets the blue component
     * @return An integer value representing the blue component (0-255)
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Gets the Hex RGB value
     */
    @Nonnull
    @JsonValue
    public String getHex() {
        String r = pad(toHexString(red));
        String g = pad(toHexString(green));
        String b = pad(toHexString(blue));
        return "#" + r + g + b;
    }

    private static String pad(String hexValue) {
        return padStart(hexValue, 2, '0');
    }

    @Override
    public String toString() {
        return toStringHelper("Color")
                .add("r", red)
                .add("g", green)
                .add("b", blue)
                .add("hex", getHex())
                .toString();
    }

    @Override
    public int hashCode() {
        return red * 3 + green * 31 + blue * 51;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Color)) {
            return false;
        }
        Color other = (Color) obj;
        return this.red == other.red
                && this.green == other.green
                && this.blue == other.blue;
    }
}
