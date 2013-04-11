package edu.stanford.bmir.protege.web.client.ui.library.itemarea;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.UserProfileManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.UserProfileManagerServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/02/2012
 */
public class UserIdSuggestOracle implements ItemProvider<UserId> {

    private List<UserId> data = Arrays.asList(UserId.getUserId("Matthew Horridge"), UserId.getUserId("Timothy Redmond"), UserId.getUserId("Martin O'Connor"));

    public UserIdSuggestOracle(final List<UserId> exclude) {
        UserProfileManagerServiceAsync service = UserProfileManagerServiceManager.getService();
        service.getUserIds(new AsyncCallback<List<UserId>>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(List<UserId> result) {
                data = new ArrayList<UserId>(result);
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
