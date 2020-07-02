package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSelector;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.SetEntityFormDescriptorAction;
import edu.stanford.bmir.protege.web.shared.form.SetEntityFormDescriptorResult;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
public class SetEntityFormDescriptorActionHandler extends AbstractProjectActionHandler<SetEntityFormDescriptorAction, SetEntityFormDescriptorResult> {

    @Nonnull
    private final EntityFormRepository entityFormRepository;

    private final EntityFormSelectorRepository entityFormSelectorRepository;

    @Inject
    public SetEntityFormDescriptorActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull EntityFormRepository entityFormRepository,
                                                EntityFormSelectorRepository entityFormSelectorRepository) {
        super(accessManager);
        this.entityFormRepository = entityFormRepository;
        this.entityFormSelectorRepository = entityFormSelectorRepository;
    }

    @Nonnull
    @Override
    public Class<SetEntityFormDescriptorAction> getActionClass() {
        return SetEntityFormDescriptorAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public SetEntityFormDescriptorResult execute(@Nonnull SetEntityFormDescriptorAction action,
                                                 @Nonnull ExecutionContext executionContext) {
        var formDescriptor = action.getFormDescriptor();
        var selectorCriteria = action.getSelectorCriteria();
        var projectId = action.getProjectId();
        entityFormRepository.saveFormDescriptor(projectId,
                                                formDescriptor);

        selectorCriteria.ifPresent(compositeRootCriteria -> {
            var formId = formDescriptor.getFormId();
            entityFormSelectorRepository.save(EntityFormSelector.get(
                    projectId,
                    compositeRootCriteria,
                    formId));
        });
        return SetEntityFormDescriptorResult.get();
    }
}
