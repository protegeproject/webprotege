package edu.stanford.bmir.protege.web.shared.lang;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class LanguageTagFormatter {

    public static String format(@Nonnull String langTag) {
        if(langTag.isEmpty()) {
            return langTag;
        }
        String [] subtags = langTag.split("\\-");
        StringBuilder sb = new StringBuilder();
        boolean followsSingleton = false;
        for(int i = 0; i < subtags.length; i++) {
            String subtag = subtags[i];
            if(i == 0) {
                // Lower Case
                sb.append(subtags[0].toLowerCase());
            }
            // Not at start
            else if(!followsSingleton) {
                if(subtag.length() == 2) {
                    // Upper Case
                    sb.append(subtag.toUpperCase());
                }
                else if(subtag.length() == 4) {
                    // Title Case
                    sb.append(Character.toUpperCase(subtag.charAt(0)));
                    sb.append(subtag.substring(1).toLowerCase());
                }
                else {
                    // Lower Case
                    sb.append(subtag.toLowerCase());
                }
            }
            else {
                // Lower case
                sb.append(subtag.toLowerCase());
            }

            if(sb.length() < langTag.length()) {
                sb.append("-");
            }
            followsSingleton = subtag.length() == 1;
        }
        return sb.toString();
    }
}
