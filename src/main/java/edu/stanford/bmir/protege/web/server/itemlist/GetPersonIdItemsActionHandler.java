package edu.stanford.bmir.protege.web.server.itemlist;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdItemsAction;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdItemsResult;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/05/15
 */
public class GetPersonIdItemsActionHandler implements ActionHandler<GetPersonIdItemsAction, GetPersonIdItemsResult> {


    private final UserDetailsManager userDetailsManager;

    @Inject
    public GetPersonIdItemsActionHandler(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = checkNotNull(userDetailsManager);
    }

    @Override
    public Class<GetPersonIdItemsAction> getActionClass() {
        return GetPersonIdItemsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetPersonIdItemsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetPersonIdItemsResult execute(GetPersonIdItemsAction action, ExecutionContext executionContext) {
        List<PersonId> result = new ArrayList<>();
        for(String itemName : action.getItemNames()) {
            if(userDetailsManager.getUserDetails(UserId.getUserId(itemName)).isPresent()) {
                result.add(new PersonId(itemName));
            }
        }
        return new GetPersonIdItemsResult(result);
    }
}
