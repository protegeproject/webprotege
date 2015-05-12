package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetUserIdCompletionsActionFactory implements GetPossibleItemCompletionsActionFactory<UserId> {

    @Override
    public GetPossibleItemCompletionsAction<UserId> createGetPossibleItemCompletionsAction(String currentItemName) {
        return new GetUserIdCompletionsAction(currentItemName);
    }
}
