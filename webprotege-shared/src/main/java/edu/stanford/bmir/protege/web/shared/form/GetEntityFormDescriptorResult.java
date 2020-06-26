package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetEntityFormDescriptorResult implements Result {

    @Nonnull
    public static GetEntityFormDescriptorResult get(@Nonnull ProjectId projectId,
                                                    @Nonnull FormId formId,
                                                    @Nullable FormDescriptor formDescriptor,
                                                    @Nullable CompositeRootCriteria formSelector) {
        return new AutoValue_GetEntityFormDescriptorResult(projectId,
                                                           formId,
                                                           formDescriptor,
                                                           formSelector);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();

    @Nullable
    protected abstract FormDescriptor getFormDescriptorInternal();

    @Nonnull
    public Optional<FormDescriptor> getFormDescriptor() {
        return Optional.ofNullable(getFormDescriptorInternal());
    }

    @Nullable
    protected abstract CompositeRootCriteria getFormSelectorCriteriaInternal();

    @Nonnull
    public Optional<CompositeRootCriteria> getFormSelectorCriteria() {
        return Optional.ofNullable(getFormSelectorCriteriaInternal());
    }

}
