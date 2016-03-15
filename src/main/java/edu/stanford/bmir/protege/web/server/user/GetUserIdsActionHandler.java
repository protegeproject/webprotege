package edu.stanford.bmir.protege.web.server.user;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsAction;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
public class GetUserIdsActionHandler implements ActionHandler<GetUserIdsAction, GetUserIdsResult> {

    private HasUserIds hasUserIds;

    @Inject
    public GetUserIdsActionHandler(HasUserIds hasUserIds) {
        this.hasUserIds = checkNotNull(hasUserIds);
    }

    @Override
    public Class<GetUserIdsAction> getActionClass() {
        return GetUserIdsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetUserIdsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetUserIdsResult execute(GetUserIdsAction action, ExecutionContext executionContext) {
        return new GetUserIdsResult(ImmutableList.copyOf(hasUserIds.getUserIds()));
    }
}
