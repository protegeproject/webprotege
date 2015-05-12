package edu.stanford.bmir.protege.web.client.ui.library.itemarea;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsAction;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/02/2012
 */
public class UserIdItemProvider implements ItemProvider<UserId> {

    private final DispatchServiceManager dispatchServiceManager;

    private List<UserId> data = new ArrayList<>();

    public UserIdItemProvider(DispatchServiceManager dispatchServiceManager) {
        this(dispatchServiceManager, Collections.<UserId>emptyList());
    }

    public UserIdItemProvider(DispatchServiceManager dispatchServiceManager, final List<UserId> exclude) {
        this.dispatchServiceManager = dispatchServiceManager;
        dispatchServiceManager.execute(new GetUserIdsAction(), new DispatchServiceCallback<GetUserIdsResult>() {
            @Override
            public void handleSuccess(GetUserIdsResult result) {
                data.clear();
                data.addAll(result.getUserIds());
                data.removeAll(exclude);
            }
        });
    }

    public List<UserId> getItems() {
        
        return data;
    }

    public List<UserId> getItemsMatchingExactly(String itemString) {
        ArrayList<UserId> result = new ArrayList<UserId>();
        for(UserId userId : data) {
            if (userId.getUserName().equalsIgnoreCase(itemString)) {
                result.add(userId);
            }
        }
        return result;
    }

    public List<UserId> getItemsMatching(String itemString) {
        if(itemString.trim().isEmpty()) {
            return Collections.emptyList();
        }
        List<UserId> result = new ArrayList<UserId>();
        for(UserId userId : data) {
            if(userId.getUserName().toLowerCase().startsWith(itemString.trim().toLowerCase())) {
                result.add(userId);
            }
        }
        return result;
    }

    public String getRendering(UserId item) {
        return item.getUserName();
    }
}
