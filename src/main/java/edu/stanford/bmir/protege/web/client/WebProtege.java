package edu.stanford.bmir.protege.web.client;


import com.google.common.base.Optional;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import edu.stanford.bmir.protege.web.client.workspace.WorkspaceView;
import edu.stanford.bmir.protege.web.client.workspace.WorkspaceViewImpl;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;


/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class WebProtege implements EntryPoint {

    public void onModuleLoad() {
        Application.init(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("There was a problem initializing WebProtege", caught);
            }

            @Override
            public void onSuccess(Void result) {
                GWT.log("Application initialization complete.  Starting UI Initialization.");
                handleUIInitialization();
            }
        });
    }

    private void handleUIInitialization() {
        buildUI();
    }

    private void buildUI() {
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        WorkspaceView view = GWT.create(WorkspaceViewImpl.class);
        RootPanel.get().add(view);

        final Optional<String> appName = Application.get().getClientApplicationProperty(WebProtegePropertyName.APPLICATION_NAME);
        if (appName.isPresent()) {
            Window.setTitle(appName.get());
        }

    }

}
