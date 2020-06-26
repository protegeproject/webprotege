package edu.stanford.bmir.protege.web.shared.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetProjectFormDescriptorsResult implements Result, HasProjectId {

    @Nonnull
    public static GetProjectFormDescriptorsResult get(@Nonnull ProjectId projectId,
                                                      @Nonnull ImmutableList<FormDescriptor> formDescriptors,
                                                      @Nonnull ImmutableList<EntityFormSelector> selectionCriteria) {
        return new AutoValue_GetProjectFormDescriptorsResult(projectId, formDescriptors, selectionCriteria);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ImmutableList<FormDescriptor> getFormDescriptors();

    public abstract ImmutableList<EntityFormSelector> getFormSelectors();
}
