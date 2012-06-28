package edu.stanford.bmir.protege.web.client;

import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.Ext;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.rpc.*;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.ClientApplicationPropertiesCache;
import edu.stanford.bmir.protege.web.client.ui.OntologyContainer;
import edu.stanford.bmir.protege.web.client.ui.TopPanel;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.InvitationConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.InviteUserUtil;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.projectlist.ProjectListDisplayImpl;
import edu.stanford.bmir.protege.web.server.ProjectManager;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class WebProtege implements EntryPoint {

    public void onModuleLoad() {

        initServletMagagers();
        Timer timer = new Timer() {
            @Override
            public void run() {
                initializeClientApplicationPropertiesCache();
            }
        };
        timer.schedule(10);
        if (isInvitation()) {
            InviteUserUtil inviteUserUtil = new InviteUserUtil();
            inviteUserUtil.ProcessInvitation();
        }
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

    private void initializeClientApplicationPropertiesCache() {
        ClientApplicationPropertiesCache.initialize(new AsyncCallback<Map<String, String>>() {
            public void onFailure(Throwable caught) {
                init();
                Ext.getBody().unmask();
            }

            public void onSuccess(Map<String, String> result) {
                init();
                Ext.getBody().unmask();
            }
        });
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
        ICDServiceManager.getInstance();
        HierarchyServiceManager.getInstance();
        OpenIdServiceManager.getInstance();
        NotificationServiceManager.getInstance();

    }


    protected void buildUI() {

        Panel rootPanel = new Panel();
        rootPanel.setLayout(new FitLayout());
        rootPanel.setCls("white-bg");
        rootPanel.setPaddings(2);

        final Panel splittingPanel = new Panel();
        splittingPanel.setLayout(new AnchorLayout());
//        splittingPanel.setCls("white-bg");
        splittingPanel.setBorder(false);
        splittingPanel.setAutoScroll(false);

        splittingPanel.add(new TopPanel(), new AnchorLayoutData("100% 50%"));
        splittingPanel.add(new OntologyContainer(), new AnchorLayoutData("100% 90%"));


        rootPanel.add(splittingPanel);
        Viewport viewport = new Viewport(rootPanel);

        rootPanel.doLayout();
    }

    /*
     * Restore user from session.
     */
    protected void init() {
        AdminServiceManager.getInstance().getCurrentUserInSession(new AsyncCallback<UserData>() {
            public void onFailure(Throwable caught) {
                GWT.log("Could not get server permission from server", caught);
                buildUI();
            }

            public void onSuccess(UserData userData) {
                GlobalSettings.getGlobalSettings().setUser(userData);
                buildUI();
            }
        });
    }

}
