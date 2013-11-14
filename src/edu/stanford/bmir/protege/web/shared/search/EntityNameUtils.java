package edu.stanford.bmir.protege.web.shared.search;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 * <p>
 *     Useful utilities for dealing with entity names.
 * </p>
 */
public class EntityNameUtils {

    public static final char SINGLE_QUOTE = '\'';

    public static final char ESCAPE_CHAR = '\\';

    private EntityNameUtils() {

    }

    /**
     * Determines if the specified entity name is quoted.
     * @param entityName The name to test.  Not {@code null}.
     * @return {@code true} if the name is quoted with single quotes, otherwise {@code false}.
     * @throws NullPointerException if {@code entityName} is {@code null}.
     */
    public static boolean isQuoted(String entityName) {
        checkNotNull(entityName);
        return entityName.length() >= 2 && entityName.charAt(0) == SINGLE_QUOTE && entityName.charAt(entityName.length() - 1) == SINGLE_QUOTE;
    }

    // TODO: Need tests
    private static String quoteIfNecessary(String entityName) {
        checkNotNull(entityName);
        if(!entityName.contains(" ")) {
            return entityName;
        }
        return quote(entityName);
    }

    // TODO: Need tests
    private static String quote(String entityName) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        for(int i = 0; i < entityName.length(); i++) {
            char curChar = entityName.charAt(i);
            if(curChar == SINGLE_QUOTE || curChar == ESCAPE_CHAR) {
                sb.append(ESCAPE_CHAR);
            }
            else {
                sb.append(curChar);
            }
        }
        sb.append("'");
        return sb.toString();
    }

    // TODO: Need tests
    private static String unquote(String entityName) {
        if(entityName.length() < 2) {
            return entityName;
        }
        if(!(entityName.charAt(0) == SINGLE_QUOTE && entityName.charAt(entityName.length() - 1) == SINGLE_QUOTE)) {
            return entityName;
        }
        if(entityName.indexOf('\'') == -1) {
            return entityName.substring(1, entityName.length() - 1);
        }
        else {
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < entityName.length() - 1; i++) {
                char curChar = entityName.charAt(i);
                if(curChar != ESCAPE_CHAR) {
                    sb.append(curChar);
                }
                else {
                    if(i + 1 < entityName.length()) {
                        sb.append(entityName.charAt(i + 1));
                        i = i + 1;
                    }
                    else {
                        sb.append(ESCAPE_CHAR);
                    }
                }
            }
            return sb.toString();
        }
    }

    /**
     * Determines if the index in the specified string, which represents an entity name, is the start of a word.
     * @param entityName The string.  Not {@code null}.
     * @param index The index to test.
     * @return {@code true} if {@code index} points to the start of the word in the string, otherwise {@code false}.
     * @throws NullPointerException {@code entityName} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code index} does not point to a valid position in {@code entityName}.
     */
    public static boolean isWordStart(final String entityName, int index) {
        checkNotNull(entityName);
        int length = entityName.length();
        checkElementIndex(index, length);
        EntityNameCharType indexCharType = EntityNameCharType.getType(entityName, index);
        if(!indexCharType.isWordLetter()) {
            // Not in a word at all
            return false;
        }
        if(index == 0) {
            // In a word and definitely at start
            return true;
        }
        EntityNameCharType prevCharType = EntityNameCharType.getType(entityName, index - 1);
        if(prevCharType != indexCharType) {
            // Special handling for CamelCase where lower case letters that follow uppercase letters are not word
            // starts.
            return !(indexCharType == EntityNameCharType.LETTER && prevCharType == EntityNameCharType.UPPER_CASE_LETTER);
        }
        // Special handling for CamelCase where uppercase letters followed by lower case letters are word starts
        // even if they are preceded by other upper case letters
        if(indexCharType == EntityNameCharType.UPPER_CASE_LETTER) {
            boolean hasFollowingCharacter = index < length - 1;
            return hasFollowingCharacter && EntityNameCharType.getType(entityName, index + 1) == EntityNameCharType.LETTER;
        }
        return false;
    }

    /**
     * Given a string that represents and entity name, finds the index of the next word in the name from a given index.
     * @param entityName The entityName containing the entity name.  Not {@code null}.
     * @param startIndex The position to begin searching from.  Words will be found at this position or after this position.
     * @return The index of the next word from the startIndex position, or -1 if there are no words after the specified
     * startIndex position.
     * @throws NullPointerException if {@code entityName} is {@code null}.
     * @throws IllegalArgumentException if {@code startIndex} is negative or is greater or equal to the length of {@code entityName}.
     */
    public static int indexOfWord(final String entityName, final int startIndex) {
        checkNotNull(entityName, "Input string must not be null");
        int length = entityName.length();
        checkElementIndex(startIndex, length);
        for(int index = startIndex; index < length; index++) {
            if(isWordStart(entityName, index)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Given a string that represents and entity name, finds the index of the next word end in the name from a given index.
     * @param entityName The entityName containing the entity name.  Not {@code null}.
     * @param startIndex The position to begin searching from.  Words ends will be found at this position or after this position.
     * @return The index of the next word from the startIndex position, or -1 if there are no word ends after the specified
     * startIndex position.
     * @throws NullPointerException if {@code entityName} is {@code null}.
     * @throws IllegalArgumentException if {@code startIndex} is negative or is greater or equal to the length of {@code entityName}.
     */
    public static int indexOfWordEnd(final String entityName, int startIndex) {
        checkNotNull(entityName);
        checkElementIndex(startIndex, entityName.length());
        // Find the first word character
        final int firstWordChar = indexOfWordChar(entityName, startIndex);
        if(firstWordChar == -1) {
            return -1;
        }
        int lastIndex = firstWordChar + 1;
        for(int i = lastIndex; i < entityName.length(); i++) {
            if(isWordStart(entityName, i) || !EntityNameCharType.getType(entityName, i).isWordLetter()) {
                break;
            }
            lastIndex++;
        }
        return lastIndex;
    }

    /**
     * Gets the index of the first word char at or after the specified start index.
     * @param entityName The entity name.  Not {@code null}.
     * @param startIndex The start index.
     * @return The index of the first word character encountered from {@code startIndex}.  This may be {@code startIndex}
     * itself.  -1 if there is no word character at {@code startIndex} of after {@code startIndex}.
     * @throws NullPointerException if {@code entityName} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code startIndex} does not point to a character in {@code entityName}.
     */
    public static int indexOfWordChar(String entityName, int startIndex) {
        for(int i = startIndex ; i < entityName.length(); i++) {
            char curChar = entityName.charAt(i);
            EntityNameCharType charType = EntityNameCharType.getType(i, curChar, entityName.length());
            if(charType.isWordLetter()) {
                return i;
            }
        }
        return -1;
    }
}
