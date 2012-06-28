package edu.stanford.bmir.protege.web.client;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.PermissionConstants;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.model.SystemEventManager;
import edu.stanford.bmir.protege.web.client.model.event.LoginEvent;
import edu.stanford.bmir.protege.web.client.model.event.PermissionEvent;
import edu.stanford.bmir.protege.web.client.model.listener.SystemListener;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.ApplicationPropertiesServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AuthenticateServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.HierarchyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.ICDServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.NotificationServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.OpenIdServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.ProjectConfigurationServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.ClientApplicationPropertiesCache;
import edu.stanford.bmir.protege.web.client.ui.Ontology;
import edu.stanford.bmir.protege.web.client.ui.TopPanel;
import edu.stanford.bmir.protege.web.client.ui.login.LoginUtil;

/**
 * The entry class for iCAT. It shows the introduction and login screen. If the user is already logged in the session
 *  with the appropriate permissions, it will bypass the log in.
 *  Once the user logs in, the iCAT main UI will be loaded.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class ICat implements EntryPoint {

    private final static String PROJECT_NAME = "ICD";

    private SystemListener systemListener;
    private Project project;

    public void onModuleLoad() {
        initServletMagagers();

        ProjectData data = new ProjectData();
        data.setName(getProjectName());
        project = new Project(data);

        //start the loading of the project in background
        getProjectRevisionFromServer();
        SystemEventManager.getSystemEventManager().addSystemListener(project, getSystemListener(project));

        init();
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

    /**
     * Initializes the client application properties. Makes a remote call to get them.
     * Needed to get several configurations (including OpenId).
     */
    protected void init() {
        ClientApplicationPropertiesCache.initialize(new AsyncCallback<Map<String, String>>() {
            public void onFailure(Throwable caught) {
                //failure logged in the cache
                checkUserInSession();
            }

            public void onSuccess(Map<String, String> result) {
                checkUserInSession();
            }
        });
    }


    private void getProjectRevisionFromServer() {
        //workaround for event bug
        OntologyServiceManager.getInstance().loadProject(getProjectName(), new AsyncCallback<Integer>() {
            public void onSuccess(Integer revision) {
                int serverVersion = revision.intValue();
                project.setServerVersion(serverVersion);
            }
            public void onFailure(Throwable caught) {
                GWT.log("Could not retrieve project " + PROJECT_NAME);
                //subsequent calls should notify the user
            }
        });
    }


    protected void checkUserInSession() {
        AdminServiceManager.getInstance().getCurrentUserInSession(new AsyncCallback<UserData>() {
            public void onFailure(Throwable caught) {
                GWT.log("Could not get user in session from server", caught);
                buildInitialUI();
            }

            public void onSuccess(UserData userData) {
                GlobalSettings.getGlobalSettings().setUser(userData);
                if (userData == null) {
                    buildInitialUI();
                }
            }
        });
    }

    private SystemListener getSystemListener(final Project project) {
        if (systemListener == null) {
            systemListener = new SystemListener() {

                public void onPermissionsChanged(PermissionEvent permissionEvent) {
                    ICat.this.onPermissionsChange(permissionEvent.getPermissions());
                }

                public void onLogout(LoginEvent loginEvent) {
                    ICat.this.onLogout();
                }

                public void onLogin(LoginEvent loginEvent) {
                    ICat.this.onLogin();
                }
            };
        }
        return systemListener;
    }


    protected void onLogin() {
        SystemEventManager.getSystemEventManager().requestPermissions(project, GlobalSettings.getGlobalSettings().getUserName());
    }

    protected void onLogout() {
       // Window.open(GWT.getHostPageBaseURL(), APP_NAME, null );
        Window.Location.replace(GWT.getHostPageBaseURL());
    }


    protected void onPermissionsChange(Collection<String> permissions) {
        if (GlobalSettings.getGlobalSettings().getUserName() == null) { //logout - case handled by logout
            return;
        }
        if (permissions == null) {
            onLogout();
            return;
        }
        if (permissions.contains(PermissionConstants.READ)) {
            buildUI(project);
        } else {
            MessageBox.alert("No read permission", "Your user account does not have the privileges to browse this project.");
            //TODO: show "Log in as a different user option".
            onLogout();
        }
    }


    /**
     * Build the introduction page of the application that contains a intro text,
     * and the login.
     */
    protected void buildInitialUI() {
        Panel wrappingPanel = new Panel();
        Panel introMessagePanel = new Panel();
        introMessagePanel.setHtml(getWelcomeMessage());

        Anchor signInAnchor = new Anchor("<span style=\"padding:20px\">If you don't see the sign in pop-up, you may also sign in here.</span>", true);
        signInAnchor.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                signIn();
            }
        });

        wrappingPanel.add(introMessagePanel);
        wrappingPanel.add(signInAnchor);

        RootPanel introPanel = RootPanel.get("intro");
        if (introPanel != null) {
            introPanel.add(wrappingPanel);
        }

        Timer timer = new Timer() {
            @Override
            public void run() {
                signIn();
            }
        };
        timer.schedule(10);
    }

    protected void signIn() {
        LoginUtil lu = new LoginUtil();
        lu.login(false);
    }

    /**
     * Builds the initial user interface by reading the project configuration from the server.
     * Removes the 'intro' panel and replaces the entire body with the new UI.
     */
    protected void buildUI(Project project) {
        // Main panel (contains entire user interface)
        Panel main = new Panel();
        main.setLayout(new FitLayout());
        main.setBorder(false);

        // Wrapper panel (for top and ontology panels)
        final Panel wrapper = new Panel();
        wrapper.setLayout(new AnchorLayout());
        wrapper.setBorder(false);

        // Add top panel to wrapper
        TopPanel top = getTopPanel();
        top.setBodyStyle("background-color:#CDEB8B");
        wrapper.add(getTopPanel(), new AnchorLayoutData("100% 100%"));

        Ontology ontology = new Ontology(project);
        ontology.layoutProject();
        wrapper.add(ontology, new AnchorLayoutData("100% 100%"));

        // Add main panel to viewport
        main.add(wrapper);
        new Viewport(main);

        main.doLayout();
    }

    protected TopPanel getTopPanel() {
        TopPanel top = new TopPanel();
        return top;
    }

    protected String getWelcomeMessage() {
        String text = "<div style=\"padding:20px; font-weight: bold\">" +
            "Welcome to iCAT &mdash; the Collaborative Authoring Tool for the ICD-11 alpha draft. <br/><br/>" +
            "In the alpha phase, the access to iCAT is restricted to TAG members only. Please sign in to access the iCAT platform.<br/><br/>" +
            " <span style=\"color:#003399\">To request an iCAT user account, please contact " +
            "your TAG managing editor. Alternatively, you may write an email with your information to Sara Cottler (cottlers_AT_who.int) and Tania Tudorache (tudorache_AT_stanford.edu).</span>" +
            "</div>";

    		return text;
    }

    protected String getProjectName() {
        return PROJECT_NAME;
    }


}
