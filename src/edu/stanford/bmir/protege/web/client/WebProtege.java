package edu.stanford.bmir.protege.web.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.rpc.*;
import edu.stanford.bmir.protege.web.client.ui.ProjectDisplayContainerPanel;
import edu.stanford.bmir.protege.web.client.ui.TopPanel;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.InvitationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.InviteUserUtil;

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
        ApplicationPropertiesServiceManager.getInstance();
        AuthenticateServiceManager.getInstance();
        OntologyServiceManager.getInstance();
        ProjectConfigurationServiceManager.getInstance();
        ChAOServiceManager.getInstance();
        HierarchyServiceManager.getInstance();
        OpenIdServiceManager.getInstance();
        NotificationServiceManager.getInstance();
    }

    protected void buildUI() {
        Panel rootPanel = new Panel();
        rootPanel.setId("rootpanel");
        rootPanel.setLayout(new FitLayout());
        rootPanel.setCls("white-bg");
        rootPanel.setPaddings(2);

        final Panel splittingPanel = new Panel();
        splittingPanel.setId("splittingpanel");

        splittingPanel.setLayout(new AnchorLayout());
        splittingPanel.setBorder(false);
        splittingPanel.setAutoScroll(false);

        splittingPanel.add(new TopPanel(), new AnchorLayoutData("100% 50px"));

        final ProjectDisplayContainerPanel projectDisplayContainerPanel = new ProjectDisplayContainerPanel();
        projectDisplayContainerPanel.setId("ontologycontainer");

        splittingPanel.add(projectDisplayContainerPanel, new AnchorLayoutData("100% 90%"));

        projectDisplayContainerPanel.addListener(new TabPanelListenerAdapter() {
            @Override
            public boolean doBeforeTabChange(TabPanel source, Panel newPanel, Panel oldPanel) {
                GWT.log("Project tab changed");
                return true;
            }

            @Override
            public boolean doBeforeClose(Panel panel) {
                GWT.log("Project tab close");
                return true;
            }
        });

        rootPanel.add(splittingPanel);
        Viewport viewport = new Viewport(rootPanel);

        rootPanel.doLayout();
    }

}
