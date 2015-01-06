package edu.stanford.bmir.protege.web.server.user;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsAction;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
public class GetUserIdsActionHandler implements ActionHandler<GetUserIdsAction, GetUserIdsResult> {

    private MetaProject metaProject;

    public GetUserIdsActionHandler(MetaProject metaProject) {
        this.metaProject = checkNotNull(metaProject);
    }

    @Override
    public Class<GetUserIdsAction> getActionClass() {
        return GetUserIdsAction.class;
    }

    @Override
    public RequestValidator<GetUserIdsAction> getRequestValidator(GetUserIdsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetUserIdsResult execute(GetUserIdsAction action, ExecutionContext executionContext) {
        ImmutableList.Builder<UserId> result = ImmutableList.builder();
        for(User user : metaProject.getUsers()) {
            result.add(UserId.getUserId(user.getName()));
        }
        return new GetUserIdsResult(result.build());
    }
}
