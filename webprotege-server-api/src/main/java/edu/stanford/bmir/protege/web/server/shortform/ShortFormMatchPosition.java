package edu.stanford.bmir.protege.web.server.shortform;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 */
@AutoValue
public abstract class ShortFormMatchPosition {

    @Nonnull
    public static ShortFormMatchPosition get(int start, int end) {
        return new AutoValue_ShortFormMatchPosition(start, end);
    }

    public abstract int getStart();

    public abstract int getEnd();
}
