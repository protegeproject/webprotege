package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.List;

public interface UserProfileManagerServiceAsync {

    void getUserIds(AsyncCallback<List<UserId>> async);
}
