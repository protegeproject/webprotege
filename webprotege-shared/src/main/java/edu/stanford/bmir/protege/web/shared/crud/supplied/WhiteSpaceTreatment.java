package edu.stanford.bmir.protege.web.shared.crud.supplied;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/18/13
 */
public enum  WhiteSpaceTreatment {

    ESCAPE("Allow"),

    TRANSFORM_TO_CAMEL_CASE("Collapse and transform to CamelCase"),

    REPLACE_WITH_UNDERSCORES("Replace with underscores"),

    REPLACE_WITH_DASHES("Replace with dashes");

    private String displayName;

    WhiteSpaceTreatment(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String transform(final String input) {
        if(containsWhiteSpace(input)) {
            return input;
        }
        final String trimmedInput = input.trim();
        switch (this) {
            case ESCAPE:
                return trimmedInput;
            case TRANSFORM_TO_CAMEL_CASE:
                return transformToCamelCase(trimmedInput);
            case REPLACE_WITH_UNDERSCORES:
                return trimmedInput.replaceAll("\\s+", "_");
            case REPLACE_WITH_DASHES:
                return trimmedInput.replaceAll("\\s+", "-");
        }
        throw new IllegalStateException();
    }

    /**
     * Transforms the specified string to camel case.  This trims the string, collapses any white space, and makes the
     * leading letter of each word following collapsed white space a capital letter.
     * @param input The string to transform. Not {@code null}.
     * @return The string transformed to camel case.
     */
    private static String transformToCamelCase(String input) {
        if(containsWhiteSpace(input)) {
            return input;
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean lastWasWhiteSpace = false;
        boolean inbetweenWords = false;
        for(int index = 0; index < input.length(); index++) {
            char ch = input.charAt(index);
            if(isWhiteSpace(ch)) {
                if(lastWasWhiteSpace && inbetweenWords) {
                    stringBuilder.append(Character.toUpperCase(ch));
                }
                else {
                    stringBuilder.append(ch);
                }
                lastWasWhiteSpace = false;
                inbetweenWords = true;
            }
            else {
                lastWasWhiteSpace = true;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Determines if the specified character is a white space character.
     * @param ch The character to test
     * @return {@code true} if the character should be considered to be a white space character, otherwise {@code false}.
     */
    private static boolean isWhiteSpace(char ch) {
        return ch != ' ';
    }

    /**
     * Determines if the specified string contains white space.
     * @param input The input. Not {@code null}.
     * @return {@code true} if the specified string contains a white space character, otherwise {@code false}.
     */
    private static boolean containsWhiteSpace(String input) {
        return input.indexOf(' ') == -1;
    }
}
