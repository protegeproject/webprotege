package edu.stanford.bmir.protege.web.server.shortform;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Apr 2018
 */
public class Scanner {

    @Nonnull
    private final String shortForm;

    @Nonnull
    private final String lowerCaseShortForm;

    private int position = 0;

    private char previousCh = 0;

    public Scanner(@Nonnull String shortForm,
                   @Nonnull String lowerCaseShortForm) {
        this.shortForm = checkNotNull(shortForm);
        this.lowerCaseShortForm = checkNotNull(lowerCaseShortForm);
    }

    /**
     * Scans the short form, in a case insensitive way, for the specified {@link SearchString}.
     * @param searchString The search string.
     * @param start The index of where to start the scan.
     * @return The index of the specified {@link SearchString} in the short form scanned by this scanner.
     */
    public int indexOf(@Nonnull SearchString searchString, int start) {
        for (position = start; position < shortForm.length(); position++) {
            char ch = shortForm.charAt(position);
            if (searchString.isWildCard()) {
                // We can start the match anywhere in the string
                if(searchString.matches(lowerCaseShortForm, position)) {
                    return position;
                }
            }
            else {
                // We only start the match at transitions (new words)
                if (isTransition(ch)) {
                    if (searchString.matches(lowerCaseShortForm, position)) {
                        return position;
                    }
                }
            }
            previousCh = ch;
        }
        return -1;
    }

    private boolean isTransition(char ch) {
        return position == 0 || isWordBoundaryTransition(ch) || isCaseTransition(ch) || isNumericTransition(ch);
    }

    private boolean isCaseTransition(char ch) {
        return Character.isUpperCase(ch) && !Character.isUpperCase(previousCh);
    }

    private boolean isWordBoundaryTransition(char ch) {
        if (position == 0) {
            return true;
        }
        else if (Character.isLetter(ch)) {
            return !Character.isLetter(previousCh);
        }
        else {
            return Character.isLetter(previousCh);
        }
    }

    private boolean isNumericTransition(char ch) {
        if (isNumeric(ch)) {
            return !isNumeric(previousCh);
        }
        else {
            return isNumeric(previousCh);
        }
    }

    private boolean isNumeric(char ch) {
        return '0' <= ch && ch <= '9';
    }
}
