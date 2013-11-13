package edu.stanford.bmir.protege.web.shared.search;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameWordFinder {

    public int findNextWord(String text, int start) {
        checkNotNull(text, "Input string must not be null");
        if(start < 0) {
            throw new IndexOutOfBoundsException("start is less than zero");
        }
        final int length = text.length();
        if(!(start < length)) {
            throw new IndexOutOfBoundsException("start >= input string length");
        }
        CharType prevType;
        if(start == 0) {
            prevType = CharType.BOUNDARY;
        }
        else {
            prevType = CharType.getType(start - 1, text.charAt(start), length);
        }
        for(int i = start; i < length; i++) {
            char cur = text.charAt(i);
            CharType type = CharType.getType(i, cur, length);
            if(type.isLetterCategory(LetterCategory.WORD)) {
                boolean hasFollowingCharacter = i < length - 1;
                // Special treatment for uppercase followed by lower case
                if(type == CharType.UPPER_CASE && hasFollowingCharacter) {
                    int nextIndex = i + 1;
                    CharType nextType = CharType.getType(nextIndex, text.charAt(nextIndex), length);
                    if(nextType == CharType.LOWER_CASE || nextType == CharType.JOIN) {
                        return i;
                    }
                }
                if(type != prevType) {
                    if(type != CharType.JOIN && prevType != CharType.JOIN) {
                        return i;
                    }
                }
            }
            prevType = type;
        }
        return -1;
    }



    private static enum LetterCategory {

        WORD,

        NON_WORD
    }


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

        private boolean isLetterCategory(LetterCategory letterCategory) {
            return this.letterCategory == letterCategory;
        }

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
