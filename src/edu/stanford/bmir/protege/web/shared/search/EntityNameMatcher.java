package edu.stanford.bmir.protege.web.shared.search;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.EntityNameUtils;

import java.util.regex.Matcher;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameMatcher {

    private String searchString;

    public EntityNameMatcher(String searchString) {
        this.searchString = checkNotNull(searchString);
    }

    public Optional<EntityNameMatchResult> findIn(String entityName) {
        Optional<EntityNameMatchResult> exactMatchResult = searchForExactEntityNameMatch(entityName);
        if(exactMatchResult.isPresent()) {
            return exactMatchResult;
        }
        // Search for a substring.  We try to find the best match in terms of a word match, word prefix match or
        // substring match
        return searchForBestPartialEntityNameMatch(entityName);
    }

    private Optional<EntityNameMatchResult> searchForExactEntityNameMatch(String entityName) {
        // Base case.  Exact match, quoted or unquoted.
        if (entityName.equalsIgnoreCase(searchString)) {
            return Optional.of(createExactMatchResultForString(entityName));
        }
        if (EntityNameUtils.isQuoted(entityName) && entityName.substring(1, entityName.length() - 1).equalsIgnoreCase(searchString)) {
            return Optional.of(createExactMatchResultForQuotedString(entityName));
        }
        return Optional.absent();
    }

    private Optional<EntityNameMatchResult> searchForBestPartialEntityNameMatch(String entityName) {
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
        return getEntityNameMatchResult(entityName, matchIndexHelper);
    }

    private Optional<EntityNameMatchResult> getEntityNameMatchResult(String entityName, MatchIndexHelper matchIndexHelper) {
        final EntityNameMatchType matchType = matchIndexHelper.getBestMatchType();
        if (matchType != EntityNameMatchType.NONE) {
            int matchIndex = matchIndexHelper.getBestMatchIndex();
            return Optional.of(new EntityNameMatchResult(matchIndex, matchIndex + searchString.length(), matchType));
        }
        else {
            return Optional.absent();
        }
    }

    private EntityNameMatchResult createExactMatchResultForQuotedString(String text) {
        return new EntityNameMatchResult(1, text.length() - 1, EntityNameMatchType.EXACT_MATCH);
    }

    private EntityNameMatchResult createExactMatchResultForString(String text) {
        return new EntityNameMatchResult(0, text.length(), EntityNameMatchType.EXACT_MATCH);
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
    }
}
