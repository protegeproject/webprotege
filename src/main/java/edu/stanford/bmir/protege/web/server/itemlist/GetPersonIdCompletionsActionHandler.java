package edu.stanford.bmir.protege.web.server.itemlist;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.metaproject.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdCompletionsAction;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetPersonIdCompletionsActionHandler implements ActionHandler<GetPersonIdCompletionsAction, GetPossibleItemCompletionsResult<PersonId>> {


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
    public RequestValidator<GetPersonIdCompletionsAction> getRequestValidator(GetPersonIdCompletionsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetPersonIdCompletionsResult execute(GetPersonIdCompletionsAction action, ExecutionContext executionContext) {
        List<PersonId> personIdList = new ArrayList<>();
        for(UserId userId : userDetailsManager.getUserIds()) {
            String lowerCaseCompletionString = action.getCompletionText().toLowerCase();
            if(userId.getUserName().toLowerCase().contains(lowerCaseCompletionString)) {
                personIdList.add(new PersonId(userId.getUserName()));
            }
        }
        Collections.sort(personIdList);
        return new GetPersonIdCompletionsResult(personIdList);
    }
}
