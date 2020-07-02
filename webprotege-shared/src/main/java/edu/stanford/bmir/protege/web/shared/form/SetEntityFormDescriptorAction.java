package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
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
public class SetEntityFormDescriptorAction implements ProjectAction<SetEntityFormDescriptorResult> {

    private ProjectId projectId;

    private FormDescriptor formDescriptor;

    private CompositeRootCriteria selectorCriteria;


    public SetEntityFormDescriptorAction(@Nonnull ProjectId projectId,
                                         @Nonnull FormDescriptor formDescriptor,
                                         @Nullable CompositeRootCriteria selectorCriteria) {
        this.projectId = checkNotNull(projectId);
        this.formDescriptor = checkNotNull(formDescriptor);
        this.selectorCriteria = checkNotNull(selectorCriteria);
    }

    @GwtSerializationConstructor
    private SetEntityFormDescriptorAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }

    @Nonnull
    public Optional<CompositeRootCriteria> getSelectorCriteria() {
        return Optional.ofNullable(selectorCriteria);
    }
}
