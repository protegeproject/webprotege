package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.form.GetEntityCreationFormsAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityCreationFormsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */
public class GetEntityCreationFormsActionHandler extends AbstractProjectActionHandler<GetEntityCreationFormsAction, GetEntityCreationFormsResult> {

    @Inject
    public GetEntityCreationFormsActionHandler(@Nonnull AccessManager accessManager) {
        super(accessManager);
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
        return null;
    }
}
