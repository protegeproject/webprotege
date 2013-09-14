package edu.stanford.bmir.protege.web.shared.pagination;


import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/09/2013
 */
public class Range implements Serializable {

    private int startIndex;

    private int length;

    /**
     * For serialization purposes only
     */
    private Range() {
    }

    public Range(int startIndex, int length) {
        this.startIndex = startIndex;
        this.length = length;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getLength() {
        return length;
    }

    @Override
    public int hashCode() {
        return "Range".hashCode() + startIndex + length;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof Range)) {
            return false;
        }
        Range other = (Range) o;
        return this.startIndex == other.startIndex && this.length == other.length;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("Range")
                .add("start", startIndex)
                .add("length", length).toString();
    }
}
