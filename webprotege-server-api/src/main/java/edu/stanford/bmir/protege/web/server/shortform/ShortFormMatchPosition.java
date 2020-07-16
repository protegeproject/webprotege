package edu.stanford.bmir.protege.web.server.shortform;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 */
@AutoValue
public abstract class ShortFormMatchPosition {

    @Nonnull
    public static ShortFormMatchPosition get(int start, int end) {
        checkArgument(start <= end);
        return new AutoValue_ShortFormMatchPosition(start, end);
    }

    public abstract int getStart();

    public abstract int getEnd();
}
