package edu.stanford.bmir.protege.web.shared.search;

import com.google.common.base.Objects;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
public class EntityNameMatchResult implements Serializable, Comparable<EntityNameMatchResult> {

    private int start;

    private int end;

    private EntityNameMatchType matchType;

    private PrefixNameMatchType prefixNameMatchType;

    @SuppressWarnings("unused")
    private EntityNameMatchResult() {
    }

    public EntityNameMatchResult(int start, int end, EntityNameMatchType matchType, PrefixNameMatchType prefixNameMatchType) {
        checkArgument(start > -1);
        checkArgument(end > -1);
        checkArgument(start <= end);
        this.start = start;
        this.end = end;
        this.matchType = checkNotNull(matchType);
        this.prefixNameMatchType = checkNotNull(prefixNameMatchType);
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

    public PrefixNameMatchType getPrefixNameMatchType() {
        return prefixNameMatchType;
    }

    @Override
    public int hashCode() {
        return "EntityNameMatchResult".hashCode() + start + 13 * end + matchType.hashCode() + prefixNameMatchType.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof EntityNameMatchResult)) {
            return false;
        }
        EntityNameMatchResult other = (EntityNameMatchResult) o;
        return this.start == other.start && this.end == other.end && this.matchType == other.matchType && this.prefixNameMatchType == other.prefixNameMatchType;
    }

    @Override
    public int compareTo(EntityNameMatchResult entityNameMatchResult) {
        final int typeDiff = this.matchType.compareTo(entityNameMatchResult.matchType);
        if(typeDiff != 0) {
            return typeDiff;
        }
        final int prefixNameMatchTypeDiff = this.prefixNameMatchType.compareTo(entityNameMatchResult.prefixNameMatchType);
        if(prefixNameMatchTypeDiff != 0) {
            return prefixNameMatchTypeDiff;
        }
        final int startDiff = this.start - entityNameMatchResult.start;
        if(startDiff != 0) {
            return startDiff;
        }
        return this.end - entityNameMatchResult.end;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("EntityNameMatchResult")
                .add("start", start)
                .add("end", end)
                .add("matchType", matchType)
                .add("prefixNameMatchType", prefixNameMatchType).toString();
    }
}
