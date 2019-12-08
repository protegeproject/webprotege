package edu.stanford.bmir.protege.web.shared.viz;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-07
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetProjectEntityGraphDefaultEdgeCriteriaResult implements Result, HasProjectId {

    @Nonnull
    public abstract ImmutableList<EdgeCriteria> getEdgeCriteria();
}
