package edu.stanford.bmir.protege.web.client.itemlist;

import com.google.gwt.user.client.ui.ValueBoxBase;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.itemlist.*;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class PersonIdItemListSuggestionOracle extends ItemListSuggestOracle<PersonId> {

    public PersonIdItemListSuggestionOracle(ValueBoxBase<String> valueBoxBase, DispatchServiceManager dispatchServiceManager) {
        super(new ValueBoxCursorPositionProvider(valueBoxBase),
                new ItemNameAtCursorParserImpl(),
                new GetPersonIdCompletionsActionFactory(),
                dispatchServiceManager,
                new PersonIdItemRenderer());
    }
}
