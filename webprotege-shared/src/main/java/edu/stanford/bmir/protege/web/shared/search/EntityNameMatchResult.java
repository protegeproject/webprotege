package edu.stanford.bmir.protege.web.shared.search;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/11/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityNameMatchResult implements Comparable<EntityNameMatchResult> {

    public static EntityNameMatchResult get(int start, int end, EntityNameMatchType matchType, PrefixNameMatchType prefixNameMatchType) {
        checkArgument(start > -1);
        checkArgument(end > -1);
        checkArgument(start <= end);
        return new AutoValue_EntityNameMatchResult(start, end, matchType, prefixNameMatchType);
    }

    public abstract int getStart();

    public abstract int getEnd();

    public abstract EntityNameMatchType getMatchType();

    public abstract PrefixNameMatchType getPrefixNameMatchType();

    @Override
    public int compareTo(@Nonnull EntityNameMatchResult entityNameMatchResult) {
        final int typeDiff = this.getMatchType().compareTo(entityNameMatchResult.getMatchType());
        if(typeDiff != 0) {
            return typeDiff;
        }
        final int prefixNameMatchTypeDiff = this.getPrefixNameMatchType().compareTo(entityNameMatchResult.getPrefixNameMatchType());
        if(prefixNameMatchTypeDiff != 0) {
            return prefixNameMatchTypeDiff;
        }
        final int startDiff = this.getStart() - entityNameMatchResult.getStart();
        if(startDiff != 0) {
            return startDiff;
        }
        return this.getEnd() - entityNameMatchResult.getEnd();
    }
}
