package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.Optional;
import java.util.function.Predicate;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormsActionHandler extends AbstractProjectActionHandler<GetEntityFormsAction, GetEntityFormsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormManager formManager;

    @Nonnull
    private final ProjectComponent projectComponent;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityFormsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull EntityFormManager formManager,
                                       @Nonnull ProjectComponent projectComponent,
                                       @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.projectId = projectId;
        this.formManager = formManager;
        this.projectComponent = projectComponent;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public GetEntityFormsResult execute(@Nonnull GetEntityFormsAction action,
                                        @Nonnull ExecutionContext executionContext) {
        var pageRequests = action.getFormPageRequests();
        var pageRequestIndex = FormPageRequestIndex.create(pageRequests);
        var entity = action.getEntity();
        var langTagFilter = action.getLangTagFilter();
        var ordering = action.getGridControlOrdering();
        var formRegionOrderingIndex = FormRegionOrderingIndex.get(ordering);
        var formRegionFilterIndex = FormRegionFilterIndex.get(action.getFilters());
        var module = new EntityFrameFormDataModule(formRegionOrderingIndex,
                                                   langTagFilter,
                                                   pageRequestIndex,
                                                   formRegionFilterIndex);
        var formDataDtoBuilder = projectComponent.getEntityFrameFormDataComponentBuilder(module).formDataBuilder();
        var formsFilterList = action.getFormFilter();
        var formSubject = Optional.<FormSubject>of(FormEntitySubject.get(entity));
        var forms = formManager.getFormDescriptors(entity, projectId, FormPurpose.ENTITY_EDITING)
                               .stream()
                               .filter(byFormIds(formsFilterList))
                               .map(formDescriptor -> formDataDtoBuilder.toFormData(formSubject, formDescriptor))
                               .collect(toImmutableList());

        var entityData = renderingManager.getRendering(entity);
        return new GetEntityFormsResult(entityData, action.getFormFilter(), forms);
    }

    public static Predicate<FormDescriptor> byFormIds(ImmutableList<FormId> formsFilterList) {
        return (FormDescriptor formDescriptor) -> formsFilterList.isEmpty() || formsFilterList.contains(formDescriptor.getFormId());
    }

    @Nonnull
    @Override
    public Class<GetEntityFormsAction> getActionClass() {
        return GetEntityFormsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetEntityFormsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }
}
