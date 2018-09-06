package edu.stanford.bmir.protege.web.shared.search;

import edu.stanford.bmir.protege.web.shared.entity.EntityNameUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 * <p>
 *     A utility for searching for substrings in entity names.  Matches that match whole entity names (quoted
 *     or unquoted) will be returned first, then matches that match whole words in entity names, then matches that
 *     math word prefixes in entity names, then matches that match substrings in entity names.
 *
 *     If the entity name is a prefix name, matches that occur after the prefix name will be found in
 *     preferences to possible matches that would occur in the prefix name.
 *
 *     For example, given a search string of "bc", and an entity name of "abcBc", the "Bc" substring would be matched
 *     over the first "bc" substring.  For the same search string and an entity name of "bc:bc", the second occurrence
 *     of "bc" would be matched in preference to the first occurrence.
 * </p>
 */
public class EntityNameMatcher {

    private String searchString;

    /**
     * Constructs and {@link EntityNameMatcher} which searches for the specified string.
     * @param searchString The string to search with.  Not {@code null}.  May be empty.
     * @throws NullPointerException if {@code searchString} is {@code null}.
     */
    public EntityNameMatcher(String searchString) {
        this.searchString = checkNotNull(searchString);
    }

    /**
     * Finds this {@link EntityNameMatcher}'s search string in the specified entity name.
     * @param entityName A string representing the entity name.  Not {@code null}.
     * @return A search result that specifies the where the this {@link EntityNameMatcher}'s search string was found
     * in {@code entityName}.  Not {@code null}.  An absent value indicates that no match was found.
     * @throws NullPointerException if {@code entityName} is {@code null}.
     */
    public java.util.Optional<EntityNameMatchResult> findIn(String entityName) {
        checkNotNull(entityName);
        java.util.Optional<EntityNameMatchResult> exactMatchResult = searchForExactEntityNameMatch(entityName);
        if(exactMatchResult.isPresent()) {
            return exactMatchResult;
        }
        // Search for a substring.  We try to find the best match in terms of a word match, word prefix match or
        // substring match
        return searchForBestPartialEntityNameMatch(entityName);
    }

    private java.util.Optional<EntityNameMatchResult> searchForExactEntityNameMatch(String entityName) {
        // Base case.  Exact match, quoted or unquoted.
        if (entityName.equalsIgnoreCase(searchString)) {
            return java.util.Optional.of(createExactMatchResultForString(entityName));
        }
        if (EntityNameUtils.isQuoted(entityName) && entityName.substring(1, entityName.length() - 1).equalsIgnoreCase(searchString)) {
            return java.util.Optional.of(createExactMatchResultForQuotedString(entityName));
        }
        return java.util.Optional.empty();
    }

    private java.util.Optional<EntityNameMatchResult> searchForBestPartialEntityNameMatch(String entityName) {
        final int prefixNameSeparatorIndex = entityName.indexOf(':');
        MatchIndexHelper matchIndexHelper = new MatchIndexHelper(prefixNameSeparatorIndex);
        for (int index = 0; index < entityName.length(); index++) {
            index = indexOfIgnoreCase(searchString, entityName, index);
            if (index == -1) {
                break;
            }
            int nextWordStart = EntityNameUtils.indexOfWord(entityName, index);
            // Match on a word start
            if (nextWordStart == index) {
                // Definitely a word prefix match
                matchIndexHelper.encounteredWordPrefixMatch(index);
                int wordEndIndex = EntityNameUtils.indexOfWordEnd(entityName, index);
                if (wordEndIndex == index + searchString.length()) {
                    matchIndexHelper.encounteredWordMatch(index);
                    if (index > prefixNameSeparatorIndex) {
                        // Found the best whole word match,  No more to do.
                        break;
                    }
                }
            }
            else {
                matchIndexHelper.encounteredSubStringMatch(index);
            }
            index++;
        }
        return getEntityNameMatchResult(matchIndexHelper);
    }

    private java.util.Optional<EntityNameMatchResult> getEntityNameMatchResult(MatchIndexHelper matchIndexHelper) {
        final EntityNameMatchType matchType = matchIndexHelper.getBestMatchType();
        if (matchType != EntityNameMatchType.NONE) {
            int matchIndex = matchIndexHelper.getBestMatchIndex();
            return java.util.Optional.of(EntityNameMatchResult.get(matchIndex, matchIndex + searchString.length(), matchType, matchIndexHelper.getBestMatchPrefixNameMatchType()));
        }
        else {
            return java.util.Optional.empty();
        }
    }

    private EntityNameMatchResult createExactMatchResultForQuotedString(String text) {
        return EntityNameMatchResult.get(1, text.length() - 1, EntityNameMatchType.EXACT_MATCH, getPrefixNameMatchTypeForExactMatch(text));
    }

    private PrefixNameMatchType getPrefixNameMatchTypeForExactMatch(String text) {
        return text.indexOf(':') == -1 ? PrefixNameMatchType.NOT_IN_PREFIX_NAME : PrefixNameMatchType.IN_PREFIX_NAME;
    }

    private EntityNameMatchResult createExactMatchResultForString(String text) {
        return EntityNameMatchResult.get(0, text.length(), EntityNameMatchType.EXACT_MATCH, getPrefixNameMatchTypeForExactMatch(text));
    }

    private static int indexOfIgnoreCase(String searchFor, String in, int start) {
        // TODO: Optimise
        return in.toLowerCase().indexOf(searchFor.toLowerCase(), start);
    }

    private static class MatchIndexHelper {

        private int prefixNameSeparatorIndex = -1;

        private int wordMatchIndex = -1;

        private int wordPrefixMatchIndex = -1;

        private int subStringMatchIndex = -1;

        private MatchIndexHelper(int prefixNameSeparatorIndex) {
            this.prefixNameSeparatorIndex = prefixNameSeparatorIndex;
        }

        private boolean isAfterPrefixSeparator(int index) {
            return prefixNameSeparatorIndex != -1 && index > prefixNameSeparatorIndex;
        }

        private boolean isBeforePrefixSeparator(int index) {
            return index < prefixNameSeparatorIndex;
        }

        public void encounteredWordMatch(int index) {
            if (wordMatchIndex == -1) {
                wordMatchIndex = index;
            }
            else if (isBeforePrefixSeparator(wordMatchIndex) && isAfterPrefixSeparator(index)) {
                wordMatchIndex = index;
            }
        }

        public void encounteredWordPrefixMatch(int index) {
            if (wordPrefixMatchIndex == -1) {
                wordPrefixMatchIndex = index;
            }
            else if (isBeforePrefixSeparator(wordPrefixMatchIndex) && isAfterPrefixSeparator(index)) {
                wordPrefixMatchIndex = index;
            }
        }

        public void encounteredSubStringMatch(int index) {
            if (subStringMatchIndex == -1) {
                subStringMatchIndex = index;
            }
            else if (isBeforePrefixSeparator(subStringMatchIndex) && isAfterPrefixSeparator(index)) {
                subStringMatchIndex = index;
            }
        }

        public int getBestMatchIndex() {
            if (wordMatchIndex != -1) {
                return wordMatchIndex;
            }
            if (wordPrefixMatchIndex != -1) {
                return wordPrefixMatchIndex;
            }
            else {
                return subStringMatchIndex;
            }
        }

        public EntityNameMatchType getBestMatchType() {
            if (wordMatchIndex != -1) {
                return EntityNameMatchType.WORD_MATCH;
            }
            if (wordPrefixMatchIndex != -1) {
                return EntityNameMatchType.WORD_PREFIX_MATCH;
            }
            if (subStringMatchIndex != -1) {
                return EntityNameMatchType.SUB_STRING_MATCH;
            }
            return EntityNameMatchType.NONE;
        }

        public PrefixNameMatchType getBestMatchPrefixNameMatchType() {
            int matchIndex = getBestMatchIndex();
            if(matchIndex > prefixNameSeparatorIndex) {
                return PrefixNameMatchType.NOT_IN_PREFIX_NAME;
            }
            else {
                return PrefixNameMatchType.IN_PREFIX_NAME;
            }
//            if(prefixNameSeparatorIndex == -1) {
//                return PrefixNameMatchType.NOT_IN_PREFIX_NAME;
//            }
//            if(wordMatchIndex != -1 && wordMatchIndex > prefixNameSeparatorIndex) {
//                return PrefixNameMatchType.NOT_IN_PREFIX_NAME;
//            }
//            if(wordPrefixMatchIndex != -1 && wordPrefixMatchIndex > prefixNameSeparatorIndex) {
//                return PrefixNameMatchType.NOT_IN_PREFIX_NAME;
//            }
//            if(subStringMatchIndex != -1 && subStringMatchIndex > prefixNameSeparatorIndex) {
//                return PrefixNameMatchType.NOT_IN_PREFIX_NAME;
//            }
//            return PrefixNameMatchType.IN_PREFIX_NAME;
        }
    }
}
