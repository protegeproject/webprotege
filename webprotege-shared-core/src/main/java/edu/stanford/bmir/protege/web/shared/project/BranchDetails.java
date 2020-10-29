package edu.stanford.bmir.protege.web.shared.project;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class BranchDetails {

    @Nonnull
    public abstract BranchId getId();

    @Nonnull
    public abstract String getLabel();
}
