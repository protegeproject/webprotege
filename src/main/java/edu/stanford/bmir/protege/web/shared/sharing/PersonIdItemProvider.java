package edu.stanford.bmir.protege.web.shared.sharing;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.itemarea.ItemProvider;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsAction;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public class PersonIdItemProvider implements ItemProvider<PersonId> {

    private final DispatchServiceManager dispatchServiceManager;

    private List<PersonId> data = new ArrayList<>();

    public PersonIdItemProvider(DispatchServiceManager dispatchServiceManager) {
        this(dispatchServiceManager, Collections.<UserId>emptyList());
    }

    public PersonIdItemProvider(DispatchServiceManager dispatchServiceManager, final List<UserId> exclude) {
        this.dispatchServiceManager = dispatchServiceManager;
        dispatchServiceManager.execute(new GetUserIdsAction(), new DispatchServiceCallback<GetUserIdsResult>() {
            @Override
            public void handleSuccess(GetUserIdsResult result) {
                data.clear();
//                data.addAll();
                data.removeAll(exclude);
            }
        });
    }

    public List<PersonId> getItems() {

        return data;
    }

    public List<PersonId> getItemsMatchingExactly(String itemString) {
        ArrayList<PersonId> result = new ArrayList<>();
        for(PersonId userId : data) {
            if (userId.getId().equalsIgnoreCase(itemString)) {
                result.add(userId);
            }
        }
        return result;
    }

    public List<PersonId> getItemsMatching(String itemString) {
        if(itemString.trim().isEmpty()) {
            return Collections.emptyList();
        }
        List<PersonId> result = new ArrayList<>();
        for(PersonId userId : data) {
            if(userId.getId().toLowerCase().startsWith(itemString.trim().toLowerCase())) {
                result.add(new PersonId(userId.getId()));
            }
        }
        return result;
    }

    public String getRendering(PersonId item) {
        return item.getId();
    }
}
