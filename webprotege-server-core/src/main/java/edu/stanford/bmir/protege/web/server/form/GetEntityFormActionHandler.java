package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityFormAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityFormResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormActionHandler extends AbstractProjectActionHandler<GetEntityFormAction, GetEntityFormResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormManager formManager;

    @Nonnull
    private final EntityFrameFormDataBuilder formDataBuilder;

    @Inject
    public GetEntityFormActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull ProjectId projectId,
                                      @Nonnull EntityFormManager formManager,
                                      @Nonnull EntityFrameFormDataBuilder formDataBuilder) {
        super(accessManager);
        this.projectId = projectId;
        this.formManager = formManager;
        this.formDataBuilder = formDataBuilder;
    }

    @Nonnull
    @Override
    public GetEntityFormResult execute(@Nonnull GetEntityFormAction action,
                                       @Nonnull ExecutionContext executionContext) {

        OWLEntity entity = action.getEntity();
        return formManager.getFormDescriptor(entity, projectId)
                          .map(formDescriptor -> {
                              var formData = formDataBuilder.getFormData(entity, formDescriptor);
                              return new GetEntityFormResult(formDescriptor, formData);

                          })
                          .orElse(new GetEntityFormResult());


    }

    @Nonnull
    @Override
    public Class<GetEntityFormAction> getActionClass() {
        return GetEntityFormAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }
}
