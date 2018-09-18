package edu.stanford.bmir.protege.web.server.util;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Apr 2018
 */
public class AlphaNumericStringComparator implements Comparator<String> {

    /**
     * A pattern that allows sequences of numeric characters and sequences of non-numeric characters
     * to be found.
     */
    private static final Pattern PATTERN = Pattern.compile("([0-9]+)|([^0-9]+)");

    /**
     * The pattern group that matches sequences of digits.
     */
    private static final int DIGITS = 1;

    /**
     * The pattern group that matches sequences of non-digits.
     */
    private static final int NON_DIGITS = 2;

    private static final int S1_BEFORE_S2 = -1;

    private static final int S1_AFTER_S2 = 1;

    private static final int S1_SAME_AS_S2 = 0;


    private static final AlphaNumericStringComparator COMPARATOR = new AlphaNumericStringComparator();


    @Nonnull
    public static AlphaNumericStringComparator alphaNumerically() {
        return COMPARATOR;
    }

    /**
     * Compares strings alpha-numerically.  This takes into consideration numbers in strings.
     * The string "S-100" will appear after "S-50" even though with traditional string comparison
     * this would be the other way round.  The compare function examines strings in segments of
     * numbers and letters (non-numbers).
     * @param s1 The first string to be compared with the second string.
     * @param s2 The second string to be compared with the first string.
     * @return An int &lt; 0 if s1 should come before s2.  An int &gt; 0 if s1 should come after
     * s2. An int equal to 0 if s1 is equal to s2.
     */
    @Override
    public int compare(@Nonnull String s1, @Nonnull String s2) {

        int fromPos1 = 0;
        int fromPos2 = 0;
        Matcher matcher1 = PATTERN.matcher(s1);
        Matcher matcher2 = PATTERN.matcher(s2);
        int foundInS1Count = 0;
        int foundInS2Count = 0;
        while (foundInS1Count == foundInS2Count) {
            boolean foundInS1 = matcher1.find(fromPos1);
            boolean foundInS2 = matcher2.find(fromPos2);

            // If we did not find either then the whole string must be the same at this point
            if(!foundInS1 && !foundInS2) {
                return S1_SAME_AS_S2;
            }

            if(foundInS1) {
                foundInS1Count++;
                fromPos1 = matcher1.end();
            }
            if(foundInS2) {
                foundInS2Count++;
                fromPos2 = matcher2.end();
            }
            // Up until this point segments have matched
            // each other.  However, now the segments have diverged.
            // Less segments comes before more segments.
            if(foundInS1Count < foundInS2Count) {
                return S1_BEFORE_S2;
            }
            else if(foundInS1Count > foundInS2Count) {
                return S1_AFTER_S2;
            }
            // Comparing segments at the same ordinal
            int matchType1 = getMatchType(matcher1);
            int matchType2 = getMatchType(matcher2);

            // Digits come before non-digits
            if(matchType1 != matchType2) {
                return matchType1 - matchType2;
            }
            // Same segment type
            if(matchType1 == DIGITS) {
                // Segments are digits
                String s1Digits = matcher1.group(DIGITS);
                long n1 = Long.parseLong(s1Digits);
                String s2Digits = matcher2.group(DIGITS);
                long n2 = Long.parseLong(s2Digits);
                // Smaller numbers before larger numbers
                if(n1 < n2) {
                    return S1_BEFORE_S2;
                }
                else if(n1 > n2) {
                    return S1_AFTER_S2;
                }
                else {
                    // Numbers compare equal.  If the strings are different lengths then
                    // place the shortest string first
                    if(s1Digits.length() < s2Digits.length()) {
                        return S1_BEFORE_S2;
                    }
                    else if(s1Digits.length() > s2Digits.length()) {
                        return S1_AFTER_S2;
                    }
                }
            }
            else {
                // Segments are letters.  We perform a case insensitive diff
                String s1s = matcher1.group(NON_DIGITS);
                String s2s = matcher2.group(NON_DIGITS);
                int diff = s1s.compareToIgnoreCase(s2s);
                if(diff != S1_SAME_AS_S2) {
                    return diff;
                }
            }
        }
        // Final case-sensitive diff
        return s1.compareTo(s2);
    }

    /**
     * Gets the match type (which corresponds to the pattern group) for the
     * specified matcher.
     * @param matcher The matcher.  This must be in a state where a match has been found.
     * @return The match type, which is either DIGITS or NON_DIGITS.
     */
    private static int getMatchType(@Nonnull Matcher matcher) {
        if(matcher.group(DIGITS) != null) {
            return DIGITS;
        }
        else {
            return NON_DIGITS;
        }
    }
}
