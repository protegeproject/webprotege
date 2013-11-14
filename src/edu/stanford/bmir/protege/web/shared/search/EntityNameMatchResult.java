package edu.stanford.bmir.protege.web.shared.search;

import com.google.common.base.Objects;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameMatchResult implements Serializable {

    private int start;

    private int end;

    private EntityNameMatchType matchType;

    @SuppressWarnings("unused")
    private EntityNameMatchResult() {
    }

    public EntityNameMatchResult(String entityName, int start, int end, EntityNameMatchType matchType) {
        this.start = checkPositionIndex(start, entityName.length());
        this.end = checkPositionIndex(end, entityName.length());
        this.matchType = checkNotNull(matchType);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public EntityNameMatchType getMatchType() {
        return matchType;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("EntityNameMatchResult")
                .add("start", start)
                .add("end", end)
                .add("type", matchType).toString();
    }
}
