package edu.stanford.bmir.protege.web.client;


import com.google.common.base.Optional;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import edu.stanford.bmir.protege.web.client.rpc.*;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.InvitationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.InviteUserUtil;
import edu.stanford.bmir.protege.web.client.workspace.WorkspaceViewImpl;
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
        initServletMagagers();

        // This doesn't feel like it belongs here
        if (isInvitation()) {
            InviteUserUtil inviteUserUtil = new InviteUserUtil();
            inviteUserUtil.ProcessInvitation();
        }

        buildUI();
    }

    /**
     * Checks whether the URL is an invitation URL, by analyzing the parameter
     * <code>InvitationConstants.INVITATION_URL_PARAMETER_IS_INVITATION</code>
     * in URL.
     * @return
     */
    private boolean isInvitation() {
        String isInvitationURL = Window.Location.getParameter(InvitationConstants.INVITATION_URL_PARAMETER_IS_INVITATION);
        return isInvitationURL != null && isInvitationURL.trim().contains("true");
    }

    /**
     * Force Servlet initialization - needed when running in browsers.
     */
    protected void initServletMagagers() {
        AdminServiceManager.getInstance();
        AuthenticateServiceManager.getInstance();
        OntologyServiceManager.getInstance();
        ProjectConfigurationServiceManager.getInstance();
        ChAOServiceManager.getInstance();
        HierarchyServiceManager.getInstance();
        OpenIdServiceManager.getInstance();
        NotificationServiceManager.getInstance();
    }

    protected void buildUI() {
        RootPanel.get().add(new WorkspaceViewImpl());

        final Optional<String> appName = Application.get().getClientApplicationProperty(WebProtegePropertyName.APPLICATION_NAME);
        if (appName.isPresent()) {
            Window.setTitle(appName.get());
        }

    }

}
