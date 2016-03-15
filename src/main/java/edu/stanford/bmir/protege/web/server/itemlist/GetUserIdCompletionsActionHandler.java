package edu.stanford.bmir.protege.web.server.itemlist;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetUserIdCompletionsAction;
import edu.stanford.bmir.protege.web.shared.itemlist.GetUserIdCompletionsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetUserIdCompletionsActionHandler implements ActionHandler<GetUserIdCompletionsAction, GetPossibleItemCompletionsResult<UserId>> {

    private final UserDetailsManager userDetailsManager;

    @Inject
    public GetUserIdCompletionsActionHandler(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = checkNotNull(userDetailsManager);
    }

    @Override
    public Class<GetUserIdCompletionsAction> getActionClass() {
        return GetUserIdCompletionsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetUserIdCompletionsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetPossibleItemCompletionsResult<UserId> execute(GetUserIdCompletionsAction action, ExecutionContext executionContext) {
        String completionText = action.getCompletionText();
        List<UserId> result = userDetailsManager.getUserIdsContainingIgnoreCase(completionText, 10);
        Collections.sort(result);
        return new GetUserIdCompletionsResult(result);
    }
}
