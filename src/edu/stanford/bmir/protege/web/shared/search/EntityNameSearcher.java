package edu.stanford.bmir.protege.web.shared.search;

import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameSearcher {

    public static final char SINGLE_QUOTE = '\'';

    private String searchString;

    public EntityNameSearcher(String searchString) {
        this.searchString = checkNotNull(searchString);
    }

    public Optional<EntityNameMatchResult> getMatchIn(String text) {
        // Base case.  Exact match, quoted or unquoted.
        if (text.equalsIgnoreCase(searchString)) {
            return Optional.of(createExactMatchResultForString(text));
        }
        if (EntityNameUtils.isQuoted(text) && text.substring(0, text.length()).equalsIgnoreCase(searchString)) {
            return Optional.of(createExactMatchResultForQuotedString(text));
        }
        // Search for a substring.  We try to find the best match in terms of a word match, word prefix match or
        // substring match
        int firstWordMatchIndex = -1;
        int firstWordPrefixMatchIndex = -1;
        int firstSubStringMatchIndex = -1;
        int index = 0;
        while (true) {
            index = indexOfIgnoreCase(searchString, text, index);
            if (index == -1) {
                break;
            }
            int nextWordStart = EntityNameUtils.indexOfWord(text, index);
            // Match on a word start
            if (nextWordStart == index) {
                // It is definitely a word prefix
                if (firstWordPrefixMatchIndex == -1) {
                    // Found the first word prefix match
                    firstWordPrefixMatchIndex = index;
                }
                if (index < text.length()) {
                    int wordEndIndex = EntityNameUtils.indexOfWordEnd(text, index);
                    if (wordEndIndex == index + searchString.length()) {
                        firstWordMatchIndex = index;
                        // Found the first whole word match,  No more to do.
                        break;
                    }
                }
            }
            else {
                if (firstSubStringMatchIndex == -1) {
                    // Found the first substring match
                    firstSubStringMatchIndex = index;
                }
            }
            index++;
        }
        final int matchIndex;
        final EntityNameMatchType matchType;
        if (firstWordMatchIndex != -1) {
            matchIndex = firstWordMatchIndex;
            matchType = EntityNameMatchType.WORD_MATCH;
        }
        else if (firstWordPrefixMatchIndex != -1) {
            matchIndex = firstWordPrefixMatchIndex;
            matchType = EntityNameMatchType.WORD_PREFIX_MATCH;
        }
        else if (firstSubStringMatchIndex != -1) {
            matchIndex = firstSubStringMatchIndex;
            matchType = EntityNameMatchType.SUB_STRING_MATCH;
        }
        else {
            matchIndex = -1;
            matchType = EntityNameMatchType.NONE;
        }
        if (matchType != EntityNameMatchType.NONE) {
            return Optional.of(new EntityNameMatchResult(text, matchIndex, matchIndex +  searchString.length(), matchType));
        }
        else {
            return Optional.absent();
        }
    }

    private EntityNameMatchResult createExactMatchResultForQuotedString(String text) {
        return new EntityNameMatchResult(text, 1, text.length() - 1, EntityNameMatchType.EXACT_MATCH);
    }

    private EntityNameMatchResult createExactMatchResultForString(String text) {
        return new EntityNameMatchResult(text, 0, text.length(), EntityNameMatchType.EXACT_MATCH);
    }

    private static int indexOfIgnoreCase(String searchFor, String in, int start) {
        // TODO: Optimise
        return in.toLowerCase().indexOf(searchFor.toLowerCase(), start);
    }
}
