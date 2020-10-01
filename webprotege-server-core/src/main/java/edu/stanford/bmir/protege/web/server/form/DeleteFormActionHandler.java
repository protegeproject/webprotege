package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.DeleteFormAction;
import edu.stanford.bmir.protege.web.shared.form.DeleteFormResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-15
 */
public class DeleteFormActionHandler extends AbstractProjectActionHandler<DeleteFormAction, DeleteFormResult> {


    @Nonnull
    private final EntityFormRepository entityFormRepository;


    @Inject
    public DeleteFormActionHandler(@Nonnull AccessManager accessManager,
                                   @Nonnull EntityFormRepository entityFormRepository) {
        super(accessManager);
        this.entityFormRepository = entityFormRepository;
    }

    @Nonnull
    @Override
    public Class<DeleteFormAction> getActionClass() {
        return DeleteFormAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(DeleteFormAction action) {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public DeleteFormResult execute(@Nonnull DeleteFormAction action, @Nonnull ExecutionContext executionContext) {
        var projectId = action.getProjectId();
        var formId = action.getFormId();
        entityFormRepository.deleteFormDescriptor(projectId, formId);
        return new DeleteFormResult();
    }
}
