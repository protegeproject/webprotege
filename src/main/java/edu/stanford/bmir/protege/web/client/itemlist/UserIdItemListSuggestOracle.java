package edu.stanford.bmir.protege.web.client.itemlist;

import com.google.gwt.user.client.ui.ValueBoxBase;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.itemlist.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class UserIdItemListSuggestOracle extends ItemListSuggestOracle<UserId> {

    public UserIdItemListSuggestOracle(ValueBoxBase<String> valueBoxBase, DispatchServiceManager dispatchServiceManager) {
        super(new ValueBoxCursorPositionProvider(valueBoxBase),
                new ItemNameAtCursorParserImpl(),
                new GetUserIdCompletionsActionFactory(),
                dispatchServiceManager,
                new UserIdItemRenderer());
    }
}
