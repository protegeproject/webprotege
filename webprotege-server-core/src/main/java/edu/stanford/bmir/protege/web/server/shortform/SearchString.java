package edu.stanford.bmir.protege.web.server.shortform;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Apr 2018
 */
public class SearchString {

    private final String rawSearchString;

    private final String searchString;

    private SearchString(@Nonnull String rawSearchString) {
        this.rawSearchString = rawSearchString;
        if(rawSearchString.startsWith("*")) {
            searchString = rawSearchString.substring(1).toLowerCase();
        }
        else {
            searchString = rawSearchString.toLowerCase();
        }
    }

    @Nonnull
    public static SearchString parseSearchString(@Nonnull String query) {
        return new SearchString(query);
    }

    public String getRawSearchString() {
        return rawSearchString;
    }

    public String getSearchString() {
        return searchString;
    }

    public int length() {
        return searchString.length();
    }

    public boolean isWildCard() {
        return rawSearchString.charAt(0) == '*';
    }

    public boolean matches(@Nonnull String string, int start) {
        return string.startsWith(searchString, start);
    }


    @Override
    public String toString() {
        return toStringHelper("SearchString")
                .addValue(rawSearchString)
                .toString();
    }
}
