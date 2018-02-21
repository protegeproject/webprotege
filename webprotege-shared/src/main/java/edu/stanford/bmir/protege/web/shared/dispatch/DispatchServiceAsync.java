package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jan 2018
 */
public interface DispatchServiceAsync {
    void executeAction( Action action, AsyncCallback<DispatchServiceResultContainer> callback );
}
