package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.GetFreshFormIdAction;
import edu.stanford.bmir.protege.web.shared.form.GetFreshFormIdResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
public class GetFreshFormIdActionHandler extends AbstractProjectActionHandler<GetFreshFormIdAction, GetFreshFormIdResult> {

    @Inject
    public GetFreshFormIdActionHandler(@Nonnull AccessManager accessManager) {
        super(accessManager);
    }

    @Nonnull
    @Override
    public Class<GetFreshFormIdAction> getActionClass() {
        return GetFreshFormIdAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetFreshFormIdAction action) {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public GetFreshFormIdResult execute(@Nonnull GetFreshFormIdAction action,
                                        @Nonnull ExecutionContext executionContext) {
        return GetFreshFormIdResult.get(action.getProjectId(),
                                        FormId.generate());
    }
}
