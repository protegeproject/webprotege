package edu.stanford.bmir.protege.web.server.shortform;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

import java.util.Comparator;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 */
@AutoValue
public abstract class ShortFormMatchPosition implements Comparable<ShortFormMatchPosition> {

    public static final Comparator<ShortFormMatchPosition> comparator = Comparator.comparing(ShortFormMatchPosition::getStart)
                                                                                  .thenComparing(ShortFormMatchPosition::getEnd);

    @Nonnull
    public static ShortFormMatchPosition get(int start, int end) {
        checkArgument(start <= end);
        checkArgument(0 <= start);
        return new AutoValue_ShortFormMatchPosition(start, end);
    }

    public abstract int getStart();

    public abstract int getEnd();

    @Override
    public int compareTo(ShortFormMatchPosition o) {
        return comparator.compare(this, o);
    }
}
