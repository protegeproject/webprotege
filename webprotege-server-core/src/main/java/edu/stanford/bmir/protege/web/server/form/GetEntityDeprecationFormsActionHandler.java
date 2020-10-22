package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.shared.form.FormPurpose;
import edu.stanford.bmir.protege.web.shared.form.GetEntityCreationFormsResult;
import edu.stanford.bmir.protege.web.shared.form.GetEntityDeprecationFormsAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityDeprecationFormsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */
public class GetEntityDeprecationFormsActionHandler extends AbstractProjectActionHandler<GetEntityDeprecationFormsAction, GetEntityDeprecationFormsResult> {

    @Nonnull
    private final EntityFormManager entityFormManager;

    @Nonnull
    private final ProjectComponent projectComponent;

    @Inject
    public GetEntityDeprecationFormsActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull EntityFormManager entityFormManager,
                                                  @Nonnull ProjectComponent projectComponent) {
        super(accessManager);
        this.entityFormManager = checkNotNull(entityFormManager);
        this.projectComponent = checkNotNull(projectComponent);
    }

    @Nonnull
    @Override
    public Class<GetEntityDeprecationFormsAction> getActionClass() {
        return GetEntityDeprecationFormsAction.class;
    }

    @Nonnull
    @Override
    public GetEntityDeprecationFormsResult execute(@Nonnull GetEntityDeprecationFormsAction action,
                                                   @Nonnull ExecutionContext executionContext) {
        var entityCreationForms = entityFormManager.getFormDescriptors(action.getEntity(),
                                                                       action.getProjectId(),
                                                                       FormPurpose.ENTITY_DEPRECATION);

        var formDtoTranslatorComponent = projectComponent.getFormDescriptorDtoTranslatorComponent(new EntityFrameFormDataModule());
        var formDtoTranslator = formDtoTranslatorComponent.getFormDescriptorDtoTranslator();
        var formDtos = entityCreationForms.stream()
                                          .map(formDtoTranslator::toFormDescriptorDto)
                                          .collect(toImmutableList());
        return GetEntityDeprecationFormsResult.get(formDtos);
    }
}
