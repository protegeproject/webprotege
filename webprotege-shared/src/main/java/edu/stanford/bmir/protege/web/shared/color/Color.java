package edu.stanford.bmir.protege.web.shared.color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.padStart;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.toHexString;
import static java.lang.Math.abs;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2018
 *
 * A simple utility class for representing colors (since Java Color is not GWT compatible)
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class Color implements IsSerializable {

    private static final int MIN = 0;

    private static final int MAX = 255;

    private static final String RED = "red";

    private static final String GREEN = "green";

    private static final String BLUE = "blue";

    /**
     * Constructs a Color using RBG values.
     * @param red The red component between 0-255 inclusive
     * @param green The green component between 0-255 inclusive
     * @param blue The blue component between 0-255 inclusive
     */
    public static Color get(int red, int green, int blue) {
        checkArgument(red >= MIN && red <= MAX, "Invalid value specified for red component");
        checkArgument(green >= MIN && green <= MAX, "Invalid value specified for red component");
        checkArgument(blue >= MIN && blue <= MAX, "Invalid value specified for red component");
        return new AutoValue_Color(red, green, blue);
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
        return get(red, green, blue);
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

    /**
     * Get a color from the hex rgb values.
     * @param red A string of two characters representing the hex value for the red component.
     * @param green A string of two characters representing the hex value for the green component.
     * @param blue A string of two characters representing the hex value for the blue component.
     * @return The corresponding color.
     */
    public static Color getHex(@Nonnull String red,
                               @Nonnull String green,
                               @Nonnull String blue) {
        checkNotNull(red);
        checkNotNull(green);
        checkNotNull(blue);
        checkArgument(red.length() == 2, "Invalid length for red hex component");
        checkArgument(green.length() == 2, "Invalid length for green hex component");
        checkArgument(blue.length() == 2, "Invalid length for blue hex component");
        return get(parseInt(red, 16), parseInt(green, 16), parseInt(blue, 16));
    }

    /**
     * Get a color from Hue, Saturation and Lightness values.
     * @param hue The value for the hue.  This must be in the range of 0 to 360 (inclusive)
     * @param saturation The value for the saturation.  This must be in the range of 0 to 1 (inclusive)
     * @param lightness The value for the lightness.  This must be in the range of 0 to 1 (inclusive)
     * @return The corresponding color.
     */
    public static Color getHSL(double hue, double saturation, double lightness) {
        checkArgument(0 <= hue && hue <= 360, "hue must be a value from 0 to 360");
        checkArgument(0 <= saturation && saturation <= 1, "saturation must be a value from 0 to 1");
        checkArgument(0 <= lightness && lightness <= 1, "lightness must be a value from 0 to 1");

        // https://en.wikipedia.org/wiki/HSL_and_HSV#From_HSL
        double c = (1 - abs(2 * lightness - 1)) * saturation;
        double hp = hue / 60;
        double x = c * (1 - abs(hp % 2 - 1));
        double r1, g1, b1;
        if(hp <= 1) {
            r1 = c;
            g1 = x;
            b1 = 0;
        }
        else if(hp <= 2) {
            r1 = x;
            g1 = c;
            b1 = 0;
        }
        else if(hp <= 3) {
            r1 = 0;
            g1 = c;
            b1 = x;
        }
        else if(hp <= 4) {
            r1 = 0;
            g1 = x;
            b1 = c;
        }
        else if(hp <= 5) {
            r1 = x;
            g1 = 0;
            b1 = c;
        }
        else {
            r1 = c;
            g1 = 0;
            b1 = x;
        }
        double m = lightness - 0.5 * c;
        int r, g, b;
        r = (int) ((r1 + m) * 255);
        g = (int) ((g1 + m) * 255);
        b = (int) ((b1 + m) * 255);
        return getRGB(r, g, b);

    }

    public static Color getWhite() {
        return getRGB(255, 255, 255);
    }

    /**
     * Gets the red component
     * @return An integer value representing the red component (0-255)
     */
    public abstract int getRed();
    
    /**
     * Gets the green component
     * @return An integer value representing the green component (0-255)
     */
    public abstract int getGreen();

    /**
     * Gets the blue component
     * @return An integer value representing the blue component (0-255)
     */
    public abstract int getBlue();

    /**
     * Gets the Hex RGB value
     */
    @Nonnull
    @JsonValue
    public String getHex() {
        String r = pad(toHexString(getRed()));
        String g = pad(toHexString(getGreen()));
        String b = pad(toHexString(getBlue()));
        return "#" + r + g + b;
    }

    private static String pad(String hexValue) {
        return padStart(hexValue, 2, '0');
    }
}
