package edu.stanford.bmir.protege.web.client;


import com.google.common.base.Optional;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.client.workspace.ApplicationPresenter;
import edu.stanford.bmir.protege.web.client.workspace.ApplicationView;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;


/**
 * @author Matthew Horridge
 */
public class WebProtege implements EntryPoint {

    public void onModuleLoad() {
        WebProtegeInitializer initializer = WebProtegeClientInjector.getWebProtegeInitializer();
        initializer.init(new AsyncCallback<Void>() {
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
        WebProtegeClientBundle.BUNDLE.buttons().ensureInjected();

        ApplicationPresenter applicationPresenter = WebProtegeClientInjector.getApplicationPresenter();
        ApplicationView applicationView = applicationPresenter.getApplicationView();

        RootLayoutPanel.get().add(applicationView);


        HasClientApplicationProperties properties = WebProtegeClientInjector.getClientApplicationProperties();
        final Optional<String> appName = properties.getClientApplicationProperty(WebProtegePropertyName.APPLICATION_NAME);
        if (appName.isPresent()) {
            Window.setTitle(appName.get());
        }


        ActivityManager activityManager =  WebProtegeClientInjector.getActivityManager();
        activityManager.setDisplay(applicationView);

        PlaceHistoryHandler placeHistoryHandler = WebProtegeClientInjector.getPlaceHistoryHandler();
        placeHistoryHandler.handleCurrentHistory();


    }

}
