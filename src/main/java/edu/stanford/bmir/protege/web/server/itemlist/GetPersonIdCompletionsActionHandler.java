package edu.stanford.bmir.protege.web.server.itemlist;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdCompletionsAction;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetPersonIdCompletionsActionHandler implements ApplicationActionHandler<GetPersonIdCompletionsAction, GetPossibleItemCompletionsResult<PersonId>> {


    private final UserDetailsManager userDetailsManager;

    @Inject
    public GetPersonIdCompletionsActionHandler(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = checkNotNull(userDetailsManager);
    }

    @Override
    public Class<GetPersonIdCompletionsAction> getActionClass() {
        return GetPersonIdCompletionsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetPersonIdCompletionsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetPersonIdCompletionsResult execute(GetPersonIdCompletionsAction action, ExecutionContext executionContext) {
        List<PersonId> matches = userDetailsManager.getUserIdsContainingIgnoreCase(action.getCompletionText(), 7).stream()
                .map(u -> new PersonId(u.getUserName()))
                .collect(toList());
        return new GetPersonIdCompletionsResult(matches);
    }
}
