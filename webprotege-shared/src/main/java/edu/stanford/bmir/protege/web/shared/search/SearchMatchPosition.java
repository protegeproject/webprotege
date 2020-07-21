package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-17
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class SearchMatchPosition {

    /**
     * The JSON property name for the match position start
     */
    public static final String START = "start";

    /**
     * The JSON property name for the match position end
     */
    public static final String END = "end";

    /**
     * Gets a search match position starting at the specified start index (inclusive)
     * and ending the specified end index (exclusive).  This works in the same way that
     * {@link String#substring(int, int)} works.
     * @param start The start position of the match (inclusive)
     * @param end The end position of the match (exclusive)
     * @return The match position.
     */
    @JsonCreator
    @Nonnull
    public static SearchMatchPosition get(@JsonProperty(START) int start,
                                          @JsonProperty(END) int end) {
        return new AutoValue_SearchMatchPosition(start, end);
    }

    /**
     * Gets the start of the search match.  This is the index of the first character that is matched (inclusive).
     * @return The start of the search match.
     */
    @JsonProperty(START)
    public abstract int getStart();

    /**
     * Gets the end of the search match.  This is the index after the last index matched (exclusive), in the
     * same way that substring ({@link String#substring(int, int)}) positions work.
     * @return The end of the search match
     */
    @JsonProperty(END)
    public abstract int getEnd();

}
