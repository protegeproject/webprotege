package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.shared.form.FormPurpose;
import edu.stanford.bmir.protege.web.shared.form.GetEntityCreationFormsAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityCreationFormsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */
public class GetEntityCreationFormsActionHandler extends AbstractProjectActionHandler<GetEntityCreationFormsAction, GetEntityCreationFormsResult> {

    @Nonnull
    private final EntityFormManager entityFormManager;

    @Nonnull
    private final ProjectComponent projectComponent;

    @Inject
    public GetEntityCreationFormsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull EntityFormManager entityFormManager,
                                               @Nonnull ProjectComponent projectComponent) {
        super(accessManager);
        this.entityFormManager = checkNotNull(entityFormManager);
        this.projectComponent = checkNotNull(projectComponent);
    }

    @Nonnull
    @Override
    public Class<GetEntityCreationFormsAction> getActionClass() {
        return GetEntityCreationFormsAction.class;
    }

    @Nonnull
    @Override
    public GetEntityCreationFormsResult execute(@Nonnull GetEntityCreationFormsAction action,
                                                @Nonnull ExecutionContext executionContext) {
        var entityCreationForms = entityFormManager.getFormDescriptors(action.getParentEntity(),
                                             action.getProjectId(),
                                             FormPurpose.ENTITY_CREATION);

        var formDtoTranslatorComponent = projectComponent.getFormDescriptorDtoTranslatorComponent(new EntityFrameFormDataModule());
        var formDtoTranslator = formDtoTranslatorComponent.getFormDescriptorDtoTranslator();
        var formDtos = entityCreationForms.stream()
                           .map(formDtoTranslator::toFormDescriptorDto)
                           .collect(toImmutableList());
        return GetEntityCreationFormsResult.get(formDtos);
    }
}
