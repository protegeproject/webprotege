package edu.stanford.bmir.protege.web.server.match;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringHasUntrimmedSpaceMatcher implements Matcher<String> {

    @Override
    public boolean matches(@Nonnull String value) {
        return value.endsWith(" ") || value.startsWith(" ");
    }
}
