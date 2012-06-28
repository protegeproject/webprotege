package edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.icd.ICDClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

import java.util.Collection;
import java.util.Map;

public class ManageHierarchyPortlet extends AbstractEntityPortlet {

    private static final String TOP_CLASS_PROP = "topClass";

    private static final String CREATE_CLASS_ENABLED_PROP ="create_class_enabled";
    private static final boolean CREATE_CLASS_ENABLED_DEFAULT = true;

    private static final String CHANGE_PARENT_ENABLED_PROP ="change_parent_enabled";
    private static final boolean CHANGE_PARENT_ENABLED_DEFAULT = true;

    private static final String RETIRE_CLASS_ENABLED_PROP ="retire_class_enabled";
    private static final boolean RETIRE_CLASS_ENABLED_DEFAULT = true;

    private Panel wrappingPanel;
    private Selectable selectable;
    private String topClass;

    public ManageHierarchyPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setTitle("Manage hierarchy");
    }

    protected void buildUI() {
        setPaddings(5);
        setLayout(new ColumnLayout());

        topClass = getRootClsName();

        Panel linkPanel = new Panel();

        if (isClassCreateEnabled()) {
            linkPanel.add(createCreateClsHtml()); //TODO: commented out because it invoked ICD class creation
        }

        if (isChangeParentEnabled()) {
            linkPanel.add(createMoveClsHtml());
        }

        if (isRetireClassEnabled()) {
            linkPanel.add(new HTML("<br />"));
            linkPanel.add(createRetireClsHtml());
        }

        linkPanel.add(createAdditionalHtml());

        wrappingPanel = new Panel();
        wrappingPanel.setAutoScroll(true);
        wrappingPanel.setPaddings(5);
        wrappingPanel.setLayout(new FitLayout());

        add(linkPanel, new ColumnLayoutData(.35));
        add(wrappingPanel, new ColumnLayoutData(.65));
    }

    @Override
    public void setPortletConfiguration(PortletConfiguration portletConfiguration) {
        super.setPortletConfiguration(portletConfiguration);
        topClass = getRootClsName();
        //TODO: this has only been placed here because we need to build the UI _after_ the configuration has been set.
        //Once we have a way of building UIs after the portlet configuration is set, we should do away with this invocation.
        buildUI();
    }


    private String getRootClsName() {
        PortletConfiguration portletConfiguration = getPortletConfiguration();
        if (portletConfiguration == null) {
            return topClass;
        }
        Map<String, Object> props = portletConfiguration.getProperties();
        if (props == null) {
            return topClass;
        }
        if (topClass == null) {
            topClass = (String) props.get(TOP_CLASS_PROP);
        }
        return topClass ;
    }

    /*
     * HTML code
     */

    protected HTML createCreateClsHtml() {
        HTML createClassHtml = new HTML("CREATE NEW CLASS", true);
        createClassHtml.setStylePrimaryName("manage-button");
        createClassHtml.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (UIUtil.confirmOperationAllowed(getProject())) {
                    onCreateCls();
                }
            }
        });
        return createClassHtml;
    }

    protected HTML createRetireClsHtml() {
        HTML retireClassHtml = new HTML("RETIRE CLASS", true);
        retireClassHtml.setStylePrimaryName("manage-button");
        retireClassHtml.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (UIUtil.confirmOperationAllowed(getProject())) {
                    //TT: Retire disabled at request of WHO on 14.07.2010. Will be restored in future versions.
                    //onRetireCls();
                    //TODO: ICD specific code!
                    MessageBox.alert("Retire function disabled",
                            "<b>The retire function has been disabled</b> in this release at the request of WHO. <br />" +
                    		"To retire a category, please use the <b>Change parents</b> link, and move the category under the <br />" +
                    		"<i>To be retired</i> sub-category in the relevant chapter. <br /><br />" +
                    		"If the <i>To be retired</i> sub-category does not exist in the chapter, please create it. <br /><br/>" +
                    		"The retire function will be enabled in future releases. We appologize for the inconvenience. ");
                }
            }
        });
        return retireClassHtml;
    }

    protected HTML createMoveClsHtml() {
        HTML moveHtml = new HTML("CHANGE PARENTS<br />(add or remove parents, move in hierarchy)", true);
        moveHtml.setStylePrimaryName("manage-button");
        moveHtml.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (UIUtil.confirmOperationAllowed(getProject())) {
                    onChangeParents();
                }
            }
        });
        return moveHtml;
    }


    protected HTML createAdditionalHtml() {
        return new HTML("");
    }

    /*
     * Functionality
     */

    protected void onCreateCls() {
        wrappingPanel.clear();
        CreateClassPanel createClassPanel = new CreateClassPanel(project, getCreateICDSpecificEntities());
        createClassPanel.setTopClass(topClass);
        createClassPanel.setParentsClses(UIUtil.createCollection(getEntity()));
        wrappingPanel.add(createClassPanel);
        selectable = createClassPanel;
        doLayout();
    }

    protected void onRetireCls() {
        wrappingPanel.clear();
        RetireClassPanel retireClassPanel = new RetireClassPanel(project);
        retireClassPanel.setTopClass(topClass);
        retireClassPanel.setParentsClses(UIUtil.createCollection(getEntity()));
        wrappingPanel.add(retireClassPanel);
        selectable = retireClassPanel;
        doLayout();
    }


    protected void onChangeParents() {
        wrappingPanel.clear();
        ChangeParentPanel changeParentPanel = new ChangeParentPanel(project);
        changeParentPanel.setTopClass(topClass);
        changeParentPanel.setParentsClses(UIUtil.createCollection(getEntity()));
        wrappingPanel.add(changeParentPanel);
        selectable = changeParentPanel;
        doLayout();
    }


    /*
     * Misc
     */

    @Override
    public void reload() {
        EntityData entityData = getEntity();
        if (selectable != null) {
            selectable.setSelection(UIUtil.createCollection(entityData));
        }
    }

    public Collection<EntityData> getSelection() {
       return (selectable != null) ? selectable.getSelection() : null;
    }

    private boolean getCreateICDSpecificEntities() {
        return UIUtil.getBooleanConfigurationProperty(getPortletConfiguration(), ICDClassTreePortlet.CREATE_ICD_SPECIFIC_ENTITES_PROP, ICDClassTreePortlet.CREATE_ICD_SPECIFIC_ENTITES_DEFAULT);
    }
    
    protected boolean isClassCreateEnabled() {
        return UIUtil.getBooleanConfigurationProperty(getPortletConfiguration(), CREATE_CLASS_ENABLED_PROP, CREATE_CLASS_ENABLED_DEFAULT);
    }

    protected boolean isChangeParentEnabled() {
        return UIUtil.getBooleanConfigurationProperty(getPortletConfiguration(), CHANGE_PARENT_ENABLED_PROP, CHANGE_PARENT_ENABLED_DEFAULT);
    }

    protected boolean isRetireClassEnabled() {
        return UIUtil.getBooleanConfigurationProperty(getPortletConfiguration(), RETIRE_CLASS_ENABLED_PROP, RETIRE_CLASS_ENABLED_DEFAULT);
    }

}
