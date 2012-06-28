package edu.stanford.bmir.protege.web.client.ui;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.model.SystemEventManager;
import edu.stanford.bmir.protege.web.client.model.event.LoginEvent;
import edu.stanford.bmir.protege.web.client.model.event.PermissionEvent;
import edu.stanford.bmir.protege.web.client.model.listener.SystemListener;
import edu.stanford.bmir.protege.web.client.model.listener.SystemListenerAdapter;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.AccessPolicyUtil;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.MyWebProtegeTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.WebProtegeHome;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.search.SearchUtil;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * Class that holds all the tabs corresponding to ontologies. It also contains
 * that MyWebProtege Tab. This class manages the loading of projects and their
 * configurations.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class OntologyContainer extends TabPanel {

    private MyWebProtegeTab myWebProTab;

    private final LinkedHashMap<String, Ontology> loadedOntologiesMap = new LinkedHashMap<String, Ontology>();

    private TextField searchField;

    private SystemListener systemListener;

    public OntologyContainer() {
        super();
        buildUI();
        addTabChangeListener();
        setInitialSelection();
    }

    protected void setInitialSelection() {
        final String ontologyName = Window.Location.getParameter("ontology");
        if (ontologyName != null) {
            loadProject(new Project(ontologyName));
        }
    }

    protected void buildUI() {
        setLayoutOnTabChange(false); // TODO: check if necessary
//        setEnableTabScroll(true);
        //  addSearchField(); //FIXME: implement the search in a better way
        createAndAddHomeTab();

        SystemEventManager.getSystemEventManager().addSystemListener(getSystemListener());
    }

    protected SystemListener getSystemListener() {
        if (systemListener == null) {
            this.systemListener = new SystemListenerAdapter() {
                @Override
                public void onLogout(LoginEvent loginEvent) {
                    removeAllTabs();
                }

                @Override
                public void onPermissionsChanged(PermissionEvent permissionEvent) {
                    //TODO
                }
            };
        }
        return systemListener;
    }

    protected void removeAllTabs() {
        for (Ontology ontology : loadedOntologiesMap.values()) {
            ontology.getProject().dispose();
            hideTabStripItem(ontology);
            ontology.hide();
            ontology.destroy();
        }
        loadedOntologiesMap.clear();
        activate(0);
    }

    protected void addSearchField() {
        searchField = createSearchField();
        add(searchField);
    }

    private void onSearch() {
        String searchText = searchField.getValueAsString().trim();
        search(searchText);
    }

    private void search(String text) {
        Ontology tab = getActiveOntology();
        if (tab == null) {
            return;
        }
        AbstractTab activeTab = tab.getActiveOntologyTab();
        if (activeTab == null) {
            return;
        }
        EntityPortlet ctrlPortlet = activeTab.getControllingPortlet();
        SearchUtil su = new SearchUtil(tab.getProject(), ctrlPortlet);
        su.search(text);
    }

    public Ontology getActiveOntology() {
        Panel activeTab = getActiveTab();
        return (activeTab instanceof Ontology) ? (Ontology) activeTab : null;
    }

    protected TextField createSearchField() {
        searchField = new TextField("Search: ", "search", 200);
        searchField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onSpecialKey(Field field, EventObject e) {
                if (e.getKey() == EventObject.ENTER) {
                    onSearch();
                }
            }
        });
        return searchField;
    }

    protected void createAndAddHomeTab() {

        myWebProTab = new MyWebProtegeTab();
        myWebProTab.setTitle(myWebProTab.getLabel());
        add(myWebProTab);

        myWebProTab.getOntologiesPortlet().addSelectionListener(new SelectionListener() {
            public void selectionChanged(SelectionEvent event) {
                String projectName = event.getSelectable().getSelection().iterator().next().getName();
                loadProject(new Project(myWebProTab.getOntologiesPortlet().getOntologyData(projectName)));
            }
        });
    }


    public void loadProject(Project project) {
        Ontology ontTab = loadedOntologiesMap.get(project.getProjectName());
        if (ontTab != null) {
            activate(ontTab.getId());
            return;
        }

        UIUtil.showLoadProgessBar("Loading " + project.getProjectName(), "Loading");
        OntologyServiceManager.getInstance().loadProject(project.getProjectName(), new LoadProjectHandler(project));
    }

    public void addTab(Project project) {
        Ontology ont = new Ontology(project);
        ont.setClosable(true);
        loadedOntologiesMap.put(project.getProjectName(), ont);

        ont.addListener(new PanelListenerAdapter() {
            @Override
            public boolean doBeforeDestroy(Component component) {
                if (component instanceof Ontology) {
                    Ontology o = (Ontology) component;
                    loadedOntologiesMap.remove(o.getProject().getProjectName());
                    o.getProject().dispose();
                    hideTabStripItem(o);
                    o.hide();
                    activate(0);
                }
                return true;
            }
        });

        add(ont);
        activate(ont.getId());
        setActiveTab(ont.getId());
        ont.layoutProject();
        SystemEventManager.getSystemEventManager().requestPermissions(project);
    }

    protected void layoutProject(Project project) {
        Ontology ontTab = loadedOntologiesMap.get(project.getProjectName());
        if (ontTab == null) {
            GWT.log("Could not find ontology tab for " + project.getProjectName(), null);
            return;
        }
        ontTab.layoutProject();
    }


    /*
     * Remote calls
     */

    class LoadProjectHandler extends AbstractAsyncHandler<Integer> {
        private final Project project;

        LoadProjectHandler(Project project) {
            this.project = project;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("There were errors at loading project " + project.getProjectName(), caught);
            UIUtil.hideLoadProgessBar();
            MessageBox.alert("Load project " + project.getProjectName() + " failed.<br>" + " Message: "
                    + caught.getMessage());
        }

        @Override
        public void handleSuccess(Integer revision) {
            int serverVersion = revision.intValue();
            project.setServerVersion(serverVersion);
            UIUtil.hideLoadProgessBar();
            addTab(project);
        }
    }


    /**
     *
     */
    protected void addTabChangeListener() {
        this.addListener(new TabPanelListenerAdapter(){

            /* (non-Javadoc)
             * @see com.gwtext.client.widgets.event.TabPanelListenerAdapter#onTabChange(com.gwtext.client.widgets.TabPanel, com.gwtext.client.widgets.Panel)
             */
            @Override
            public void onTabChange(TabPanel source, Panel tab) {
                super.onTabChange(source, tab);
                AccessPolicyUtil.updateShareLink(tab.getTitle());
            }

        });
    }
}
