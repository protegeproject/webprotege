package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.GetEntityFormsAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityFormsResult;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormActionHandler extends AbstractProjectActionHandler<GetEntityFormsAction, GetEntityFormsResult> {

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
    public GetEntityFormsResult execute(@Nonnull GetEntityFormsAction action,
                                        @Nonnull ExecutionContext executionContext) {

        OWLEntity entity = action.getEntity();
        var forms = formManager.getFormDescriptors(entity, projectId)
                          .stream()
                          .map(formDescriptor -> formDataBuilder.toFormData(entity, formDescriptor))
                          .collect(toImmutableList());
        return new GetEntityFormsResult(forms);
    }

    @Nonnull
    @Override
    public Class<GetEntityFormsAction> getActionClass() {
        return GetEntityFormsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }
}
