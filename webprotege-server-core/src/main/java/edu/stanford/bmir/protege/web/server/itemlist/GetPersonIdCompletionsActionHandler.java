package edu.stanford.bmir.protege.web.server.itemlist;

import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdCompletionsAction;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;

import javax.annotation.Nonnull;
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

    @Nonnull
    @Override
    public Class<GetPersonIdCompletionsAction> getActionClass() {
        return GetPersonIdCompletionsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetPersonIdCompletionsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetPersonIdCompletionsResult execute(@Nonnull GetPersonIdCompletionsAction action, @Nonnull ExecutionContext executionContext) {
        List<PersonId> matches = userDetailsManager.getUserIdsContainingIgnoreCase(action.getCompletionText(), 7).stream()
                .map(u -> new PersonId(u.getUserName()))
                .collect(toList());
        return new GetPersonIdCompletionsResult(matches);
    }
}
