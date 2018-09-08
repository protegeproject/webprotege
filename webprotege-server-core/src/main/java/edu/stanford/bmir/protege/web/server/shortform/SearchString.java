package edu.stanford.bmir.protege.web.server.shortform;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.stream.Collectors.toList;

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
        if (rawSearchString.startsWith("*")) {
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

    public static List<SearchString> parseMultiWordSearchString(@Nonnull String query) {
        if(query.isEmpty()) {
            return Collections.emptyList();
        }
        return Stream.of(query.split("\\s+|_|:"))
                     .filter(s -> !s.isEmpty())
                     .map(SearchString::parseSearchString)
                     .collect(toList());
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
        return rawSearchString.length() > 0 && rawSearchString.charAt(0) == '*';
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
