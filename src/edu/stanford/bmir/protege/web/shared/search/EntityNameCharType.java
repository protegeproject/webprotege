package edu.stanford.bmir.protege.web.shared.search;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/11/2013
 */
public enum EntityNameCharType {

    UPPER_CASE_LETTER(LetterCategory.WORD),

    LETTER(LetterCategory.WORD),

    DIGIT(LetterCategory.WORD),

    BOUNDARY(LetterCategory.NON_WORD),

    ESCAPING_QUOTE(LetterCategory.NON_WORD);



    private static final char SINGLE_QUOTE = '\'';

    private LetterCategory letterCategory;

    private EntityNameCharType(LetterCategory letterCategory) {
        this.letterCategory = letterCategory;
    }

    /**
     * Determines if this is character has a {@link LetterCategory#WORD} category.
     *
     * @return {@code true} if this character has a category of {@link LetterCategory#WORD}, otherwise {@code false}.
     */
    public boolean isWordLetter() {
        return letterCategory == LetterCategory.WORD;
    }

    /**
     * Gets the {@link EntityNameCharType} of the character of the specified index in the specified string.
     * @param text The string to examine.
     * @param index The index of the character.
     * @return The {@link EntityNameCharType} of the character at position {@code index} in {@code text}.
     * @throws NullPointerException if {@code text} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code index} does not specify a character index in {@code text}.
     */
    public static EntityNameCharType getType(String text, int index) {
        checkNotNull(text);
        checkElementIndex(index, text.length());
        char ch = text.charAt(index);
        return getType(index, ch, text.length());
    }

    /**
     * Gets the character type (which is dependent on the position of the character in the string being considered).
     *
     * @param index  The index of the character in the string being considered.
     * @param ch     The character whose type is to be determined.
     * @param length The length of the string being considered.
     * @return The {@link EntityNameCharType} of the {@code ch}.  Not {@code null}.
     */
    public static EntityNameCharType getType(int index, char ch, int length) {
        if (Character.isUpperCase(ch)) {
            return UPPER_CASE_LETTER;
        }
        else if (Character.isLowerCase(ch)) {
            return LETTER;
        }
        else if (Character.isDigit(ch)) {
            return DIGIT;
        }
        else if (index == 0 && ch == SINGLE_QUOTE) {
            // Special handling for quoted names
            return ESCAPING_QUOTE;
        }
        else if (index == length - 1 && ch == SINGLE_QUOTE) {
            // Special handling for quoted names
            return ESCAPING_QUOTE;
        }
        else if (ch == '-') {
            return LETTER;
        }
        else if (ch == SINGLE_QUOTE) {
            return LETTER;
        }
        else if (ch == ' ') {
            return BOUNDARY;
        }
        else if(33 <= ch && ch <= 47) {
            return BOUNDARY;
        }
        else if(58 <= ch && ch <= 64) {
            return BOUNDARY;
        }
        else if(91 <= ch && ch <= 96) {
            return BOUNDARY;
        }
        else if(123 <= ch && ch <= 126) {
            return BOUNDARY;
        }
        else if (ch == '\t') {
            return BOUNDARY;
        }
        else if (ch == '\n') {
            return BOUNDARY;
        }
        else {
            return LETTER;
        }
    }

    /**
     * A maker for categories of letters.  Splits letters into word letters and non-word letters.
     */
    private static enum LetterCategory {
        WORD,
        NON_WORD
    }
}
