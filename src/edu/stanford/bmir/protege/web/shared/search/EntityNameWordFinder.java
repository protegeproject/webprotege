package edu.stanford.bmir.protege.web.shared.search;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;

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

    private EntityNameWordFinder() {

    }

    /**
     * Given a string that represents and entity name, finds the index of the next word in the name from a given index.
     * @param text The text containing the entity name.  Not {@code null}.
     * @param startIndex The position to begin searching from.  Words will be found at this position or after this position.
     * @return The index of the next word from the startIndex position, or -1 if there are no words after the specified
     * startIndex position.
     * @throws NullPointerException if {@code text} is {@code null}.
     * @throws IllegalArgumentException if {@code startIndex} is negative or is greater or equal to the length of {@code text}.
     */
    public static int indexOfWord(final String text, final int startIndex) {
        checkNotNull(text, "Input string must not be null");
        checkElementIndex(startIndex, text.length());
        final int length = text.length();
//        CharType prevType;
//        if(startIndex == 0) {
//            prevType = CharType.BOUNDARY;
//        }
//        else {
//            int previousIndex = startIndex - 1;
//            char previousChar = text.charAt(previousIndex);
//            prevType = CharType.getType(previousIndex, previousChar, length);
//        }
        for(int index = startIndex; index < length; index++) {
            if(isWordStart(text, index)) {
                return index;
            }
//            final char curChar = text.charAt(index);
//            final CharType type = CharType.getType(index, curChar, length);
//            if(type.isWordLetter()) {
//                if(type != prevType) {
//                    if(type != CharType.JOIN && prevType != CharType.JOIN) {
//                        return index;
//                    }
//                }
//                else {
//                    // Cover the case of a run of upper case letters terminated with a lower case letter or a join
//                    // e.g.  OWLClass.  OWL is one word.  Class is another word.
//                    if(type == CharType.UPPER_CASE_LETTER) {
//                        final boolean hasFollowingCharacter = index < length - 1;
//                        if (hasFollowingCharacter) {
//                            int nextIndex = index + 1;
//                            CharType nextType = CharType.getType(nextIndex, text.charAt(nextIndex), length);
//                            if(nextType == CharType.LETTER || nextType == CharType.JOIN) {
//                                return index;
//                            }
//                        }
//                    }
//                }
//            }
//            prevType = type;
        }
        return -1;
    }

    public static boolean isWordStart(final String text, int index) {
        checkNotNull(text);
        int length = text.length();
        checkElementIndex(index, length);
        CharType indexCharType = CharType.getType(text, index);
        if(!indexCharType.isWordLetter()) {
            // Not in a word at all
            return false;
        }
        if(index == 0) {
            // In a word and definitely at start
            return true;
        }
        CharType prevCharType = CharType.getType(text, index - 1);
        if(prevCharType != indexCharType) {
            // Special handling for CamelCase where lower case letters that follow uppercase letters are not word
            // starts.
            return !(indexCharType == CharType.LETTER && prevCharType == CharType.UPPER_CASE_LETTER);
        }
        // Special handling for CamelCase where uppercase letters followed by lower case letters are word starts
        // even if they are preceded by other upper case letters
        if(indexCharType == CharType.UPPER_CASE_LETTER) {
            boolean hasFollowingCharacter = index < length - 1;
            return hasFollowingCharacter && CharType.getType(text, index + 1) == CharType.LETTER;
        }
        return false;

    }

    public static int indexOfWordEnd(final String text, int startIndex) {
        checkNotNull(text, "Input string must not be null");
        checkElementIndex(startIndex, text.length());
        // Find the first word character
        final int firstWordChar = indexOfWordChar(text, startIndex);
        if(firstWordChar == -1) {
            return -1;
        }
        int lastIndex = firstWordChar + 1;
        for(int i = lastIndex; i < text.length(); i++) {
            if(isWordStart(text, i) || !CharType.getType(text, i).isWordLetter()) {
                break;
            }
            lastIndex++;
        }
        return lastIndex;
    }

    public static int indexOfWordChar(String text, int startIndex) {
        for(int i = startIndex ; i < text.length(); i++) {
            char curChar = text.charAt(i);
            CharType charType = CharType.getType(i, curChar, text.length());
            if(charType.isWordLetter()) {
                return i;
            }
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

        UPPER_CASE_LETTER(LetterCategory.WORD),

        LETTER(LetterCategory.WORD),

        DIGIT(LetterCategory.WORD),
        
        BOUNDARY(LetterCategory.NON_WORD),

        ESCAPING_QUOTE(LetterCategory.NON_WORD);

        public static final char SINGLE_QUOTE = '\'';

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

        public static CharType getType(String text, int index) {
            char ch = text.charAt(index);
            return getType(index, ch, text.length());
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
                return UPPER_CASE_LETTER;
            }
            else if(Character.isLowerCase(ch)) {
                return LETTER;
            }
            else if(Character.isDigit(ch)) {
                return DIGIT;
            }
            else if(index == 0 && ch == SINGLE_QUOTE) {
                // Special handling for quoted names
                return ESCAPING_QUOTE;
            }
            else if(index == length - 1 && ch == SINGLE_QUOTE) {
                // Special handling for quoted names
                return ESCAPING_QUOTE;
            }
            else if(ch == ' ') {
                return BOUNDARY;
            }
            else if(ch == '_') {
                return BOUNDARY;
            }
            else if(ch == ',') {
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
                return LETTER;
            }
        }
    }
}
