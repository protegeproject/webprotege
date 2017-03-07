package edu.stanford.bmir.protege.web.server.notes;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/04/2012
 * <p>
 *     Thrown by {@link StarRating#parseStarRating(String)} to indicate that the specified string representation of
 *     a StarRating is not formatted in a syntactically valid way.
 * </p>
 */
@Deprecated
public class StarRatingFormatException extends RuntimeException {

    private String specifiedString;

    public StarRatingFormatException(String specifiedString) {
        super("Incorrect Star Rating Format: " + specifiedString);
        this.specifiedString = specifiedString;
    }

    /**
     * Gets the string that could not be parsed into a StarRating.
     * @return The string.
     */
    public String getSpecifiedString() {
        return specifiedString;
    }
}
