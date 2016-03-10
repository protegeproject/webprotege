package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.auth.Md5DigestAlgorithmProvider;
import edu.stanford.bmir.protege.web.shared.auth.MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserIdInitialsExtractor;

import java.math.BigInteger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public class UserIcon {

    private UserIcon() {
    }

    public static IsWidget get(UserId userId) {
        String text = UserIdInitialsExtractor.getInitials(userId);
        String color = getColor(userId);
        UserIconView icon = new UserIconView();
        icon.setText(text);
        icon.setColor(color);
        return icon;
    }



    private static String getColor(UserId userId) {
        float hue = getHue(userId.getUserName());
        float sat = 0.4f;
        float brt = 0.9f;
        return hsvToRgb(hue, sat, brt);
    }

    private static float getHue(String name) {
        MessageDigestAlgorithm alg = new Md5DigestAlgorithmProvider().get();
        alg.update(name.getBytes());
        byte[] hash = alg.computeDigest();
        BigInteger bi = new BigInteger(hash);
        double percentageHue = Math.abs(bi.longValue() * 1.0 / Long.MAX_VALUE);
        // Bound the value so that it's not a Red colour
        double minHue = 25;
        double maxHue = 340;
        return (float) ((minHue + percentageHue * (maxHue - minHue))  / 360.0);
    }

    private static String hsvToRgb(float hue, float saturation, float value) {

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return rgbToString(value, t, p);
            case 1: return rgbToString(q, value, p);
            case 2: return rgbToString(p, value, t);
            case 3: return rgbToString(p, q, value);
            case 4: return rgbToString(t, p, value);
            case 5: return rgbToString(value, p, q);
            default: throw new RuntimeException();
        }
    }

    private static String rgbToString(float r, float g, float b) {
        String rs = Integer.toHexString((int)(r * 256));
        if(rs.length() != 2) {
            rs = "0" + rs;
        }
        String gs = Integer.toHexString((int)(g * 256));
        if(gs.length() != 2) {
            gs = "0" + gs;
        }
        String bs = Integer.toHexString((int)(b * 256));
        if(bs.length() != 2) {
            bs = "0" + bs;
        }

        return "#" + rs + gs + bs;
    }

}
