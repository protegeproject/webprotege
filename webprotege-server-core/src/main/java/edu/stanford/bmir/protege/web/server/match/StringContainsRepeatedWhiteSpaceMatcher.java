package edu.stanford.bmir.protege.web.server.match;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class StringContainsRepeatedWhiteSpaceMatcher implements Matcher<String> {

    @Override
    public boolean matches(@Nonnull String value) {
        int spaceCount = 0;
        for(int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if(ch == ' ') {
                spaceCount++;
            }
            else {
                spaceCount = 0;
            }
            if(spaceCount > 1) {
                return true;
            }
        }
        return false;
    }
}
