package edu.stanford.bmir.protege.web.server.shortform;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Apr 2018
 */
public class ShortFormQuotingUtils {

    /**
     * Produces a quoted short form (if necessary).  If the original short form contains
     * spaces, braces, brackets or commas, and various other symbols used as delimeters in the
     * Manchester syntax parser then the returned value is the original rendering enclosed in
     * single quotes.
     *
     * @param shortForm The rendering to be escaped
     * @return The escaped rendering.
     */
    public static String getQuotedShortForm(String shortForm) {
        if (shortForm.indexOf(' ') != -1
                || shortForm.indexOf(',') != -1
                || shortForm.indexOf('<') != -1
                || shortForm.indexOf('>') != -1
                || shortForm.indexOf('=') != -1
                || shortForm.indexOf('^') != -1
                || shortForm.indexOf('@') != -1
                || shortForm.indexOf('{') != -1
                || shortForm.indexOf('}') != -1
                || shortForm.indexOf('[') != -1
                || shortForm.indexOf(']') != -1
                || shortForm.indexOf('(') != -1
                || shortForm.indexOf(')') != -1) {
            return "'" + shortForm + "'";
        }
        else {
            return shortForm;
        }
    }

    @Nonnull
    public static String getUnquotedShortForm(@Nonnull String rendering) {
        if (rendering.startsWith("'") && rendering.endsWith("'")) {
            return rendering.substring(1, rendering.length() - 1);
        }
        else {
            return rendering;
        }
    }
}
