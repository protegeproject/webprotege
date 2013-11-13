package edu.stanford.bmir.protege.web.shared.search;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 * <p>
 *     Finds the starts of words in entity names.  The finder takes into consideration names surrounded with single
 *     quotes, names that are written in camel case and names that have other delimeters e.g. spaces or underscores.
 * </p>
 */
public class EntityNameWordFinder {

    /**
     * Given a string that represents and entity name, finds the index of the next word in the name from a given index.
     * @param text The text containing the entity name.  Not {@code null}.
     * @param startIndex The position to begin searching from.  Words will be found at this position or after this position.
     * @return The index of the next word from the startIndex position, or -1 if there are no words after the specified
     * startIndex position.
     * @throws NullPointerException if {@code text} is {@code null}.
     * @throws IllegalArgumentException if {@code startIndex} is negative or is greater or equal to the length of {@code text}.
     */
    public int findNextWord(final String text, final int startIndex) {
        checkNotNull(text, "Input string must not be null");
        checkElementIndex(startIndex, text.length());
        final int length = text.length();
        CharType prevType;
        if(startIndex == 0) {
            prevType = CharType.BOUNDARY;
        }
        else {
            int previousIndex = startIndex - 1;
            char previousChar = text.charAt(previousIndex);
            prevType = CharType.getType(previousIndex, previousChar, length);
        }
        for(int index = startIndex; index < length; index++) {
            final char curChar = text.charAt(index);
            final CharType type = CharType.getType(index, curChar, length);
            if(type.isWordLetter()) {
                if(type != prevType) {
                    if(type != CharType.JOIN && prevType != CharType.JOIN) {
                        return index;
                    }
                }
                else {
                    // Cover the case of a run of upper case letters terminated with a lower case letter or a join
                    // e.g.  OWLClass.  OWL is one word.  Class is another word.
                    if(type == CharType.UPPER_CASE) {
                        final boolean hasFollowingCharacter = index < length - 1;
                        if (hasFollowingCharacter) {
                            int nextIndex = index + 1;
                            CharType nextType = CharType.getType(nextIndex, text.charAt(nextIndex), length);
                            if(nextType == CharType.LOWER_CASE || nextType == CharType.JOIN) {
                                return index;
                            }
                        }
                    }
                }
            }
            prevType = type;
        }
        return -1;
    }

    /**
     * A maker for categories of letters.  Splits letters into word letters and non-word letters.
     */
    private static enum LetterCategory {

        WORD,

        NON_WORD
    }

    /**
     * A list of the different types of characters that we consider.
     */
    private static enum CharType {

        UPPER_CASE(LetterCategory.WORD),

        LOWER_CASE(LetterCategory.WORD),

        OTHER(LetterCategory.WORD),

        DIGIT(LetterCategory.WORD),
        
        JOIN(LetterCategory.WORD),

        BOUNDARY(LetterCategory.NON_WORD),

        QUOTE(LetterCategory.NON_WORD);


        private LetterCategory letterCategory;

        private CharType(LetterCategory letterCategory) {
            this.letterCategory = letterCategory;
        }

        /**
         * Determines if this is character has a {@link LetterCategory#WORD} category.
         * @return {@code true} if this character has a category of {@link LetterCategory#WORD}, otherwise {@code false}.
         */
        private boolean isWordLetter() {
            return letterCategory == LetterCategory.WORD;
        }

        /**
         * Gets the character type (which is dependent on the position of the character in the string being considered).
         * @param index The index of the character in the string being considered.
         * @param ch The character whose type is to be determined.
         * @param length The length of the string being considered.
         * @return The {@link CharType} of the {@code ch}.  Not {@code null}.
         */
        public static CharType getType(int index, char ch, int length) {
            if(Character.isUpperCase(ch)) {
                return UPPER_CASE;
            }
            else if(Character.isLowerCase(ch)) {
                return LOWER_CASE;
            }
            else if(Character.isDigit(ch)) {
                return DIGIT;
            }
            else if(index == 0 && ch == '\'') {
                // Special handling for quoted names
                return QUOTE;
            }
            else if(index == length - 1 && ch == '\'') {
                // Special handling for quoted names
                return QUOTE;
            }
            else if(ch == '-') {
                return JOIN;
            }
            else if(ch == ' ') {
                return BOUNDARY;
            }
            else if(ch == '_') {
                return BOUNDARY;
            }
            else if(ch == '\\') {
                return BOUNDARY;
            }
            else if(ch == ':') {
                return BOUNDARY;
            }
            else if(ch == '(') {
                return BOUNDARY;
            }
            else if(ch == ')') {
                return BOUNDARY;
            }
            else if(ch == '[') {
                return BOUNDARY;
            }
            else if(ch == ']') {
                return BOUNDARY;
            }
            else if(ch == '{') {
                return BOUNDARY;
            }
            else if(ch == '}') {
                return BOUNDARY;
            }
            else if(ch == '<') {
                return BOUNDARY;
            }
            else if(ch == '>') {
                return BOUNDARY;
            }
            else if(ch == '#') {
                return BOUNDARY;
            }
            else {
                return OTHER;
            }
        }
    }
}
