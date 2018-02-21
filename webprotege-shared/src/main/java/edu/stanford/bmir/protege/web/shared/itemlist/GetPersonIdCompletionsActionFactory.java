package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.sharing.PersonId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetPersonIdCompletionsActionFactory implements GetPossibleItemCompletionsActionFactory<PersonId> {
    @Override
    public GetPossibleItemCompletionsAction<PersonId> createGetPossibleItemCompletionsAction(String currentItemName) {
        return new GetPersonIdCompletionsAction(currentItemName);
    }
}
