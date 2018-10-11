package edu.stanford.bmir.protege.web.shared.change;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetProjectChangesResult implements Result, HasProjectChanges {

    @Nonnull
    public static GetProjectChangesResult get(Page<ProjectChange> changes) {
        return new AutoValue_GetProjectChangesResult(changes);
    }

    @Nonnull
    public abstract Page<ProjectChange> getProjectChanges();
}
